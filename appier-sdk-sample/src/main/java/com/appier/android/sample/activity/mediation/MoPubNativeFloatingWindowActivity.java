package com.appier.android.sample.activity.mediation;

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
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.FloatViewManager;
import com.appier.android.sample.fragment.BaseFragment;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.AppierNativeAdRenderer;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;


public class MoPubNativeFloatingWindowActivity extends BaseActivity {
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
        private FloatViewManager mFloatViewManager;
        private LinearLayout mAdContainer;
        private FrameLayout mOverlayFrame;
        private MoPubNative moPubNative;
        private NativeAd.MoPubNativeEventListener moPubNativeEventListener;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mFloatViewManager = new FloatViewManager(getActivity(), new FloatViewManager.OnFloatViewEventListener() {
                @Override
                public void onOpen(LinearLayout contentContainer) {
                    mAdContainer = contentContainer;
                    View view = getView();
                    if (view != null) {
                        view.findViewById(R.id.button_load).setVisibility(View.GONE);
                        mOverlayFrame.setVisibility(View.VISIBLE);
                    }
                    mAdContainer.removeAllViews();
                    loadMoPubNative(getActivity(), mAdContainer, getResources().getString(R.string.mopub_adunit_native));
                }

                @Override
                public void onDrawOverlayPermissionResult(boolean isPermissionGranted) {
                    if (isPermissionGranted) {
                        mFloatViewManager.open();
                    }
                }

                @Override
                public void onClose(LinearLayout contentContainer) {
                    if (moPubNative != null) {
                        moPubNative.destroy();
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
        public void onDestroy() {
            super.onDestroy();
            if (moPubNative != null) {
                moPubNative.destroy();
            }
        }

        @Override
        protected void onViewVisible(View view) {}

        public FloatViewManager getFloatViewManager() {
            return mFloatViewManager;
        }

        private void loadMoPubNative(final Context context, final LinearLayout parentLayout, String adunitId) {

            Appier.setTestMode(true);

            /*
             * (Optional) Set GDPR and COPPA explicitly to follow the regulations
             */
            Appier.setGDPRApplies(true);
            Appier.setCoppaApplies(true);

            /*
             * (Required) MoPub NativeAd mediation integration
             */
            moPubNativeEventListener = new NativeAd.MoPubNativeEventListener() {
                @Override
                public void onImpression(View view) {
                    Appier.log("[Sample App]", "Native ad recorded an impression.");
                    // Impress is recorded - do what is needed AFTER the ad is visibly shown here.
                }

                @Override
                public void onClick(View view) {
                    Appier.log("[Sample App]", "Native ad recorded a click.");
                    // Click tracking.
                }
            };

            MoPubNative.MoPubNativeNetworkListener moPubNativeNetworkListener = new MoPubNative.MoPubNativeNetworkListener() {
                @Override
                public void onNativeLoad(final NativeAd nativeAd) {
                    Appier.log("[Sample App]", "Native ad has loaded.");

                    final AdapterHelper adapterHelper = new AdapterHelper(context, 0, 3); // When standalone, any range will be fine.

                    // Retrieve the pre-built ad view that AdapterHelper prepared for us.
                    View adView = adapterHelper.getAdView(null, null, nativeAd, new ViewBinder.Builder(0).build());

                    // Set the native event listeners (onImpression, and onClick).
                    nativeAd.setMoPubNativeEventListener(moPubNativeEventListener);

                    // Add the ad view to our view hierarchy
                    parentLayout.addView(adView);
                }

                @Override
                public void onNativeFail(NativeErrorCode errorCode) {
                    Appier.log("[Sample App]", "Native ad failed to load with error:", errorCode.toString());
                }
            };

            ViewBinder viewBinder = new ViewBinder.Builder(R.layout.template_native_ad_full_1)
                    .mainImageId(R.id.native_main_image)
                    .iconImageId(R.id.native_icon_image)
                    .titleId(R.id.native_title)
                    .textId(R.id.native_text)
                    .callToActionId(R.id.native_cta)
                    .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                    .build();

            // Note: Appier zone_id is integrated through server extra on MoPub console
            AppierNativeAdRenderer appierNativeAdRenderer = new AppierNativeAdRenderer(viewBinder);
            MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

            moPubNative = new MoPubNative(context, adunitId, moPubNativeNetworkListener);

            moPubNative.registerAdRenderer(appierNativeAdRenderer);
            moPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);

            Appier.log("[Sample App]", "====== make request ======");
            moPubNative.makeRequest();

        }
    }
}