package com.appier.android.sample.activity.sdk;

import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.AppierBannerAd;
import com.appier.ads.AppierError;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.FloatViewManager;
import com.appier.android.sample.fragment.BaseFragment;

public class BannerFloatingWindowActivity  extends BaseActivity {
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

    public static class DemoFragment extends BaseFragment {
        private FloatViewManager mFloatViewManager;
        private LinearLayout mAdContainer;
        private View mAdView;
        private AppierBannerAd mAppierBannerAd;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mFloatViewManager = new FloatViewManager(getActivity(), new FloatViewManager.OnFloatViewEventListener() {
                @Override
                public void onOpen(LinearLayout contentContainer) {
                    mAdContainer = contentContainer;
                    loadBanner(getActivity(), getResources().getString(R.string.zone_300x250), 300, 250);
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
                    if (mAppierBannerAd != null) {
                        mAppierBannerAd.destroy();
                    }
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_sdk_banner_floating_window, container, false);
            view.findViewById(R.id.button_load).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFloatViewManager.openWithPermissionCheck();
                }
            });
            return view;
        }

        @Override
        protected void onViewVisible(View view) {}

        public FloatViewManager getFloatViewManager() {
            return mFloatViewManager;
        }

        private void loadBanner(Context context, String zoneId, int width, int height) {
            mAppierBannerAd = new AppierBannerAd(context, new AppierBannerAd.EventListener() {
                @Override
                public void onAdLoaded(AppierBannerAd appierBannerAd) {
                    Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");
                    if (mAdContainer != null) {
                        mAdView = appierBannerAd.getView();
                        mAdContainer.addView(mAdView);
                    }
                }

                @Override
                public void onAdNoBid(AppierBannerAd appierBannerAd) {
                    Appier.log("[Sample App]", "[Banner]", "onAdNoBid()");
                }

                @Override
                public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
                    Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
                }

                @Override
                public void onViewClick(AppierBannerAd appierBannerAd) {
                    Appier.log("[Sample App]", "[Banner]", "onViewClick()");
                }
            });
            mAppierBannerAd.setAdDimension(width, height);
            mAppierBannerAd.setZoneId(zoneId);

            Appier.log("[Sample App]", "====== load Appier Banner ======");
            mAppierBannerAd.loadAd();
        }
    }
}