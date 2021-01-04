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
import com.appier.android.sample.activity.mediation.admob.AdMobBannerBasicActivity;
import com.appier.android.sample.activity.mediation.admob.AdMobBannerFloatingWindowActivity;
import com.appier.android.sample.activity.mediation.admob.AdMobInterstitialActivity;
import com.appier.android.sample.activity.mediation.admob.AdMobNativeBasicActivity;
import com.appier.android.sample.activity.mediation.admob.AdMobNativeFloatingWindowActivity;
import com.appier.android.sample.common.NavigationAdapter;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.appier.mediation.admob.AppierAdapterConfiguration;
import com.google.android.gms.ads.MobileAds;
import com.appier.mediation.admob.AppierAdUnitIdentifier;
import com.appier.mediation.admob.AppierPredictHandler;

public class AdMobMediationNavigationFragment extends BaseFragment {
    public AdMobMediationNavigationFragment() {}

    @Override
    protected void onViewVisible(View view) {

    }

    public static AdMobMediationNavigationFragment newInstance(String title) {
        AdMobMediationNavigationFragment fragment = new AdMobMediationNavigationFragment();
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

                predictor.predictAd(new AppierAdUnitIdentifier(getString(R.string.admob_adunit_native)));
                predictor.predictAd(new AppierAdUnitIdentifier(getString(R.string.admob_adunit_banner_300x250)));
                predictor.predictAd(new AppierAdUnitIdentifier(getString(R.string.admob_adunit_interstitial)));
            }
        });

        initializeAdMobMediationView(layout);
        return layout;
    }

    private void initializeAdMobMediationView(View layout) {
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_interstitial),
                new Pair[] {
                        new Pair<>("Interstitial", AdMobInterstitialActivity.class)
                }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_banner),
                new Pair[] {
                        new Pair<>("Banner - basic format", AdMobBannerBasicActivity.class),
                        new Pair<>("Banner - in a floating window", AdMobBannerFloatingWindowActivity.class)
                }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_native),
                new Pair[] {
                        new Pair<>("Native - basic format", AdMobNativeBasicActivity.class),
                        new Pair<>("Native - in a floating window", AdMobNativeFloatingWindowActivity.class)
                }
        );
        TextView textVersion = layout.findViewById(R.id.text_version);
        textVersion.setText(
                "Appier SDK version : " + AppierAdapterConfiguration.getNetworkSdkVersion() + "\n" +
                "AdMob Mediation SDK version : " + AppierAdapterConfiguration.getMediationVersion() + "\n" +
                "AdMob SDK version : " + MobileAds.getVersionString()
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
