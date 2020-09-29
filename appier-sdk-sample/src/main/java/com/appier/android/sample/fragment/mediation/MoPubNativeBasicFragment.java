package com.appier.android.sample.fragment.mediation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
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

public class MoPubNativeBasicFragment extends BaseFragment implements MoPubNative.MoPubNativeNetworkListener {

    private Context mContext;
    private LinearLayout mAdContainer;

    public static MoPubNativeBasicFragment newInstance(Context context) {
        return new MoPubNativeBasicFragment(context);
    }

    public MoPubNativeBasicFragment() {}

    private MoPubNativeBasicFragment(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sdk_native_basic, container, false);
    }

    @Override
    protected void onViewVisible(View view) {

        mAdContainer = view.findViewById(R.id.ad_container);
        mAdContainer.removeAllViews();

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

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

        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.template_native_ad_full_1)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        AppierNativeAdRenderer appierNativeAdRenderer = new AppierNativeAdRenderer(viewBinder);
        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        MoPubNative mMoPubNativeAd = new MoPubNative(
                mContext, getString(R.string.mopub_adunit_predict_native), this
        );

        // Required for Appier MoPub Mediation
        mMoPubNativeAd.registerAdRenderer(appierNativeAdRenderer);
        // Optional, if the AdUnit contains MoPub Native line item
        mMoPubNativeAd.registerAdRenderer(moPubStaticNativeAdRenderer);
        mMoPubNativeAd.makeRequest();
        mMoPubNativeAd.makeRequest(AppierPredictHandler.setKeywordTargeting(getString(R.string.mopub_zone_predict_native)).build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
