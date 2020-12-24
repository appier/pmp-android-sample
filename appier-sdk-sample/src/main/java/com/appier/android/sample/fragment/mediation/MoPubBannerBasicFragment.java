package com.appier.android.sample.fragment.mediation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.mobileads.AppierPredictHandler;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.HashMap;
import java.util.Map;


public class MoPubBannerBasicFragment extends BaseFragment implements MoPubView.BannerAdListener {

    private MoPubView mMoPubView;

    public MoPubBannerBasicFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mediation_mopub_banner_basic, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMoPubView != null) {
            mMoPubView.destroy();
        }
    }

    @Override
    protected void onViewVisible(View view) {
        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        final String MOPUB_AD_UNIT_ID = getString(R.string.mopub_adunit_predict_banner_300x250);
        final int AD_WIDTH = 300;
        final int AD_HEIGHT = 250;

        /*
         * Initialize MoPubView and load banner
         *
         * To enable Appier MoPub Mediation, the AdUnit requires at least one "Network line item",
         * with the following settings:
         *
         *   "Custom event class": "com.mopub.mobileads.AppierBanner".
         *   "Custom event data":  { "zoneId": "<THE ZONE ID PROVIDED BY APPIER>" }
         *
         */

        Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(AppierDataKeys.AD_WIDTH_LOCAL, AD_WIDTH);
        localExtras.put(AppierDataKeys.AD_HEIGHT_LOCAL, AD_HEIGHT);

        mMoPubView = getView().findViewById(R.id.banner_container_300_250);

        /*
         *  Optional: Required when integrating with Appier predict
         *
         *  To achieve the best performance, please set "Keyword targeting" for each line item in MoPub
         *  console with the following values:
         *  Keyword targeting:
         *      appier_zone_<THE ZONE ID PROVIDED BY APPIER>:1
         *      appier_predict_ver:1
         */

        localExtras.put(AppierDataKeys.AD_UNIT_ID_LOCAL, MOPUB_AD_UNIT_ID);
        mMoPubView.setKeywords(AppierPredictHandler.getKeywordTargeting(MOPUB_AD_UNIT_ID));

        // Load Ad!
        mMoPubView.setLocalExtras(localExtras);
        mMoPubView.setBannerAdListener(this);
        mMoPubView.setAdUnitId(MOPUB_AD_UNIT_ID);
        mMoPubView.loadAd();
    }

    /*
     * Override MoPubView.BannerAdListener functions to handle event callbacks
     */

    @Override
    public void onBannerLoaded(MoPubView banner) {
        Appier.log("[Sample App]", "onBannerLoaded()");
        mDemoFlowController.notifyAdBid();
    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        Appier.log("[Sample App]", "onBannerFailed():", errorCode.toString());
        mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
    }

    @Override
    public void onBannerClicked(MoPubView banner) {
        Appier.log("[Sample App]", "onBannerClicked()");
    }

    @Override
    public void onBannerExpanded(MoPubView banner) {
        Appier.log("[Sample App]", "onBannerExpanded()");
    }

    @Override
    public void onBannerCollapsed(MoPubView banner) {
        Appier.log("[Sample App]", "onBannerCollapsed()");
    }

}
