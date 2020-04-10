package com.appier.android.sample.fragment.mediation;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseInterstitialFragment;
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

        final int AD_WIDTH = 320;
        final int AD_HEIGHT = 480;

        Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(AppierDataKeys.AD_WIDTH_LOCAL, AD_WIDTH);
        localExtras.put(AppierDataKeys.AD_HEIGHT_LOCAL, AD_HEIGHT);

        mMoPubInterstitial = new MoPubInterstitial((Activity) context, getString(R.string.mopub_adunit_interstitial));
        mMoPubInterstitial.setLocalExtras(localExtras);
        mMoPubInterstitial.setInterstitialAdListener(this);
        Appier.log("[Sample App]", "====== make request ======");
    }

    protected void loadInterstitial() {
        mMoPubInterstitial.load();
    }

    protected void showInterstitial() {
        mMoPubInterstitial.show();
    }

    /*
     * Override MoPubInterstitial.InterstitialAdListener functions for events callback
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
    }
}