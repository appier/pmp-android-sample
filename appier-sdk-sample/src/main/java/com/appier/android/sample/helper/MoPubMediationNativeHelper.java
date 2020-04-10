package com.appier.android.sample.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.android.sample.R;
import com.appier.android.sample.common.DemoFlowController;
import com.appier.android.sample.common.MyRecyclerViewAdapter;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.AppierNativeAdRenderer;
import com.mopub.nativeads.MoPubAdAdapter;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;

public class MoPubMediationNativeHelper {

    public static MoPubNative createMoPubNative(final Context context, final DemoFlowController demoFlowController, final LinearLayout parentLayout, String adunitId, int layoutId) {

        /*
         * (Required) MoPub NativeAd mediation integration
         */
        final NativeAd.MoPubNativeEventListener moPubNativeEventListener = new NativeAd.MoPubNativeEventListener() {
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
        };

        MoPubNative.MoPubNativeNetworkListener moPubNativeNetworkListener = new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(final NativeAd nativeAd) {
                Appier.log("[Sample App]", "Native ad has loaded.");

                final AdapterHelper adapterHelper = new AdapterHelper(context, 0, 3); // When standalone, any range will be fine.

                // Retrieve the pre-built ad view that AdapterHelper prepared for us.
                View adView = adapterHelper.getAdView(null, null, nativeAd, new ViewBinder.Builder(0).build());

                // Set the native event listeners (onImpression, and onClick).
                nativeAd.setMoPubNativeEventListener(moPubNativeEventListener);

                // Add the ad view to our view hierarchy
                parentLayout.addView(adView);

                demoFlowController.notifyAdBid();
            }

            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                Appier.log("[Sample App]", "Native ad failed to load with error:", errorCode.toString());
                demoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
            }
        };

        ViewBinder viewBinder = new ViewBinder.Builder(layoutId)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        // Note: Appier zone_id is integrated through server extra on MoPub console
        AppierNativeAdRenderer appierNativeAdRenderer = new AppierNativeAdRenderer(viewBinder);
        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        MoPubNative moPubNative = new MoPubNative(context, adunitId, moPubNativeNetworkListener);

        moPubNative.registerAdRenderer(appierNativeAdRenderer);
        moPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);

        return moPubNative;

        // Note: use moPubNative.makeRequest() to load the Ad
    }

    /*
     * A helper to create Native Ad and insert into specific position when the ad is loaded
     */
    public static void insertMoPubNativeListView(Context context, ArrayAdapter<String> adapter, ListView listView, String adunitId, int layoutId) {

        ViewBinder viewBinder = new ViewBinder.Builder(layoutId)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        AppierNativeAdRenderer appierNativeAdRenderer = new AppierNativeAdRenderer(viewBinder);
        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        MoPubAdAdapter moPubAdAdapter = new MoPubAdAdapter((Activity) context, adapter);
        moPubAdAdapter.registerAdRenderer(appierNativeAdRenderer);
        moPubAdAdapter.registerAdRenderer(moPubStaticNativeAdRenderer);

        listView.setAdapter(moPubAdAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Appier.log("[Sample App]", "List item clicked");
            }
        });

        moPubAdAdapter.loadAds(adunitId);
    }

    /*
     * A helper to create Native Ad and insert into specific position when the ad is loaded
     */
    public static void insertMoPubNativeRecyclerView(Context context, MyRecyclerViewAdapter adapter, RecyclerView recyclerView, String adunitId, int layoutId) {

        ViewBinder viewBinder = new ViewBinder.Builder(layoutId)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        AppierNativeAdRenderer appierNativeAdRenderer = new AppierNativeAdRenderer(viewBinder);
        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        MoPubRecyclerAdapter moPubAdAdapter = new MoPubRecyclerAdapter((Activity)context, adapter);
        moPubAdAdapter.registerAdRenderer(appierNativeAdRenderer);
        moPubAdAdapter.registerAdRenderer(moPubStaticNativeAdRenderer);

        recyclerView.setAdapter(moPubAdAdapter);

        moPubAdAdapter.loadAds(adunitId);
    }
}
