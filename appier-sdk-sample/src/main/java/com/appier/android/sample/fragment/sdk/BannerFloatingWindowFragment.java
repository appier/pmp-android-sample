package com.appier.android.sample.fragment.sdk;

import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.AppierBannerAd;
import com.appier.ads.AppierError;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.AppierAdHelper;

public class BannerFloatingWindowFragment extends BaseFloatingWindowFragment {

    private AppierBannerAd mAppierBannerAd;

    public BannerFloatingWindowFragment() {
    }

    protected void loadAdInContainer(LinearLayout adContainer) {

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        /*
         * Load Ad in ad container layout
         */
        mAppierBannerAd = new AppierBannerAd(getActivity(), new EventListener(adContainer));
        mAppierBannerAd.setAdDimension(300, 250);
        mAppierBannerAd.setZoneId(getResources().getString(R.string.zone_300x250));
        mAppierBannerAd.loadAd();
    }

    protected void destroyAdView() {
        if (mAppierBannerAd != null) {
            mAppierBannerAd.destroy();
        }
    }

    private class EventListener implements AppierBannerAd.EventListener {

        private LinearLayout mAdContainer;

        private EventListener(LinearLayout adContainer) {
            mAdContainer = adContainer;
        }

        @Override
        public void onAdLoaded(AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");
            mDemoFlowController.notifyAdBid();
            mAdContainer.addView(appierBannerAd.getView());
        }

        @Override
        public void onAdNoBid(AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onAdNoBid()");
            mDemoFlowController.notifyAdNoBid();
            if (mFloatViewManager != null) {
                mFloatViewManager.close();
            }
        }

        @Override
        public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
            mDemoFlowController.notifyAdError(appierError);
            if (mFloatViewManager != null) {
                mFloatViewManager.close();
            }
        }

        @Override
        public void onViewClick(AppierBannerAd appierBannerAd) {
            Appier.log("[Sample App]", "[Banner]", "onViewClick()");
        }

    }

}
