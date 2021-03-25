package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.view.View;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdUnitIdentifier;
import com.appier.ads.AppierError;
import com.appier.ads.AppierInterstitialAd;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseInterstitialFragment;
import com.appier.android.sample.helper.AppierAdHelper;

public class InterstitialFragment extends BaseInterstitialFragment implements AppierInterstitialAd.EventListener {

    private AppierInterstitialAd mAppierInterstitialAd;

    public InterstitialFragment() {}

    @Override
    protected void onViewVisible(View view) {}

    /*
     * The functions to handle Appier Interstitial lifecycle
     */

    protected void createInterstitial(Context context) {
        if (mAppierInterstitialAd != null) {
            mAppierInterstitialAd.destroy();
        }

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();
        final String APPIER_AD_UNIT_ID = getString(R.string.ad_unit_320x480);
        final String APPIER_ZONE_ID = getString(R.string.zone_interstitial);

        /*
         * Create AppierInterstitialAd, waiting for load and show
         */
        mAppierInterstitialAd = new AppierInterstitialAd(context, new AppierAdUnitIdentifier(APPIER_AD_UNIT_ID), this);
        mAppierInterstitialAd.setAdDimension(320, 480);

        // Set targeting should be done before loadAd()
        AppierAdHelper.setTargeting(mAppierInterstitialAd);

        mAppierInterstitialAd.setZoneId(APPIER_ZONE_ID);
    }

    protected void loadInterstitial() {
        // Request for AD content
        mAppierInterstitialAd.loadAd();
    }

    protected void showInterstitial() {
        // Check the ad is loaded
        if (mAppierInterstitialAd.isLoaded()) {
            // Show the full screen interstitial activity
            mAppierInterstitialAd.showAd();
        }
    }

    /*
     * Override AppierInterstitialAd.EventListener functions for event callbacks
     */

    @Override
    public void onAdLoaded(AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial loaded");
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
        mDemoFlowController.notifyAdBid();
    }

    @Override
    public void onAdNoBid(AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial ad returns no bid");
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
        mDemoFlowController.notifyAdNoBid();
    }

    @Override
    public void onAdLoadFail(AppierError appierError, AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial load failed");
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
        mDemoFlowController.notifyAdError(appierError);
    }

    @Override
    public void onViewClick(AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial is clicked");
    }

    @Override
    public void onShown(AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial shown");
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
    }

    @Override
    public void onShowFail(AppierError appierError, AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial show failed with error: " + appierError);
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
        mDemoFlowController.notifyAdError(appierError);
    }

    @Override
    public void onDismiss(AppierInterstitialAd appierInterstitialAd) {
        Appier.log("[Sample App]", "Interstitial dismissed");
        this.setCurrentState(this.STATE_UNLOADED);
        this.updateLayoutByState(this.getCurrentState());

        // Destroy Interstitial properly to prevent from memory leak
        this.mAppierInterstitialAd.destroy();
    }

}
