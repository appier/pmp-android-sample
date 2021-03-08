package com.appier.android.sample.fragment.mediation.mopub;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseVideoFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.mobileads.AppierPredictHandler;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubRewardedVideoManager;
import com.mopub.mobileads.MoPubRewardedVideos;

import java.util.HashMap;
import java.util.Map;

public class MoPubVideoFragment extends BaseVideoFragment implements MoPubInterstitial.InterstitialAdListener {

    private MoPubInterstitial moPubInterstitial;

    public MoPubVideoFragment() {}

    @Override
    protected void onViewVisible(View view) {}

    @Override
    protected void createVideo(Context context) {

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();
        final String MOPUB_AD_UNIT_ID = getString(R.string.mopub_adunit_predict_video);
        final int AD_WIDTH = 320;
        final int AD_HEIGHT = 480;

        Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(AppierDataKeys.AD_UNIT_ID_LOCAL, MOPUB_AD_UNIT_ID);
        localExtras.put(AppierDataKeys.AD_WIDTH_LOCAL, AD_WIDTH);
        localExtras.put(AppierDataKeys.AD_HEIGHT_LOCAL, AD_HEIGHT);

        moPubInterstitial = new MoPubInterstitial((Activity) context, MOPUB_AD_UNIT_ID);
        moPubInterstitial.setKeywords(AppierPredictHandler.getKeywordTargeting(MOPUB_AD_UNIT_ID));

        moPubInterstitial.setLocalExtras(localExtras);
        moPubInterstitial.setInterstitialAdListener(this);
        Appier.log("[Sample App]", "====== make request ======");
    }

    @Override
    protected void loadVideo() {
        moPubInterstitial.load();
    }

    @Override
    protected void showVideo() {
        moPubInterstitial.show();
    }

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
        this.moPubInterstitial.destroy();
    }
}
