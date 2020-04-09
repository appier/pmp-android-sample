package com.appier.android.sample.fragment.sdk;

import android.widget.LinearLayout;

import com.appier.ads.AppierBannerAd;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.AppierBannerHelper;

public class BannerFloatingWindowFragment extends BaseFloatingWindowFragment {

    private AppierBannerAd mAppierBannerAd;

    public BannerFloatingWindowFragment() {}

    protected void loadAdInContainer(LinearLayout adContainer) {
        /*
         * Load Ad in ad container layout
         */
        mAppierBannerAd = AppierBannerHelper.createAppierBanner(
                getActivity(), mDemoFlowController, adContainer,
                getResources().getString(R.string.zone_300x250), 300, 250
        );
        mAppierBannerAd.loadAd();
    }

    protected void destroyAdView() {
        if (mAppierBannerAd != null) {
            mAppierBannerAd.destroy();
        }
    }

}
