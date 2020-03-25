package com.appier.android.sample.activity.sdk;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.DemoContextFragment;

public class BannerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTabbedViewPager(new SectionsPagerAdapter(getSupportFragmentManager(), new String[] {"Activity", "Application", "Service"}) {
            @Override
            public Fragment getItem(int position) {
                return DemoContextFragment.newInstance(position);
            }
        });
    }
}
