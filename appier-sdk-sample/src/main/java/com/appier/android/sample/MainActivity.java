package com.appier.android.sample;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appier.ads.Appier;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.navigation.AdMobMediationNavigationFragment;
import com.appier.android.sample.fragment.navigation.MoPubMediationNavigationFragment;
import com.appier.android.sample.fragment.navigation.SdkNavigationFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.quantumgraph.sdk.QG;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] titles = new String[]{"Appier SDK", "MoPub Mediation", "AdMob Mediation"};

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles) {
            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        return SdkNavigationFragment.newInstance(titles[position]);
                    case 1:
                        return MoPubMediationNavigationFragment.newInstance(titles[position]);
                    case 2:
                        return AdMobMediationNavigationFragment.newInstance(titles[position]);
                    default:
                        throw new IndexOutOfBoundsException();
                }

            }
        };

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Lock screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // AIQUA SDK
        QG.initializeSdk(getApplication(), getResources().getString(R.string.aiqua_app_id));

        // AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Appier.log("AdMob", "SDK initialized");
            }
        });

        // MoPub SDK
        SdkConfiguration sdkConfiguration = new SdkConfiguration
                .Builder(getString(R.string.mopub_adunit_predict_interstitial))
                .build();
        MoPub.initializeSdk(getApplicationContext(), sdkConfiguration, new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                Log.d("MoPub", "SDK initialized");
            }
        });
    }
}
