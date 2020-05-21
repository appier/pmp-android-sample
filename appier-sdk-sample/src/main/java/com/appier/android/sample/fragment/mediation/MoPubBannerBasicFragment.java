package com.appier.android.sample.fragment.mediation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.common.AppierDataKeys;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdHelper;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.HashMap;
import java.util.Map;


public class MoPubBannerBasicFragment extends BaseFragment implements MoPubView.BannerAdListener {

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
        /*
         * Apply Appier global settings
         */
        AppierAdHelper.setAppierGlobal();

        /*
         * Initialize MoPubView and load banner
         */
        Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(AppierDataKeys.AD_WIDTH_LOCAL, 300);
        localExtras.put(AppierDataKeys.AD_HEIGHT_LOCAL, 250);

        mMoPubView = getView().findViewById(R.id.banner_container_300_250);

        mMoPubView.setLocalExtras(localExtras);
        mMoPubView.setBannerAdListener(this);
        mMoPubView.setAdUnitId(getResources().getString(R.string.mopub_adunit_banner_300x250));

        mMoPubView.loadAd();
    }

    @Override
    public void onBannerLoaded(MoPubView banner) {
        Appier.log("[Sample App]", "onBannerLoaded()");
        mDemoFlowController.notifyAdBid();
    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        Appier.log("[Sample App]", "onBannerFailed():", errorCode.toString());
        mDemoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
    }

    @Override
    public void onBannerClicked(MoPubView banner) {
        Appier.log("[Sample App]", "onBannerClicked()");
    }

    @Override
    public void onBannerExpanded(MoPubView banner) {
        Appier.log("[Sample App]", "onBannerExpanded()");
    }

    @Override
    public void onBannerCollapsed(MoPubView banner) {
        Appier.log("[Sample App]", "onBannerCollapsed()");
    }

}
