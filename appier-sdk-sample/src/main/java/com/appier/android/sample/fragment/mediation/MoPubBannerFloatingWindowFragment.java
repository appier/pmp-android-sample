package com.appier.android.sample.fragment.mediation;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFloatingWindowFragment;
import com.appier.android.sample.helper.MoPubMediationBannerHelper;
import com.mopub.mobileads.MoPubView;


public class MoPubBannerFloatingWindowFragment extends BaseFloatingWindowFragment {

    private MoPubView mMoPubView;

    public MoPubBannerFloatingWindowFragment() {}

    protected void loadAdInContainer(LinearLayout adContainer) {
        /*
         * Load Ad in ad container layout
         */
        mMoPubView = MoPubMediationBannerHelper.createMoPubView(
                getActivity(), mDemoFlowController, adContainer,
                getResources().getString(R.string.mopub_adunit_banner_300x250), 300, 250
        );
        mMoPubView.loadAd();
    }

    protected void destroyAdView() {
        if (mMoPubView != null) {
            mMoPubView.destroy();
        }
    }

}
