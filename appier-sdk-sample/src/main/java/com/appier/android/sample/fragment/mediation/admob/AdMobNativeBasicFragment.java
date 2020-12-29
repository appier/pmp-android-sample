package com.appier.android.sample.fragment.mediation.admob;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.appier.mediation.admob.AppierAdapterConfiguration;
import com.appier.mediation.admob.ads.AppierNative;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

public class AdMobNativeBasicFragment extends BaseFragment {

    private Context mContext;
    private LinearLayout mAdContainer;
    private UnifiedNativeAdView mNativeAdView;

    public static AdMobNativeBasicFragment newInstance(Context context) {
        return new AdMobNativeBasicFragment(context);
    }

    public AdMobNativeBasicFragment() {}

    private AdMobNativeBasicFragment(Context context) { mContext = context; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    protected View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sdk_native_basic, container, false);
    }

    @Override
    protected void onViewVisible(final View view) {

        mAdContainer = view.findViewById(R.id.ad_container);
        mAdContainer.removeAllViews();

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        final String ADMOB_AD_UNIT_ID = getString(R.string.admob_adunit_native);

        /*
         * Initialize AdMob native
         *
         * To enable Appier AdMob Mediation, the AdUnit requires at least one "Custom Event",
         * with the following settings:
         *
         *   "Class Name": "com.appier.mediation.admob.ads.AppierNative".
         *   "Parameter":  { "zoneId": "<THE ZONE ID PROVIDED BY APPIER>" }
         *
         */

        Bundle localExtras = new Bundle();
        localExtras.putString(AppierDataKeys.AD_UNIT_ID_LOCAL, ADMOB_AD_UNIT_ID);

        // Inflate the layout
        mNativeAdView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.template_admob_native_ad_full_1, null);

        // Load ad
        AdLoader adLoader = new AdLoader.Builder(mContext, ADMOB_AD_UNIT_ID)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        Appier.log("[Sample App]", "onUnifiedNativeAdLoaded()");

                        populateUnifiedNativeAdView(unifiedNativeAd, mNativeAdView);
                        mAdContainer.addView(mNativeAdView);
                    }
                })
                .withAdListener(new AdMobNativeAdListener())
                .build();
        adLoader.loadAd(new AdRequest.Builder()
                .addCustomEventExtrasBundle(AppierNative.class, localExtras)
                .build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNativeAdView != null) {
            mNativeAdView.destroy();
            mNativeAdView = null;
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        adView.setVisibility(View.VISIBLE);

        // Check whether the native ad is provided by Appier
        if (nativeAd.getAdvertiser().equals(AppierAdapterConfiguration.getAdvertiserName())) {
            adView.setAdvertiserView(adView.findViewById(R.id.native_privacy_information_icon_image));
            adView.setHeadlineView(adView.findViewById(R.id.native_title));
            adView.setBodyView(adView.findViewById(R.id.native_text));
            adView.setCallToActionView(adView.findViewById(R.id.native_cta));
            adView.setImageView(adView.findViewById(R.id.native_main_image));
            adView.setIconView(adView.findViewById(R.id.native_icon_image));

            /*
             * We provide two way (text and image) to show appier advertiser info.
             * You can choose one to bind the AdvertiserView.
             *
             * ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
             */
            ((ImageView) adView.getAdvertiserView()).setImageDrawable(
                    // The image at index 0 would always Appier privacy info icon
                    nativeAd.getImages().get(0).getDrawable()
            );

            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            ((ImageView) adView.getImageView()).setImageDrawable(
                    // The image at index 1 would be the main image
                    nativeAd.getImages().get(1).getDrawable()
            );
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.setNativeAd(nativeAd);
        }
    }

    /*
     * Customize AdListener functions to handle event callbacks
     */

    public class AdMobNativeAdListener extends AdListener {
        @Override
        public void onAdLoaded() {
            Appier.log("[Sample App]", "onAdLoaded():");
            mDemoFlowController.notifyAdBid();
        }

        @Override
        public void onAdFailedToLoad(LoadAdError loadAdError) {
            Appier.log("[Sample App]", "onAdFailedToLoad():", loadAdError.getCode(), loadAdError.getMessage());
            mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
        }

        @Override
        public void onAdClicked() {
            Appier.log("[Sample App]", "onAdClicked():");
        }
    }
}
