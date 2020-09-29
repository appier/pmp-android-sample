package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdUnitIdentifier;
import com.appier.ads.AppierError;
import com.appier.ads.AppierNativeAd;
import com.appier.ads.AppierNativeViewBinder;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;

public class NativeBasicFragment extends BaseFragment {

    private Context mContext;
    private AppierNativeAd mAppierNativeAd;

    public NativeBasicFragment() {}

    public static NativeBasicFragment newInstance(Context context) {
        return new NativeBasicFragment(context);
    }

    private NativeBasicFragment(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sdk_native_basic, container, false);
    }

    @Override
    protected void onViewVisible(View view) {

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        final String APPIER_AD_UNIT_ID = getString(R.string.zone_native);

        LinearLayout adContainer = view.findViewById(R.id.ad_container);
        adContainer.removeAllViews();

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

        mAppierNativeAd = new AppierNativeAd(mContext, new AppierAdUnitIdentifier(APPIER_AD_UNIT_ID), new EventListener(adContainer));
        mAppierNativeAd.setViewBinder(appierNativeViewBinder);

        mAppierNativeAd.setZoneId(APPIER_AD_UNIT_ID);

        // Set targeting should be done before loadAd()
        AppierAdHelper.setTargeting(mAppierNativeAd);

        mAppierNativeAd.loadAd();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
     * AppierNativeAd EventListener to handle event callbacks
     */
    private class EventListener implements AppierNativeAd.EventListener {

        private LinearLayout adContainer;

        private EventListener(LinearLayout container) {
            adContainer = container;
        }

        @Override
        public void onAdLoaded(AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdLoaded()");
            mDemoFlowController.notifyAdBid();

            // Display AppierNativeAd view to container when AdLoaded
            View adView = appierNativeAd.getAdView();
            adContainer.addView(adView);
        }

        @Override
        public void onAdNoBid(AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdNoBid()");
            mDemoFlowController.notifyAdNoBid();
        }

        @Override
        public void onAdLoadFail(AppierError appierError, AppierNativeAd appierNativeAd) {
            Appier.log("[Sample App] onAdLoadFail()", appierError.toString());
            mDemoFlowController.notifyAdError(appierError);
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
