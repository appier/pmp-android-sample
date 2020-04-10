package com.appier.android.sample.fragment.mediation;

import android.widget.LinearLayout;

import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.MoPubMediationNativeHelper;
import com.mopub.nativeads.MoPubNative;


public class MoPubNativeFloatingWindowFragment extends BaseFloatingWindowFragment {

    private MoPubNative mMoPubNativeAd;

    public MoPubNativeFloatingWindowFragment() {}

    protected void loadAdInContainer(LinearLayout adContainer) {
        /*
         * Load Ad in ad container layout
         */
        mMoPubNativeAd = MoPubMediationNativeHelper.createMoPubNative(
                getActivity(), mDemoFlowController, adContainer,
                getResources().getString(R.string.mopub_adunit_native),
                R.layout.template_native_ad_full_1
        );
        mMoPubNativeAd.makeRequest();
    }

    protected void destroyAdView() {
        if (mMoPubNativeAd != null) {
            mMoPubNativeAd.destroy();
        }
    }

}
