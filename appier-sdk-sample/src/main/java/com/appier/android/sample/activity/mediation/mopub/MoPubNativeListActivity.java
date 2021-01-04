package com.appier.android.sample.activity.mediation.mopub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.mediation.mopub.MoPubNativeListViewFragment;
import com.appier.android.sample.fragment.mediation.mopub.MoPubNativeRecyclerViewFragment;


public class MoPubNativeListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTabbedViewPager(new SectionsPagerAdapter(getSupportFragmentManager(), new String[] {"ListView", "RecyclerView"}) {
            @Override
            @NonNull
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new MoPubNativeListViewFragment();
                    case 1:
                        return new MoPubNativeRecyclerViewFragment();
                    default:
                        throw new IndexOutOfBoundsException();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
