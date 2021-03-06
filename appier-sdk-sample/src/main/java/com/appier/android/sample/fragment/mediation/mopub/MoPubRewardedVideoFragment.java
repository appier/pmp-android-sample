package com.appier.android.sample.fragment.mediation.mopub;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

import androidx.annotation.NonNull;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseVideoFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.common.MediationSettings;
import com.mopub.common.MoPub;
import com.mopub.common.MoPubReward;
import com.mopub.common.SdkConfiguration;
import com.mopub.mobileads.AppierMediationSettings;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MoPubRewardedVideoFragment extends BaseVideoFragment implements MoPubRewardedVideoListener {
    private String MOPUB_AD_UNIT_ID;

    @Override
    protected void onViewVisible(View view) {
        MOPUB_AD_UNIT_ID = getString(R.string.mopub_adunit_predict_rewarded_video);

        // Initialize MoPub SDK before load rewarded ad in activity, or the ad will load failed.
        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(MOPUB_AD_UNIT_ID).build();
        MoPub.initializeSdk(getContext(), sdkConfiguration, null);
    }


    /*
     * The function handle MoPub Rewarded Video life cycle
     */
    @Override
    protected void loadVideo(Context context) {

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        /*
         * Initialize MoPub Rewarded Video
         *
         * To enable Appier MoPub Mediation, the AdUnit requires at least one "Network line item",
         * with the following settings:
         *
         *   "Custom event class": "com.mopub.mobileads.AppierRewardedVideo".
         *   "Custom event data": { "zoneId": "<THE ZONE ID PROVIDED BY APPIER>" }
         *
         */

        Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(AppierDataKeys.AD_UNIT_ID_LOCAL, MOPUB_AD_UNIT_ID);

        // Set Appier rewarded video ad orientation through localExtras
        localExtras.put(AppierDataKeys.AD_ORIENTATION_LOCAL, Configuration.ORIENTATION_LANDSCAPE);

        // Prepare to load!
        MediationSettings mediationSettings = new AppierMediationSettings().withLocalExtras(localExtras);
        MoPubRewardedVideos.setRewardedVideoListener(this);

        Appier.log("[Sample App]", "====== make request ======");

        // Load rewarded video
        MoPubRewardedVideos.loadRewardedVideo(MOPUB_AD_UNIT_ID, mediationSettings);
    }

    @Override
    protected void showVideo() {
        if (MoPubRewardedVideos.hasRewardedVideo(MOPUB_AD_UNIT_ID)) {
            // Pop up full-screen activity to show rewarded video
            MoPubRewardedVideos.showRewardedVideo(MOPUB_AD_UNIT_ID);
        }
    }

    /*
     * Override MoPubRewardedVideoListener functions to handle event callbacks
     */
    @Override
    public void onRewardedVideoLoadSuccess(@NonNull String adUnitId) {
        Appier.log("[Sample App]", "Rewarded video loaded");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdBid();
    }

    @Override
    public void onRewardedVideoLoadFailure(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {
        Appier.log("[Sample App]", "Rewarded video load failed");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
        mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
    }

    @Override
    public void onRewardedVideoStarted(@NonNull String adUnitId) {
        Appier.log("[Sample App]", "Rewarded video started");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());

    }

    @Override
    public void onRewardedVideoPlaybackError(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {
        Appier.log("[Sample App]", "Rewarded video playback error");
        setCurrentState(getNextLoadingState(getCurrentState()));
        updateLayoutByState(getCurrentState());
    }

    @Override
    public void onRewardedVideoClicked(@NonNull String adUnitId) {
        Appier.log("[Sample App]", "Rewarded video clicked");
    }

    @Override
    public void onRewardedVideoClosed(@NonNull String adUnitId) {
        Appier.log("[Sample App]", "Rewarded video closed");
        setCurrentState(STATE_UNLOADED);
        updateLayoutByState(getCurrentState());
    }

    @Override
    public void onRewardedVideoCompleted(@NonNull Set<String> adUnitIds, @NonNull MoPubReward reward) {
        Appier.log("[Sample App]", "Rewarded video completed:", reward.getLabel(), reward.getAmount());
        // Please implement reward behavior in this callback function.
    }
}
