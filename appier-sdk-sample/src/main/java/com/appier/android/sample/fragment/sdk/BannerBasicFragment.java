package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appier.ads.AppierBannerAd;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdsHelper;

public class BannerBasicFragment extends BaseFragment {

    private Context mContext;
    private AppierBannerAd mAppierBannerAd1;
    private AppierBannerAd mAppierBannerAd2;
    private AppierBannerAd mAppierBannerAd3;

    public BannerBasicFragment() {}

    public static BannerBasicFragment newInstance(Context context) {
        return new BannerBasicFragment(context);
    }

    private BannerBasicFragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sdk_banner_basic, container, false);
    }

    @Override
    protected void onViewVisible(View view) {
        ((LinearLayout) view.findViewById(R.id.banner_container_320_50)).removeAllViews();
        ((LinearLayout) view.findViewById(R.id.banner_container_300_250)).removeAllViews();
        ((LinearLayout) view.findViewById(R.id.banner_container_320_480)).removeAllViews();

        mAppierBannerAd1 = AppierAdsHelper.createAppierBanner(mContext, (LinearLayout) view.findViewById(R.id.banner_container_320_50), getResources().getString(R.string.zone_320x50), 320, 50);
        mAppierBannerAd2 = AppierAdsHelper.createAppierBanner(mContext, (LinearLayout) view.findViewById(R.id.banner_container_300_250), getResources().getString(R.string.zone_300x250), 300, 250);
        mAppierBannerAd3 = AppierAdsHelper.createAppierBanner(mContext, (LinearLayout) view.findViewById(R.id.banner_container_320_480), getResources().getString(R.string.zone_320x480), 320, 480);

        mAppierBannerAd1.loadAd();
        mAppierBannerAd2.loadAd();
        mAppierBannerAd3.loadAd();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}