package com.appier.android.sample.activity.mediation;

import android.os.Bundle;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.fragment.mediation.MoPubBannerBasicFragment;


public class MoPubBannerBasicActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new MoPubBannerBasicFragment());
    }

}
