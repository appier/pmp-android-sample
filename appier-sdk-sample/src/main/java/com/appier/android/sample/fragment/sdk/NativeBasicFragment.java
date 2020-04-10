package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appier.ads.AppierNativeAd;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierNativeHelper;

public class NativeBasicFragment extends BaseFragment {

    private Context mContext;
    private AppierNativeAd mAppierNativeAd;

    public NativeBasicFragment() {}

    public static NativeBasicFragment newInstance(Context context) {
        return new NativeBasicFragment(context);
    }

    private NativeBasicFragment(Context context) {
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
        mAppierNativeAd = AppierNativeHelper.createAppierNative(mContext, mDemoFlowController, (LinearLayout) view.findViewById(R.id.ad_container), getResources().getString(R.string.zone_native));
        mAppierNativeAd.loadAd();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
