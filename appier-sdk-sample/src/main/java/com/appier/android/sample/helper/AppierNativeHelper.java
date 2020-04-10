package com.appier.android.sample.helper;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdAdapter;
import com.appier.ads.AppierError;
import com.appier.ads.AppierNativeAd;
import com.appier.ads.AppierNativeViewBinder;
import com.appier.ads.AppierRecyclerAdapter;
import com.appier.android.sample.R;
import com.appier.android.sample.common.DemoFlowController;
import com.appier.android.sample.common.FloatViewManager;

public class AppierNativeHelper {

    public static AppierNativeAd createAppierNative(Context context, final DemoFlowController demoFlowController, final LinearLayout parentLayout, String zoneId) {
        return createAppierNative(context, demoFlowController, parentLayout, zoneId, null);
    }

    public static AppierNativeAd createAppierNative(Context context, final DemoFlowController demoFlowController, final LinearLayout parentLayout, String zoneId, final FloatViewManager floatViewManager) {

        /*
         * (Required) Appier Native Ad integration
         */
        AppierNativeViewBinder appierNativeViewBinder = new AppierNativeViewBinder.Builder(R.layout.template_native_ad_full_1)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        AppierNativeAd appierNativeAd = new AppierNativeAd(context, new AppierNativeAd.EventListener() {

            @Override
            public void onAdLoaded(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App] onAdLoaded()");
                View adView = appierNativeAd.getAdView();
                parentLayout.addView(adView);
                demoFlowController.notifyAdBid();
            }

            @Override
            public void onAdNoBid(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App] onAdNoBid()");
                demoFlowController.notifyAdNoBid();
                if (floatViewManager != null) {
                    floatViewManager.close();
                }
            }

            @Override
            public void onAdLoadFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App] onAdLoadFail()", appierError.toString());
                demoFlowController.notifyAdError(appierError);
                if (floatViewManager != null) {
                    floatViewManager.close();
                }
            }

            @Override
            public void onAdShown(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App] onAdShown()");
            }

            @Override
            public void onImpressionRecorded(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App] onImpressionRecorded()");
            }

            @Override
            public void onImpressionRecordFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App] onImpressionRecordFail()", appierError.toString());
            }

            @Override
            public void onAdClick(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App] onAdClick()");
            }

            @Override
            public void onAdClickFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App] onAdClickFail()");
            }

        });

        appierNativeAd.setViewBinder(appierNativeViewBinder);
        appierNativeAd.setZoneId(zoneId);

        return appierNativeAd;
    }

    /*
     * A helper to create Native Ad and insert into specific position when the ad is loaded
     */
    public static void insertAppierNativeToListView(Context context,
                                                    final DemoFlowController demoFlowController,
                                                    final AppierAdAdapter appierAdAdapter,
                                                    final int insertPosition, String zoneId, int layoutId) {

        Appier.setTestMode(true);

        AppierNativeViewBinder appierNativeViewBinder = new AppierNativeViewBinder.Builder(layoutId)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        AppierNativeAd appierNativeAd = new AppierNativeAd(context, new AppierNativeAd.EventListener() {
            @Override
            public void onAdLoaded(AppierNativeAd appierNativeAd) {

                Appier.log("[Sample App] onAdLoaded()");

                try {
                    appierAdAdapter.insertAd(insertPosition, appierNativeAd);
                    demoFlowController.notifyAdBid();
                } catch (Exception e) {
                    Appier.log("[Sample App] Fail to insert ad into list. Maybe the position is out of bound or is already used.");
                    demoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
                }

            }

            @Override
            public void onAdNoBid(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onAdNoBid()");
                demoFlowController.notifyAdNoBid();
            }

            @Override
            public void onAdLoadFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onAdLoadFail()", appierError.toString());
                demoFlowController.notifyAdError(appierError);
            }

            @Override
            public void onAdShown(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onAdShown()");
            }

            @Override
            public void onImpressionRecorded(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onImpressionRecorded()");
            }

            @Override
            public void onImpressionRecordFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onImpressionRecordFail()", appierError.toString());
            }

            @Override
            public void onAdClick(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onAdClick()");
            }

            @Override
            public void onAdClickFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onAdClickFail()");
            }
        });
        appierNativeAd.setViewBinder(appierNativeViewBinder);
        appierNativeAd.setZoneId(zoneId);

        Appier.log("[Sample App]", "====== load Appier Native ======");
        appierNativeAd.loadAd();
    }

    /*
     * A helper to create Native Ad and insert into specific position when the ad is loaded
     */
    public static void insertAppierNativeToRecyclerView(Context context,
                                                        final DemoFlowController demoFlowController,
                                                        final AppierRecyclerAdapter appierRecyclerAdapter,
                                                        final int insertPosition, String zoneId, int layoutId) {

        Appier.setTestMode(true);

        AppierNativeViewBinder appierNativeViewBinder = new AppierNativeViewBinder.Builder(layoutId)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        AppierNativeAd appierNativeAd = new AppierNativeAd(context, new AppierNativeAd.EventListener() {
            @Override
            public void onAdLoaded(AppierNativeAd appierNativeAd) {

                Appier.log("[Sample App] onAdLoaded()");

                try {
                    appierRecyclerAdapter.insertAd(insertPosition, appierNativeAd);
                    demoFlowController.notifyAdBid();
                } catch (Exception e) {
                    Appier.log("[Sample App] Fail to insert ad into list. Maybe the position is out of bound or is already used.");
                    demoFlowController.notifyAdError(AppierError.UNKNOWN_ERROR);
                }

            }

            @Override
            public void onAdNoBid(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onAdNoBid()");
                demoFlowController.notifyAdNoBid();
            }

            @Override
            public void onAdLoadFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onAdLoadFail()", appierError.toString());
                demoFlowController.notifyAdError(appierError);
            }

            @Override
            public void onAdShown(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onAdShown()");
            }

            @Override
            public void onImpressionRecorded(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onImpressionRecorded()");
            }

            @Override
            public void onImpressionRecordFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onImpressionRecordFail()", appierError.toString());
            }

            @Override
            public void onAdClick(AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onAdClick()");
            }

            @Override
            public void onAdClickFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                Appier.log("[Sample App]", "[Native]", "onAdClickFail()");
            }
        });
        appierNativeAd.setViewBinder(appierNativeViewBinder);
        appierNativeAd.setZoneId(zoneId);

        Appier.log("[Sample App]", "====== load Appier Native ======");
        appierNativeAd.loadAd();
    }

}
