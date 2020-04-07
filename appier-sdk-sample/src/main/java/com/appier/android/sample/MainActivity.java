package com.appier.android.sample;

import android.os.Bundle;

import com.appier.android.sample.fragment.navigation.MoPubMediationNavigationFragment;
import com.appier.android.sample.fragment.navigation.SdkNavigationFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.appier.android.sample.common.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] titles = new String[]{"Appier SDK", "MoPub Mediation"};

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles) {
            @Override
            public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        return SdkNavigationFragment.newInstance(titles[position]);
                    case 1:
                        return MoPubMediationNavigationFragment.newInstance(titles[position]);
                    default:
                        throw new IndexOutOfBoundsException();
                }

            }
        };

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}