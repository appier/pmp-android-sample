package com.appier.android.sample.fragment.mediation;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseInterstitialFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.mobileads.AppierPredictHandler;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.util.HashMap;
import java.util.Map;

public class MoPubInterstitialFragment extends BaseInterstitialFragment implements MoPubInterstitial.InterstitialAdListener {

    private MoPubInterstitial mMoPubInterstitial;

    public MoPubInterstitialFragment() {}

    @Override
    protected void onViewVisible(View view) {}

    /*
     * The function handle MoPub Interstitial life cycle
     */
    protected void createInterstitial(Context context) {

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        final String MOPUB_AD_UNIT_ID = getString(R.string.mopub_adunit_predict_interstitial);
        final int AD_WIDTH = 320;
        final int AD_HEIGHT = 480;

        /*
         * Initialize MoPub Interstitial
         *
         * To enable Appier MoPub Mediation, the AdUnit requires at least one "Network line item",
         * with the following settings:
         *
         *   "Custom event class": "com.mopub.mobileads.AppierInterstitial".
         *   "Custom event data":  { "zoneId": "<THE ZONE ID PROVIDED BY APPIER>" }
         *
         */

        Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(AppierDataKeys.AD_WIDTH_LOCAL, AD_WIDTH);
        localExtras.put(AppierDataKeys.AD_HEIGHT_LOCAL, AD_HEIGHT);

        mMoPubInterstitial = new MoPubInterstitial((Activity) context, MOPUB_AD_UNIT_ID);

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
        mMoPubInterstitial.setKeywords(AppierPredictHandler.getKeywordTargeting(MOPUB_AD_UNIT_ID));

        // Prepare to load!
        mMoPubInterstitial.setLocalExtras(localExtras);
        mMoPubInterstitial.setInterstitialAdListener(this);

        Appier.log("[Sample App]", "====== make request ======");
    }

    protected void loadInterstitial() {
        // Load interstitial
        mMoPubInterstitial.load();
    }

    protected void showInterstitial() {
        // Pop up full-screen activity to show interstitial
        mMoPubInterstitial.show();
    }

    /*
     * Override MoPubInterstitial.InterstitialAdListener functions to handle event callbacks
     */

    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        Appier.log("[Sample App]", "Interstitial loaded");
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
        mDemoFlowController.notifyAdBid();
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
        Appier.log("[Sample App]", "Interstitial load failed");
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
        mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {
        Appier.log("[Sample App]", "Interstitial shown");
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {
        Appier.log("[Sample App]", "Interstitial clicked");
    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {
        Appier.log("[Sample App]", "Interstitial dismissed");
        this.setCurrentState(this.STATE_UNLOADED);
        this.updateLayoutByState(this.getCurrentState());

        // Destroy Interstitial properly to prevent from memory leak
        this.mMoPubInterstitial.destroy();
    }
}
