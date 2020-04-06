package com.appier.android.sample.activity.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.AppierNativeAd;
import com.appier.ads.AppierNativeViewBinder;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.FloatViewManager;
import com.appier.android.sample.fragment.BaseFragment;

public class NativeFloatingWindowActivity extends BaseActivity {
    DemoFragment mDemoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDemoFragment = new DemoFragment();
        addFragment(mDemoFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDemoFragment.getFloatViewManager().handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDemoFragment.getFloatViewManager().close();
    }

    public static class DemoFragment extends BaseFragment {
        private Context mContext;
        private FloatViewManager mFloatViewManager;
        private LinearLayout mAdContainer;
        private FrameLayout mOverlayFrame;
        private View mAdView;
        private AppierNativeAd mAppierNativeAd;

        public DemoFragment() {}

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mContext = getActivity();

            mFloatViewManager = new FloatViewManager(getActivity(), new FloatViewManager.OnFloatViewEventListener() {
                @Override
                public void onOpen(LinearLayout contentContainer) {
                    mAdContainer = contentContainer;
                    View view = getView();
                    if (view != null) {
                        view.findViewById(R.id.button_load).setVisibility(View.GONE);
                        mOverlayFrame.setVisibility(View.VISIBLE);
                    }
                    loadNativeAdUnit(mContext, getResources().getString(R.string.zone_native));
                }

                @Override
                public void onDrawOverlayPermissionResult(boolean isPermissionGranted) {
                    if (isPermissionGranted) {
                        mFloatViewManager.open();
                    }
                }

                @Override
                public void onClose(LinearLayout contentContainer) {
                    if (mAdView != null) {
                        contentContainer.removeView(mAdView);
                    }
                    if (mAppierNativeAd != null) {
                        mAppierNativeAd.destroy();
                    }
                    View view = getView();
                    if (view != null) {
                        view.findViewById(R.id.button_load).setVisibility(View.VISIBLE);
                        mOverlayFrame.setVisibility(View.GONE);
                    }
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_common_floating_window, container, false);
            view.findViewById(R.id.button_load).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFloatViewManager.openWithPermissionCheck();
                }
            });
            mOverlayFrame = view.findViewById(R.id.overlay_frame);
            mOverlayFrame.setVisibility(View.GONE);
            return view;
        }

        @Override
        protected void onViewVisible(View view) {}

        public FloatViewManager getFloatViewManager() {
            return mFloatViewManager;
        }

        private void loadNativeAdUnit(Context context, String zoneId) {

            Appier.setTestMode(true);

            /*
             * (Optional) Set GDPR and COPPA explicitly to follow the regulations
             */
            Appier.setGDPRApplies(true);
            Appier.setCoppaApplies(true);

            /*
             * (Required) Appier Native Ad integration
             */
            AppierNativeViewBinder appierNativeViewBinder = new AppierNativeViewBinder.Builder(R.layout.template_native_ad_full_2)
                    .mainImageId(R.id.native_main_image)
                    .iconImageId(R.id.native_icon_image)
                    .titleId(R.id.native_title)
                    .textId(R.id.native_text)
                    .callToActionId(R.id.native_cta)
                    .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                    .build();

            mAppierNativeAd = new AppierNativeAd(context, new AppierNativeAd.EventListener() {
                @Override
                public void onAdLoaded(AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App] onAdLoaded()");
                    if (mAdContainer != null) {
                        mAdView = appierNativeAd.getAdView();
                        mAdContainer.addView(mAdView);
                    }
                }

                @Override
                public void onAdNoBid(AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App] onAdNoBid()");
                }

                @Override
                public void onAdLoadFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App] onAdLoadFail()", appierError.toString());
                }

                @Override
                public void onAdShown(AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App] onAdShown()");
                }

                @Override
                public void onImpressionRecorded(AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App] onImpressionRecorded()");
                }

                @Override
                public void onImpressionRecordFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App] onImpressionRecordFail()", appierError.toString());
                }

                @Override
                public void onAdClick(AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App] onAdClick()");
                    mFloatViewManager.close();
                }

                @Override
                public void onAdClickFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App] onAdClickFail()");
                }
            });

            mAppierNativeAd.setViewBinder(appierNativeViewBinder);
            mAppierNativeAd.setZoneId(zoneId);

            /*
             * Load Ad and display in the UI through the view binder
             */
            Appier.log("[Sample App]", "====== load Appier Native ======");
            mAppierNativeAd.loadAd();
        }

    }
}