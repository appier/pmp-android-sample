package com.appier.android.sample.fragment.mediation.mopub;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appier.ads.common.Dimension;
import com.appier.android.sample.R;
import com.appier.android.sample.common.MyRecyclerViewAdapter;
import com.appier.android.sample.common.MyRecyclerViewItemDecoration;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.mobileads.AppierPredictHandler;
import com.mopub.nativeads.AppierNativeAdRenderer;
import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;

import java.util.Arrays;

public class MoPubNativeRecyclerViewFragment extends BaseFragment {

    private Context mContext;
    private RecyclerView mRecyclerView;

    public MoPubNativeRecyclerViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * MoPubRecyclerAdapter doesn't support lifecycle control,
         * so enableErrorHandling() only invokes initial render and doesn't handle any error for this sample.
         */
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_recycler_view, container, false);
        mRecyclerView = view.findViewById(R.id.recycler);

        // add space between items
        mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(Dimension.dipsToIntPixels(12, getContext())));

        return view;
    }

    @Override
    protected void onViewVisible(View view) {
        mContext = getActivity();
        String[] items = new String[] {"", "", "", "", "", "", "", "", "", ""};

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MyRecyclerViewAdapter myRecyclerViewAdapter = new MyRecyclerViewAdapter(mContext, Arrays.asList(items));

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        insertMoPubNativeRecyclerView(
                myRecyclerViewAdapter, mRecyclerView, getString(R.string.mopub_adunit_predict_native)
        );

    }

    /*
     * Create Native Ad and insert into specific position when the ad is loaded
     */
    private void insertMoPubNativeRecyclerView(MyRecyclerViewAdapter adapter, RecyclerView recyclerView, String adUnitId) {

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

        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.template_native_ad_compact_2)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        AppierNativeAdRenderer appierNativeAdRenderer = new AppierNativeAdRenderer(viewBinder);
        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        MoPubRecyclerAdapter moPubAdAdapter = new MoPubRecyclerAdapter((Activity) mContext, adapter);

        // When using multiple line items with different networks, we need to register all AdRenderers
        moPubAdAdapter.registerAdRenderer(appierNativeAdRenderer);
        moPubAdAdapter.registerAdRenderer(moPubStaticNativeAdRenderer);

        recyclerView.setAdapter(moPubAdAdapter);

        RequestParameters parameters = new RequestParameters.Builder()
                .keywords(AppierPredictHandler.getKeywordTargeting(adUnitId))
                .build();

        moPubAdAdapter.loadAds(adUnitId, parameters);
    }

}
