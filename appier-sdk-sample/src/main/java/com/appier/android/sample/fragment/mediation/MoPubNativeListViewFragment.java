package com.appier.android.sample.fragment.mediation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appier.ads.Appier;
import com.appier.android.sample.R;
import com.appier.android.sample.common.MyListViewAdapter;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.nativeads.AppierNativeAdRenderer;
import com.mopub.nativeads.MoPubAdAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.ViewBinder;

import java.util.Arrays;

public class MoPubNativeListViewFragment extends BaseFragment {

    private Context mContext;
    private ListView mListView;

    public MoPubNativeListViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * MoPubAdAdapter doesn't support lifecycle control,
         * so enableErrorHandling() only invokes initial render and doesn't handle any error for this sample.
         */
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_list_view, container, false);
        mListView = view.findViewById(R.id.list);
        return view;
    }

    @Override
    protected void onViewVisible(View view) {
        mContext = getActivity();
        String[] items = new String[] {"", "", "", "", "", "", "", "", "", ""};

        MyListViewAdapter myListViewAdapter = new MyListViewAdapter(mContext, Arrays.asList(items));

        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        insertMoPubNativeListView(myListViewAdapter, mListView, getResources().getString(R.string.mopub_adunit_native));

    }

    /*
     * Create Native Ad and insert into specific position when the ad is loaded
     */
    private void insertMoPubNativeListView(ArrayAdapter<String> adapter, ListView listView, String adunitId) {

        /*
         * Initialize MoPub ViewBinder and MoPubNative Ads
         *
         * To enable Appier MoPub Mediation, the AdUnit requires at least one "Network line item",
         *   with "Custom event class" set to "com.mopub.mobileads.AppierBanner".
         *   The Appier ZoneId is configured in the "Custom event data" of the line item, with format:
         *     { "zoneId": "<THE ZONE ID PROVIDED BY APPIER>" }
         */

        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.template_native_ad_compact_1)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        AppierNativeAdRenderer appierNativeAdRenderer = new AppierNativeAdRenderer(viewBinder);
        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        MoPubAdAdapter moPubAdAdapter = new MoPubAdAdapter((Activity) mContext, adapter);
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

}
