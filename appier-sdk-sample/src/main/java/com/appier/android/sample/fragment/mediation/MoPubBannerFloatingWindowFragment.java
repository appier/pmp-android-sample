package com.appier.android.sample.fragment.mediation;

import android.content.Context;
import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.common.AppierDataKeys;
import com.appier.ads.common.Dimension;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.HashMap;
import java.util.Map;


public class MoPubBannerFloatingWindowFragment extends BaseFloatingWindowFragment implements MoPubView.BannerAdListener {

    private LinearLayout mAdContainer;
    private MoPubView mMoPubView;

    public MoPubBannerFloatingWindowFragment() {}

    protected void loadAdInContainer(LinearLayout adContainer) {

        Context context = getActivity();
        mAdContainer = adContainer;

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

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
        localExtras.put(AppierDataKeys.AD_WIDTH_LOCAL, 300);
        localExtras.put(AppierDataKeys.AD_HEIGHT_LOCAL, 250);

        mMoPubView = new MoPubView(getActivity());

        mMoPubView.setLocalExtras(localExtras);
        mMoPubView.setBannerAdListener(this);
        mMoPubView.setAdUnitId(getResources().getString(R.string.mopub_adunit_banner_300x250));

        // set layout parameter to remove the white margin
        mMoPubView.setLayoutParams(new LinearLayout.LayoutParams(
                Dimension.dipsToIntPixels(300, context), Dimension.dipsToIntPixels(250, context))
        );

        mMoPubView.loadAd();
    }

    protected void destroyAdView() {
        if (mMoPubView != null) {
            mMoPubView.destroy();
        }
    }

    /*
     * Override MoPubView.BannerAdListener functions to handle event callbacks
     */

    @Override
    public void onBannerLoaded(MoPubView banner) {
        Appier.log("[Sample App]", "onBannerLoaded()");
        if (mAdContainer != null) {
            mDemoFlowController.notifyAdBid();

            // Display banner in parent container
            mAdContainer.removeAllViews();
            mAdContainer.addView(banner);

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
