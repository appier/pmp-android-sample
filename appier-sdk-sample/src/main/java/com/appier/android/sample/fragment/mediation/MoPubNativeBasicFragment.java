package com.appier.android.sample.fragment.mediation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.MoPubMediationNativeHelper;
import com.mopub.nativeads.MoPubNative;

public class MoPubNativeBasicFragment extends BaseFragment {

    private Context mContext;
    private MoPubNative mMoPubNativeAd;

    public static MoPubNativeBasicFragment newInstance(Context context) {
        return new MoPubNativeBasicFragment(context);
    }

    public MoPubNativeBasicFragment() {}

    private MoPubNativeBasicFragment(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sdk_native_basic, container, false);
    }

    @Override
    protected void onViewVisible(View view) {
        ((LinearLayout) view.findViewById(R.id.ad_container)).removeAllViews();
        mMoPubNativeAd = MoPubMediationNativeHelper.createMoPubNative(
                mContext, mDemoFlowController, (LinearLayout) view.findViewById(R.id.ad_container),
                getResources().getString(R.string.mopub_adunit_native),
                R.layout.template_native_ad_full_1
        );
        mMoPubNativeAd.makeRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
