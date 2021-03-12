package com.appier.android.sample.activity.mediation.mopub;

import android.os.Bundle;

import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.fragment.mediation.mopub.MoPubRewardedVideoFragment;

public class MoPubRewardedVideoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load rewarded video fragment
        addFragment(new MoPubRewardedVideoFragment());
    }
}
