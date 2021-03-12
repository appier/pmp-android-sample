package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.view.View;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdUnitIdentifier;
import com.appier.ads.AppierError;
import com.appier.ads.VastVideoAd;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseVideoFragment;
import com.appier.android.sample.helper.AppierAdHelper;

public class VideoFragment extends BaseVideoFragment implements VastVideoAd.EventListener {

    private VastVideoAd vastVideoAd;

    public VideoFragment() {}

    @Override
    protected void onViewVisible(View view) {}

    /*
     * The functions to handle Appier Interstitial lifecycle
     */
    @Override
    protected void loadVideo(Context context) {
        if (vastVideoAd != null) {
            vastVideoAd.destroy();
        }

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();
        final String APPIER_AD_UNIT_ID = getString(R.string.ad_unit_video);
        final String APPIER_ZONE_ID = getString(R.string.zone_video);

        /*
         * Create VastVideoAd, waiting for load and show
         */
        vastVideoAd = new VastVideoAd(context, new AppierAdUnitIdentifier(APPIER_AD_UNIT_ID), this);

        // Set targeting should be done before loadAd()
        AppierAdHelper.setTargeting(vastVideoAd);

        vastVideoAd.setZoneId(APPIER_ZONE_ID);
        vastVideoAd.loadAd();
    }

    @Override
    protected void showVideo() {
        vastVideoAd.showAd();
    }

    /*
     * Override VastVideoAd.EventListener functions for event callbacks
     */

    @Override
    public void onAdLoaded(VastVideoAd vastVideoAd) {
        Appier.log("[Sample App]", "Video loaded");
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
        mDemoFlowController.notifyAdBid();
    }

    @Override
    public void onAdNoBid(VastVideoAd vastVideoAd) {
        Appier.log("[Sample App]", "Video ad returns no bid");
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
        mDemoFlowController.notifyAdNoBid();
    }

    @Override
    public void onAdLoadFail(AppierError appierError, VastVideoAd vastVideoAd) {
        Appier.log("[Sample App]", "Video load failed");
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
        mDemoFlowController.notifyAdError(appierError);
    }

    @Override
    public void onViewClick(VastVideoAd vastVideoAd) {
        Appier.log("[Sample App]", "Video is clicked");
    }

    @Override
    public void onViewClickFail(AppierError appierError, VastVideoAd vastVideoAd) {
        Appier.log("[Sample App]", "Video click fail");
    }

    @Override
    public void onShown(VastVideoAd vastVideoAd) {
        Appier.log("[Sample App]", "Video shown");
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
    }

    @Override
    public void onAdVideoComplete(VastVideoAd vastVideoAd) {
        Appier.log("[Sample App]", "Video complete");
    }

    @Override
    public void onShowFail(AppierError appierError, VastVideoAd vastVideoAd) {
        Appier.log("[Sample App]", "Video show failed with error: " + appierError);
        this.setCurrentState(this.getNextLoadingState(this.getCurrentState()));
        this.updateLayoutByState(this.getCurrentState());
        mDemoFlowController.notifyAdError(appierError);
    }

    @Override
    public void onDismiss(VastVideoAd vastVideoAd) {
        Appier.log("[Sample App]", "Video dismissed");
        this.setCurrentState(this.STATE_UNLOADED);
        this.updateLayoutByState(this.getCurrentState());

        // Destroy Video properly to prevent from memory leak
        vastVideoAd.destroy();
    }
}
