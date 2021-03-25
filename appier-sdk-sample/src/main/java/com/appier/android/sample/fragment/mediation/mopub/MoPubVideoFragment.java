package com.appier.android.sample.fragment.mediation.mopub;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseVideoFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.util.HashMap;
import java.util.Map;

public class MoPubVideoFragment extends BaseVideoFragment implements MoPubInterstitial.InterstitialAdListener {

    private MoPubInterstitial moPubInterstitial;

    public MoPubVideoFragment() {}

    @Override
    protected void onViewVisible(View view) {}

    /*
     * The function handle MoPub Interstitial life cycle
     */
    @Override
    protected void loadVideo(Context context) {

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        final String MOPUB_AD_UNIT_ID = getString(R.string.mopub_adunit_predict_video);

        /*
         * Initialize MoPub Interstitial
         *
         * To enable Appier MoPub Mediation, the AdUnit requires at least one "Network line item",
         * with the following settings:
         *
         *   "Custom event class": "com.mopub.mobileads.AppierVideo".
         *   "Custom event data": { "zoneId": "<THE ZONE ID PROVIDED BY APPIER>" }
         *
         */

        Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(AppierDataKeys.AD_UNIT_ID_LOCAL, MOPUB_AD_UNIT_ID);

        // Set Appier video ad orientation through localExtras
        localExtras.put(AppierDataKeys.AD_ORIENTATION_LOCAL, Configuration.ORIENTATION_LANDSCAPE);

        moPubInterstitial = new MoPubInterstitial((Activity) context, MOPUB_AD_UNIT_ID);

        // Prepare to load!
        moPubInterstitial.setLocalExtras(localExtras);
        moPubInterstitial.setInterstitialAdListener(this);

        Appier.log("[Sample App]", "====== make request ======");

        // Load video
        moPubInterstitial.load();
    }

    @Override
    protected void showVideo() {
        // Pop up full-screen activity to show video
        moPubInterstitial.show();
    }

    /*
     * Override MoPubInterstitial.InterstitialAdListener functions to handle event callbacks
     */
    @Override
    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        Appier.log("[Sample App]", "Video loaded");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdBid();
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
        Appier.log("[Sample App]", "Video load failed");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
    }

    @Override
    public void onInterstitialShown(MoPubInterstitial interstitial) {
        Appier.log("[Sample App]", "Video shown");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial interstitial) {
        Appier.log("[Sample App]", "Video clicked");
    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial interstitial) {
        Appier.log("[Sample App]", "Video dismissed");
        setCurrentState(STATE_UNLOADED);
        updateLayoutByState(getCurrentState());

        // Destroy Interstitial properly to prevent from memory leak
        moPubInterstitial.destroy();
    }
}
