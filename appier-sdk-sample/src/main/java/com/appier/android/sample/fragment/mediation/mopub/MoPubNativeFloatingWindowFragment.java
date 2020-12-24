package com.appier.android.sample.fragment.mediation.mopub;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.mobileads.AppierPredictHandler;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.AppierNativeAdRenderer;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;

import java.util.HashMap;
import java.util.Map;


public class MoPubNativeFloatingWindowFragment extends BaseFloatingWindowFragment implements MoPubNative.MoPubNativeNetworkListener {

    private Context mContext;
    private LinearLayout mAdContainer;
    private MoPubNative mMoPubNativeAd;

    public MoPubNativeFloatingWindowFragment() {}

    protected void loadAdInContainer(LinearLayout adContainer) {

        mContext = getActivity();
        mAdContainer = adContainer;

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        final String MOPUB_AD_UNIT_ID = getString(R.string.mopub_adunit_predict_native);

        /*
         * Initialize MoPub ViewBinder and MoPubNative Ads
         *
         * To enable Appier MoPub Mediation, the AdUnit requires at least one "Network line item",
         * with the following settings:
         *
         *   "Custom event class": "com.mopub.nativeads.AppierNative".
         *   "Custom event data":  { "zoneId": "<THE ZONE ID PROVIDED BY APPIER>" }
         *
         */

        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.template_native_ad_full_2)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        AppierNativeAdRenderer appierNativeAdRenderer = new AppierNativeAdRenderer(viewBinder);
        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        mMoPubNativeAd = new MoPubNative(
                mContext, MOPUB_AD_UNIT_ID, this
        );

        /*
         *  Optional: Required when integrating with Appier predict
         *
         *  To achieve the best performance, please set "Keyword targeting" for each line item in MoPub
         *  console with the following values:
         *  Keyword targeting:
         *      appier_zone_<THE ZONE ID PROVIDED BY APPIER>:1
         *      appier_predict_ver:1
         */
        Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(AppierDataKeys.AD_UNIT_ID_LOCAL, MOPUB_AD_UNIT_ID);
        mMoPubNativeAd.setLocalExtras(localExtras);

        RequestParameters parameters = new RequestParameters.Builder()
                .keywords(AppierPredictHandler.getKeywordTargeting(MOPUB_AD_UNIT_ID))
                .build();

        // Required for Appier MoPub Mediation, alone with other mediation line items
        mMoPubNativeAd.registerAdRenderer(appierNativeAdRenderer);
        mMoPubNativeAd.registerAdRenderer(moPubStaticNativeAdRenderer);

        // Load Ads with RequestParameters for Appier Predict.
        // use `mMoPubNativeAd.makeRequest();` if you are doing the basic integration (without Appier predict).
        mMoPubNativeAd.makeRequest(parameters);
    }

    protected void destroyAdView() {
        if (mMoPubNativeAd != null) {
            mMoPubNativeAd.destroy();
        }
    }

    /*
     * Override MoPubNative.MoPubNativeNetworkListener functions to handle event callbacks from MoPub
     */

    @Override
    public void onNativeLoad(final NativeAd nativeAd) {
        Appier.log("[Sample App]", "Native ad has loaded.");

        final AdapterHelper adapterHelper = new AdapterHelper(mContext, 0, 3); // When standalone, any range will be fine.

        // Retrieve the pre-built ad view that AdapterHelper prepared for us.
        View adView = adapterHelper.getAdView(null, null, nativeAd, new ViewBinder.Builder(0).build());

        // Set the event listeners for NativeAd (onImpression, and onClick).
        nativeAd.setMoPubNativeEventListener(new NativeAd.MoPubNativeEventListener() {
            @Override
            public void onImpression(View view) {
                Appier.log("[Sample App]", "Native ad recorded an impression.");
                // Impress is recorded - do what is needed AFTER the ad is visibly shown here.
            }

            @Override
            public void onClick(View view) {
                Appier.log("[Sample App]", "Native ad recorded a click.");
                // Click tracking.
            }
        });

        // Add the ad view to our view hierarchy
        mAdContainer.addView(adView);

        mDemoFlowController.notifyAdBid();
    }

    @Override
    public void onNativeFail(NativeErrorCode errorCode) {
        Appier.log("[Sample App]", "Native ad failed to load with error:", errorCode.toString());
        mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
    }

}
