package com.appier.android.sample.fragment.sdk;

import android.widget.LinearLayout;

import com.appier.ads.AppierNativeAd;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.AppierNativeHelper;

public class NativeFloatingWindowFragment extends BaseFloatingWindowFragment {

    private AppierNativeAd mAppierNativeAd;

    public NativeFloatingWindowFragment() {}

    protected void loadAdInContainer(LinearLayout adContainer) {
        /*
         * Load Ad in ad container layout
         */
        mAppierNativeAd = AppierNativeHelper.createAppierNative(
                getActivity(), adContainer, getResources().getString(R.string.zone_native)
        );
        mAppierNativeAd.loadAd();
    }

    protected void destroyAdView() {
        if (mAppierNativeAd != null) {
            mAppierNativeAd.destroy();
        }
    }

}