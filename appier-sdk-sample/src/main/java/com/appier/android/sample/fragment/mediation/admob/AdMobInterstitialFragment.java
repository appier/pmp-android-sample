package com.appier.android.sample.fragment.mediation.admob;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseInterstitialFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.appier.mediation.admob.ads.AppierInterstitial;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdMobInterstitialFragment extends BaseInterstitialFragment {

    private InterstitialAd mInterstitialAd;
    private Bundle mLocalExtras;

    public AdMobInterstitialFragment() {}

    @Override
    protected void onViewVisible(View view) {}

    /*
     * The function handle AdMob Interstitial life cycle
     */
    protected void createInterstitial(Context context) {

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        final String ADMOB_AD_UNIT_ID = getString(R.string.admob_adunit_interstitial);
        final int AD_WIDTH = 320;
        final int AD_HEIGHT = 480;

        /*
         * Initialize AdMob Interstitial
         *
         * To enable Appier AdMob Mediation, the AdUnit requires at least one "Custom Event",
         * with the following settings:
         *
         *   "Class Name": "com.appier.mediation.admob.ads.AppierInterstitial".
         *   "Parameter":  { "zoneId": "<THE ZONE ID PROVIDED BY APPIER>" }
         *
         */

        mLocalExtras = new Bundle();
        mLocalExtras.putInt(AppierDataKeys.AD_WIDTH_LOCAL, AD_WIDTH);
        mLocalExtras.putInt(AppierDataKeys.AD_HEIGHT_LOCAL, AD_HEIGHT);
        mLocalExtras.putString(AppierDataKeys.AD_UNIT_ID_LOCAL, ADMOB_AD_UNIT_ID);

        mInterstitialAd = new InterstitialAd(context);

        // Prepare to load!
        mInterstitialAd.setAdUnitId(ADMOB_AD_UNIT_ID);
        mInterstitialAd.setAdListener(new AdMobInterstitialAdListener());

        Appier.log("[Sample App]", "====== make request ======");
    }

    @Override
    protected void loadInterstitial() {
        // Load interstitial
        mInterstitialAd.loadAd(new AdRequest.Builder()
                .addCustomEventExtrasBundle(AppierInterstitial.class, mLocalExtras)
                .build());
    }

    @Override
    protected void showInterstitial() {
        // Pop up full-screen activity to show interstitial
        mInterstitialAd.show();
    }

    /*
     * Customize AdListener functions to handle event callbacks
     */

    public class AdMobInterstitialAdListener extends AdListener {
        @Override
        public void onAdLoaded() {
            Appier.log("[Sample App]", "onAdLoaded()");
            setCurrentState(getNextLoadingState(getCurrentState()));
            updateLayoutByState(getCurrentState());
            mDemoFlowController.notifyAdBid();
        }

        @Override
        public void onAdFailedToLoad(int i) {
            Appier.log("[Sample App]", "onAdFailedToLoad():");
            setCurrentState(getNextLoadingState(getCurrentState()));
            updateLayoutByState(getCurrentState());
            if (i == AdRequest.ERROR_CODE_NO_FILL) {
                mDemoFlowController.notifyAdNoBid();
            } else {
                mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
            }
        }

        @Override
        public void onAdImpression() {
            Appier.log("[Sample App]", "onAdImpression()");
            setCurrentState(getNextLoadingState(getCurrentState()));
            updateLayoutByState(getCurrentState());
        }

        @Override
        public void onAdClicked() {
            Appier.log("[Sample App]", "onAdClicked()");
        }

        @Override
        public void onAdClosed() {
            Appier.log("[Sample App]", "onAdClosed()");
            setCurrentState(STATE_UNLOADED);
            updateLayoutByState(getCurrentState());
            mInterstitialAd = null;
        }
    }
}
