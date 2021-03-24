package com.appier.android.sample.fragment.mediation.mopub;

import android.content.Context;
import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.common.AppierDataKeys;
import com.appier.ads.common.Dimension;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.mobileads.AppierPredictHandler;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.HashMap;
import java.util.Map;


public class MoPubBannerFloatingWindowFragment extends BaseFloatingWindowFragment implements MoPubView.BannerAdListener {

    private LinearLayout adContainer;
    private MoPubView moPubView;

    public MoPubBannerFloatingWindowFragment() {}

    protected void loadAdInContainer(LinearLayout adContainer) {

        Context context = getActivity();
        this.adContainer = adContainer;

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

        moPubView = new MoPubView(getActivity());

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
        moPubView.setKeywords(AppierPredictHandler.getKeywordTargeting(MOPUB_AD_UNIT_ID));

        // set layout parameter to remove the white margin
        moPubView.setLayoutParams(new LinearLayout.LayoutParams(
                Dimension.dipsToIntPixels(300, context), Dimension.dipsToIntPixels(250, context))
        );

        // Load Ad!
        moPubView.setLocalExtras(localExtras);
        moPubView.setBannerAdListener(this);
        moPubView.setAdUnitId(MOPUB_AD_UNIT_ID);
        moPubView.loadAd();
    }

    @Override
    protected void destroyAdView() {
        if (moPubView != null) {
            moPubView.destroy();
        }
    }

    /*
     * Override MoPubView.BannerAdListener functions to handle event callbacks
     */

    @Override
    public void onBannerLoaded(MoPubView banner) {
        Appier.log("[Sample App]", "onBannerLoaded()");
        if (adContainer != null) {
            mDemoFlowController.notifyAdBid();

            // Display banner in parent container
            adContainer.removeAllViews();
            adContainer.addView(banner);

        }
    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        Appier.log("[Sample App]", "onBannerFailed():", errorCode.toString());
        mDemoFlowController.notifyAdNoBid();
        if (mFloatViewManager != null) {
            mFloatViewManager.close();
        }
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
