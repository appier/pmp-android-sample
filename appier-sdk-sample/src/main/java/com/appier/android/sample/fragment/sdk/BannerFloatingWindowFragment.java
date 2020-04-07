package com.appier.android.sample.fragment.sdk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.appier.ads.AppierBannerAd;
import com.appier.android.sample.R;
import com.appier.android.sample.common.FloatViewManager;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierBannerHelper;

public class BannerFloatingWindowFragment extends BaseFragment {

    private Button mLoadButton;
    private FrameLayout mOverlayFrame;
    private FloatViewManager mFloatViewManager;
    private LinearLayout mAdContainer;

    private AppierBannerAd mAppierBannerAd;

    public BannerFloatingWindowFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFloatViewManager = new FloatViewManager(getActivity(), new FloatViewManager.OnFloatViewEventListener() {
            @Override
            public void onOpen(LinearLayout contentContainer) {
                mAdContainer = contentContainer;
                View view = getView();
                if (view != null) {
                    mLoadButton.setVisibility(View.GONE);
                    mOverlayFrame.setVisibility(View.VISIBLE);
                }
                mAppierBannerAd = AppierBannerHelper.createAppierBanner(getActivity(), mAdContainer, getResources().getString(R.string.zone_300x250), 300, 250);
                mAppierBannerAd.loadAd();
            }

            @Override
            public void onDrawOverlayPermissionResult(boolean isPermissionGranted) {
                if (isPermissionGranted) {
                    mFloatViewManager.open();
                }
            }

            @Override
            public void onClose(LinearLayout contentContainer) {
                mAdContainer.removeAllViews();
                if (mAppierBannerAd != null) {
                    mAppierBannerAd.destroy();
                }
                View view = getView();
                if (view != null) {
                    mLoadButton.setVisibility(View.VISIBLE);
                    mOverlayFrame.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_floating_window, container, false);

        mLoadButton = view.findViewById(R.id.button_load);
        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatViewManager.openWithPermissionCheck();
            }
        });

        mOverlayFrame = view.findViewById(R.id.overlay_frame);
        mOverlayFrame.setVisibility(View.GONE);
        return view;
    }

    @Override
    protected void onViewVisible(View view) {}

    public FloatViewManager getFloatViewManager() {
        return mFloatViewManager;
    }

}