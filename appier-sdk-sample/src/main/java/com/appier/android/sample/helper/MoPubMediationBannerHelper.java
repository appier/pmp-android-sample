package com.appier.android.sample.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.common.AppierDataKeys;
import com.appier.ads.common.Dimension;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.HashMap;
import java.util.Map;

public class MoPubMediationBannerHelper {

    /*
     * Load MoPub Ad in an existing MoPubView in the layout
     */
    public static void loadMoPubView(MoPubView moPubView, String adUnitId, int width, int height) {

        Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(AppierDataKeys.AD_WIDTH_LOCAL, width);
        localExtras.put(AppierDataKeys.AD_HEIGHT_LOCAL, height);

        moPubView.setLocalExtras(localExtras);
        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(MoPubView banner) {
                Appier.log("[Sample App]", "onBannerLoaded()");
            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                Appier.log("[Sample App]", "onBannerFailed():", errorCode.toString());
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
        });
        moPubView.setAdUnitId(adUnitId);

        Appier.log("[Sample App]", "====== make request ======");
        moPubView.loadAd();
    }

    /*
     * Create a new MoPubView and add the AdView in the parent view
     */
    public static MoPubView createMoPubView(Context context, final LinearLayout parentLayout, String adUnitId, int width, int height) {
        Map<String, Object> localExtras = new HashMap<>();
        localExtras.put(AppierDataKeys.AD_WIDTH_LOCAL, width);
        localExtras.put(AppierDataKeys.AD_HEIGHT_LOCAL, height);

        MoPubView moPubView = new MoPubView(context);
        moPubView.setLayoutParams(new LinearLayout.LayoutParams(Dimension.dipsToIntPixels(width, context), Dimension.dipsToIntPixels(height, context)));
        moPubView.setLocalExtras(localExtras);
        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(MoPubView banner) {
                Appier.log("[Sample App]", "onBannerLoaded()");
                if (parentLayout != null) {
                    View mAdView = banner;
                    if (mAdView.getParent() != null) {
                        ((ViewGroup) mAdView.getParent()).removeAllViews();
                    }
                    parentLayout.addView(mAdView);
                }
            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                Appier.log("[Sample App]", "onBannerFailed():", errorCode.toString());
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
        });
        moPubView.setAdUnitId(adUnitId);

        return moPubView;
    }
}
