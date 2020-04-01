package com.appier.android.sample.activity.mediation.mopub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.appier.ads.Appier;
import com.appier.ads.common.AppierDataKeys;
import com.appier.ads.common.Dimension;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.FloatViewManager;
import com.appier.android.sample.fragment.BaseFragment;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.HashMap;
import java.util.Map;

public class BannerFloatingWindowActivity extends BaseActivity {
    DemoFragment mDemoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDemoFragment = new DemoFragment();
        addFragment(mDemoFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDemoFragment.getFloatViewManager().handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDemoFragment.getFloatViewManager().close();
    }

    public static class DemoFragment extends BaseFragment {
        private FloatViewManager mFloatViewManager;
        private LinearLayout mAdContainer;
        private FrameLayout mOverlayFrame;
        private View mAdView;
        private MoPubView mMoPubView;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mFloatViewManager = new FloatViewManager(getActivity(), new FloatViewManager.OnFloatViewEventListener() {
                @Override
                public void onOpen(LinearLayout contentContainer) {
                    mAdContainer = contentContainer;
                    View view = getView();
                    if (view != null) {
                        view.findViewById(R.id.button_load).setVisibility(View.GONE);
                        mOverlayFrame.setVisibility(View.VISIBLE);
                    }
                    loadBanner(getResources().getString(R.string.mopub_adunit_banner_300x250), 300, 250);
                }

                @Override
                public void onDrawOverlayPermissionResult(boolean isPermissionGranted) {
                    if (isPermissionGranted) {
                        mFloatViewManager.open();
                    }
                }

                @Override
                public void onClose(LinearLayout contentContainer) {
                    if (mAdView != null) {
                        contentContainer.removeView(mAdView);
                    }
                    if (mMoPubView != null) {
                        mMoPubView.destroy();
                    }
                    View view = getView();
                    if (view != null) {
                        view.findViewById(R.id.button_load).setVisibility(View.VISIBLE);
                        mOverlayFrame.setVisibility(View.GONE);
                    }
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_common_floating_window, container, false);
            view.findViewById(R.id.button_load).setOnClickListener(new View.OnClickListener() {
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
        public void onDestroy() {
            super.onDestroy();
            if (mMoPubView != null) {
                mMoPubView.destroy();
            }
        }

        @Override
        protected void onViewVisible(View view) {}

        public FloatViewManager getFloatViewManager() {
            return mFloatViewManager;
        }

        private void loadBanner(String adUnitId, int width, int height) {
            Map<String, Object> localExtras = new HashMap<>();
            localExtras.put(AppierDataKeys.AD_WIDTH_LOCAL, width);
            localExtras.put(AppierDataKeys.AD_HEIGHT_LOCAL, height);

            mMoPubView = new MoPubView(getContext());
            mMoPubView.setLayoutParams(new LinearLayout.LayoutParams(Dimension.dipsToIntPixels(width, getContext()), Dimension.dipsToIntPixels(height, getContext())));
            mMoPubView.setLocalExtras(localExtras);
            mMoPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
                // Sent when the banner has successfully retrieved an ad.
                @Override
                public void onBannerLoaded(MoPubView banner) {
                    Appier.log("[Sample App]", "onBannerLoaded()");
                    if (mAdContainer != null) {
                        mAdView = banner;
                        if (mAdView.getParent() != null) {
                            ((ViewGroup) mAdView.getParent()).removeAllViews();
                        }
                        mAdContainer.addView(mAdView);
                    }
                }

                // Sent when the banner has failed to retrieve an ad. You can use the MoPubErrorCode value to diagnose the cause of failure.
                @Override
                public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                    Appier.log("[Sample App]", "onBannerFailed():", errorCode.toString());
                }

                // Sent when the user has tapped on the banner.
                @Override
                public void onBannerClicked(MoPubView banner) {
                    Appier.log("[Sample App]", "onBannerClicked()");
                }

                // Sent when the banner has just taken over the screen.
                @Override
                public void onBannerExpanded(MoPubView banner) {
                    Appier.log("[Sample App]", "onBannerExpanded()");
                }

                // Sent when an expanded banner has collapsed back to its original size.
                @Override
                public void onBannerCollapsed(MoPubView banner) {
                    Appier.log("[Sample App]", "onBannerCollapsed()");
                }
            });
            mMoPubView.setAdUnitId(adUnitId); // Enter your Ad Unit ID from www.mopub.com
            Appier.log("[Sample App]", "====== make request ======");
            mMoPubView.loadAd();
        }
    }
}