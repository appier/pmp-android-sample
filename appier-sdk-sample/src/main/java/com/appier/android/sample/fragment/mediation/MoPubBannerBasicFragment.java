package com.appier.android.sample.fragment.mediation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.MoPubMediationBannerHelper;
import com.mopub.mobileads.MoPubView;


public class MoPubBannerBasicFragment extends BaseFragment {

    private MoPubView mMoPubView;

    public MoPubBannerBasicFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mediation_mopub_banner_basic, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMoPubView != null) {
            mMoPubView.destroy();
        }
    }

    @Override
    protected void onViewVisible(View view) {
        mMoPubView = getView().findViewById(R.id.banner_container_300_250);
        MoPubMediationBannerHelper.loadMoPubView(mMoPubView, mDemoFlowController, getResources().getString(R.string.mopub_adunit_banner_300x250), 300, 250);
    }

}
