package com.appier.android.sample.helper;

import android.content.Context;
import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdAdapter;
import com.appier.ads.AppierBannerAd;
import com.appier.ads.AppierError;
import com.appier.ads.AppierRecyclerAdapter;
import com.appier.android.sample.common.DemoFlowController;

public class AppierBannerHelper {

    public static AppierBannerAd createAppierBanner(Context context,
                                                    final DemoFlowController demoFlowController,
                                                    final LinearLayout parentLayout,
                                                    String zoneId, int width, int height) {

        AppierBannerAd appierBannerAd = new AppierBannerAd(context, new AppierBannerAd.EventListener() {
            @Override
            public void onAdLoaded(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");
                demoFlowController.notifyAdBid();
                parentLayout.addView(appierBannerAd.getView());
            }

            @Override
            public void onAdNoBid(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdNoBid()");
                demoFlowController.notifyAdNoBid();
            }

            @Override
            public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
                demoFlowController.notifyAdError(appierError);
            }

            @Override
            public void onViewClick(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onViewClick()");
            }
        });
        appierBannerAd.setAdDimension(width, height);
        appierBannerAd.setZoneId(zoneId);

        return appierBannerAd;
    }

    public static void insertAppierBannerToListView(Context context,
                                                    final DemoFlowController demoFlowController,
                                                    final AppierAdAdapter appierAdAdapter, final int insertPosition,
                                                    String zoneId, int width, int height) {

        AppierBannerAd appierBannerAd = new AppierBannerAd(context, new AppierBannerAd.EventListener() {
            @Override
            public void onAdLoaded(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");
                try {
                    appierAdAdapter.insertAd(insertPosition, appierBannerAd);
                    demoFlowController.notifyAdBid();
                } catch (Exception e) {
                    Appier.log("[Sample App] Fail to insert ad into list. Maybe the position is out of bound or is already used.");
                    demoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
                }
            }

            @Override
            public void onAdNoBid(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdNoBid()");
                demoFlowController.notifyAdNoBid();
            }

            @Override
            public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
                demoFlowController.notifyAdError(appierError);
            }

            @Override
            public void onViewClick(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onViewClick()");
            }
        });
        appierBannerAd.setAdDimension(width, height);
        appierBannerAd.setZoneId(zoneId);

        Appier.log("[Sample App]", "====== load Appier Banner ======");
        appierBannerAd.loadAd();
    }

    public static void insertAppierBannerToRecyclerView(Context context,
                                                        final DemoFlowController demoFlowController,
                                                        final AppierRecyclerAdapter appierRecyclerAdapter, final int insertPosition,
                                                        String zoneId, int width, int height) {

        AppierBannerAd appierBannerAd = new AppierBannerAd(context, new AppierBannerAd.EventListener() {
            @Override
            public void onAdLoaded(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");
                try {
                    appierRecyclerAdapter.insertAd(insertPosition, appierBannerAd);
                    demoFlowController.notifyAdBid();
                } catch (Exception e) {
                    Appier.log("[Sample App] Fail to insert ad into list. Maybe the position is out of bound or is already used.");
                    demoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
                }
            }

            @Override
            public void onAdNoBid(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdNoBid()");
                demoFlowController.notifyAdNoBid();
            }

            @Override
            public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
                demoFlowController.notifyAdError(appierError);
            }

            @Override
            public void onViewClick(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onViewClick()");
            }
        });
        appierBannerAd.setAdDimension(width, height);
        appierBannerAd.setZoneId(zoneId);

        Appier.log("[Sample App]", "====== load Appier Banner ======");
        appierBannerAd.loadAd();
    }
}
