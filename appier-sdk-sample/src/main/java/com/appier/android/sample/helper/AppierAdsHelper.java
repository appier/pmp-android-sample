package com.appier.android.sample.helper;

import android.content.Context;
import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdAdapter;
import com.appier.ads.AppierBannerAd;
import com.appier.ads.AppierError;
import com.appier.ads.AppierRecyclerAdapter;

public class AppierAdsHelper {

    public static void setAppierGlobal() {
        // TODO: Setup Appier Global vars, include GDPR,... etc.
    }

    public static AppierBannerAd createAppierBanner(Context context,
                                                    final LinearLayout parent,
                                                    String zoneId, int width, int height) {

        AppierBannerAd appierBannerAd = new AppierBannerAd(context, new AppierBannerAd.EventListener() {
            @Override
            public void onAdLoaded(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");
                parent.addView(appierBannerAd.getView());
            }

            @Override
            public void onAdNoBid(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdNoBid()");
            }

            @Override
            public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
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
                                             final AppierAdAdapter appierAdAdapter, final int insertPosition,
                                             String zoneId, int width, int height) {

        AppierBannerAd appierBannerAd = new AppierBannerAd(context, new AppierBannerAd.EventListener() {
            @Override
            public void onAdLoaded(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");
                try {
                    appierAdAdapter.insertAd(insertPosition, appierBannerAd);
                } catch (Exception e) {
                    Appier.log("[Sample App] Fail to insert ad into list. Maybe the position is out of bound or is already used.");
                }
            }

            @Override
            public void onAdNoBid(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdNoBid()");
            }

            @Override
            public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
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
                                                        final AppierRecyclerAdapter appierRecyclerAdapter, final int insertPosition,
                                                        String zoneId, int width, int height) {

        AppierBannerAd appierBannerAd = new AppierBannerAd(context, new AppierBannerAd.EventListener() {
            @Override
            public void onAdLoaded(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");
                try {
                    appierRecyclerAdapter.insertAd(insertPosition, appierBannerAd);
                } catch (Exception e) {
                    Appier.log("[Sample App] Fail to insert ad into list. Maybe the position is out of bound or is already used.");
                }
            }

            @Override
            public void onAdNoBid(AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdNoBid()");
            }

            @Override
            public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
                Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
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
