package com.appier.android.sample.fragment.sdk;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.appier.ads.AppierNativeAd;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.AppierNativeHelper;

public class NativeFloatingWindowFragment extends BaseFloatingWindowFragment {

    private AppierNativeAd mAppierNativeAd;

    public NativeFloatingWindowFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    protected void loadAdInContainer(LinearLayout adContainer) {
        /*
         * Load Ad in ad container layout
         */
        mAppierNativeAd = AppierNativeHelper.createAppierNative(
                getActivity(), mDemoFlowController, adContainer, getResources().getString(R.string.zone_native), mFloatViewManager
        );
        mAppierNativeAd.loadAd();
    }

    protected void destroyAdView() {
        if (mAppierNativeAd != null) {
            mAppierNativeAd.destroy();
        }
    }

}