package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.view.View;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.AppierInterstitialAd;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseInterstitialFragment;

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
         * (Required) Appier Interstitial Ad integration
         */
        mAppierInterstitialAd = new AppierInterstitialAd(context, this);
        mAppierInterstitialAd.setAdDimension(320, 480);
        mAppierInterstitialAd.setZoneId(getResources().getString(R.string.zone_interstitial));
    }

    protected void loadInterstitial() {
        mAppierInterstitialAd.loadAd();
    }

    protected void showInterstitial() {
        mAppierInterstitialAd.showAd();
    }

    /*
     * Override AppierInterstitialAd.EventListener functions for events callback
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
    }

}
