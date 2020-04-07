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

    public MoPubNativeBasicFragment() {}

    public static MoPubNativeBasicFragment newInstance(Context context) {
        return new MoPubNativeBasicFragment(context);
    }

    private MoPubNativeBasicFragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sdk_native_basic, container, false);
    }

    @Override
    protected void onViewVisible(View view) {
        ((LinearLayout) view.findViewById(R.id.ad_container)).removeAllViews();
        mMoPubNativeAd = MoPubMediationNativeHelper.createMoPubNative(
                mContext, (LinearLayout) view.findViewById(R.id.ad_container),
                getResources().getString(R.string.mopub_adunit_native)
        );
        mMoPubNativeAd.makeRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
