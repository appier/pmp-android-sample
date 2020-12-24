package com.appier.android.sample.fragment.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.appier.ads.AppierPredictor;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.activity.mediation.mopub.MoPubBannerBasicActivity;
import com.appier.android.sample.activity.mediation.mopub.MoPubBannerFloatingWindowActivity;
import com.appier.android.sample.activity.mediation.mopub.MoPubInterstitialActivity;
import com.appier.android.sample.activity.mediation.mopub.MoPubNativeBasicActivity;
import com.appier.android.sample.activity.mediation.mopub.MoPubNativeFloatingWindowActivity;
import com.appier.android.sample.activity.mediation.mopub.MoPubNativeListActivity;
import com.appier.android.sample.common.NavigationAdapter;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.common.MoPub;
import com.mopub.mobileads.AppierAdapterConfiguration;
import com.mopub.mobileads.AppierAdUnitIdentifier;
import com.mopub.mobileads.AppierPredictHandler;


public class MoPubMediationNavigationFragment extends BaseFragment {

    public MoPubMediationNavigationFragment() {}

    @Override
    protected void onViewVisible(View view) {

    }

    public static MoPubMediationNavigationFragment newInstance(String title) {
        MoPubMediationNavigationFragment fragment = new MoPubMediationNavigationFragment();
        fragment.setTitleArgs(title);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_primary_navigation, container, false);

        // Appier Predict Button
        Button mButtonPredict = layout.findViewById(R.id.button_predict);
        mButtonPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * (Optional) Do Appier Predict
                 *
                 * It is recommended to call predictAd() at the previous activity before rendering Ads to achieve
                 * best performance.
                 */

                // Remember to call helper functions to apply GDPR and COPPA consent before Appier predict
                AppierAdHelper.setAppierGlobal();

                // Create AppierPredictor instance and apply predict to all required AdUnits
                AppierPredictor predictor = new AppierPredictor(getContext(), new AppierPredictHandler(getContext()));

                predictor.predictAd(new AppierAdUnitIdentifier(getString(R.string.mopub_adunit_predict_native)));
                predictor.predictAd(new AppierAdUnitIdentifier(getString(R.string.mopub_adunit_predict_banner_300x250)));
                predictor.predictAd(new AppierAdUnitIdentifier(getString(R.string.mopub_adunit_predict_interstitial)));
            }
        });

        initializeMoPubMediationView(layout);

        return layout;
    }

    private void initializeMoPubMediationView(View layout) {
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_interstitial),
                new Pair[] {
                        new Pair<>("Interstitial", MoPubInterstitialActivity.class)
                }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_banner),
                new Pair[] {
                        new Pair<>("Banner - basic format", MoPubBannerBasicActivity.class),
                        new Pair<>("Banner - in a floating window", MoPubBannerFloatingWindowActivity.class)
                }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_native),
                new Pair[] {
                        new Pair<>("Native - basic format", MoPubNativeBasicActivity.class),
                        new Pair<>("Native - in a listview", MoPubNativeListActivity.class),
                        new Pair<>("Native - in a floating window", MoPubNativeFloatingWindowActivity.class)
                }
        );
        TextView textVersion = layout.findViewById(R.id.text_version);
        AppierAdapterConfiguration appierAdapterConfiguration = new AppierAdapterConfiguration();
        textVersion.setText(
            "Appier SDK version : " + appierAdapterConfiguration.getNetworkSdkVersion() + "\n" +
            "MoPub Mediation SDK version : " + appierAdapterConfiguration.getAdapterVersion() + "\n" +
            "MoPub SDK version : " + MoPub.SDK_VERSION
        );
    }

    // TODO: extract me
    private void initializeNavigationList(View view, Pair<String, Class<?>>[] navigations) {
        ListView listView = (ListView) view;
        final String title = this.getTitle();
        final NavigationAdapter navigationAdapter = new NavigationAdapter(getContext());
        for (Pair<String, Class<?>> navigation: navigations) {
            navigationAdapter.add(navigation);
        }
        listView.setAdapter(navigationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
                Pair<String, Class<?>> item = navigationAdapter.getItem(position);
                Intent intent = new Intent(getContext(), item.second);
                intent.putExtra(BaseActivity.EXTRA_TITLE, item.first);
                intent.putExtra(BaseActivity.EXTRA_SUB_TITLE, title);
                startActivity(intent);
            }
        });
    }
}
