package com.appier.android.sample.activity.sdk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.AppierNativeAd;
import com.appier.ads.AppierNativeViewBinder;
import com.appier.ads.common.AppierTargeting;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.MyService;
import com.appier.android.sample.common.MyServiceController;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.BaseFragment;

public class NativeBasicActivity extends BaseActivity {
    private MyServiceController mMyServiceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMyServiceController = new MyServiceController(NativeBasicActivity.this);
        mMyServiceController.startMyService();

        addTabbedViewPager(new SectionsPagerAdapter(getSupportFragmentManager(), new String[] {"Activity", "Application", "Service"}) {
            @Override
            public Fragment getItem(int position) {
                return NativeAdFragment.newInstance(position, mMyServiceController);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyServiceController.stopMyService();
    }

    public static class NativeAdFragment extends BaseFragment {
        private static final String ARG_POSITION = "position";

        private int mPosition;
        private MyServiceController mMyServiceController;

        public static NativeAdFragment newInstance(int position, MyServiceController myServiceController) {
            NativeAdFragment fragment = new NativeAdFragment(myServiceController);
            Bundle args = new Bundle();
            args.putInt(ARG_POSITION, position);
            fragment.setArguments(args);
            return fragment;
        }

        public NativeAdFragment() {}

        public NativeAdFragment(MyServiceController myServiceController) {
            mMyServiceController = myServiceController;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mPosition = getArguments().getInt(ARG_POSITION);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_sdk_native_basic, container, false);
        }

        @Override
        protected void onViewVisible(View view) {
            ((LinearLayout) view.findViewById(R.id.ad_container)).removeAllViews();

            Context context = getActivity();
            switch (mPosition) {
                case 0:
                    context = getActivity();
                    break;
                case 1:
                    context = getActivity().getApplicationContext();
                    break;
                case 2:
                    MyService myService = mMyServiceController.getService();
                    if (myService == null) {
                        Appier.log("[Sample App]", "Please make sure the service is added into manifest");
                        return;
                    }
                    context = myService;
                    break;
            }
            insertNative(context, (LinearLayout) view.findViewById(R.id.ad_container), getResources().getString(R.string.zone_native));
        }

        private void insertNative(Context context, final LinearLayout parentLayout, String zoneId) {

            Appier.setTestMode(true);

            /*
             * (Optional) Set GDPR and COPPA explicitly to follow the regulations
             */
            Appier.setGDPRApplies(true);
            Appier.setCoppaApplies(true);

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
                }

                @Override
                public void onAdNoBid(AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App] onAdNoBid()");
                }

                @Override
                public void onAdLoadFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App] onAdLoadFail()", appierError.toString());
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

            /*
             * (Optional) Set Appier Targeting
             * Set targeting could bring more precise ads, which may increase revenue for App developers
             */
            appierNativeAd.setYob(2001);
            appierNativeAd.setGender(AppierTargeting.Gender.MALE);
            appierNativeAd.addKeyword("interest", "sports");

            /*
             * Load Ad and display in the UI through the view binder
             */
            Appier.log("[Sample App]", "====== load Appier Native ======");
            appierNativeAd.loadAd();

        }
    }
}
