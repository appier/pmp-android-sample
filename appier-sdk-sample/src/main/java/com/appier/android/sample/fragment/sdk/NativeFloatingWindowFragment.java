package com.appier.android.sample.fragment.sdk;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.AppierNativeAd;
import com.appier.ads.AppierNativeViewBinder;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.AppierAdHelper;

public class NativeFloatingWindowFragment extends BaseFloatingWindowFragment {

    private AppierNativeAd mAppierNativeAd;

    public NativeFloatingWindowFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    protected void loadAdInContainer(LinearLayout adContainer) {

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        /*
         * Create AppierNativeAd and display in the container using ViewBinder
         */
        AppierNativeViewBinder appierNativeViewBinder = new AppierNativeViewBinder.Builder(R.layout.template_native_ad_full_1)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        mAppierNativeAd = new AppierNativeAd(getActivity(), new EventListener(adContainer));
        mAppierNativeAd.setViewBinder(appierNativeViewBinder);
        mAppierNativeAd.setZoneId(getResources().getString(R.string.zone_native));
        mAppierNativeAd.loadAd();
    }

    protected void destroyAdView() {
        if (mAppierNativeAd != null) {
            mAppierNativeAd.destroy();
        }
    }

    private class EventListener implements AppierNativeAd.EventListener {

        private LinearLayout adContainer;

        private EventListener(LinearLayout container) {
            adContainer = container;
        }

        @Override
        public void onAdLoaded(AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdLoaded()");
            View adView = appierNativeAd.getAdView();
            adContainer.addView(adView);
            mDemoFlowController.notifyAdBid();
        }

        @Override
        public void onAdNoBid(AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdNoBid()");
            mDemoFlowController.notifyAdNoBid();
            if (mFloatViewManager != null) {
                mFloatViewManager.close();
            }
        }

        @Override
        public void onAdLoadFail(AppierError appierError, AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdLoadFail()", appierError.toString());
            mDemoFlowController.notifyAdError(appierError);
            if (mFloatViewManager != null) {
                mFloatViewManager.close();
            }
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
        }

        @Override
        public void onAdClickFail(AppierError appierError, AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdClickFail()");
        }

    }

}
