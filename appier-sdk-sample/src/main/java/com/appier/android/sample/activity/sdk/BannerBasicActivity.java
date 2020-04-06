package com.appier.android.sample.activity.sdk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.appier.ads.Appier;
import com.appier.ads.AppierBannerAd;
import com.appier.ads.AppierError;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.MyService;
import com.appier.android.sample.common.MyServiceController;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.BaseFragment;

public class BannerBasicActivity extends BaseActivity {
    private MyServiceController mMyServiceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMyServiceController = new MyServiceController(BannerBasicActivity.this);
        mMyServiceController.startMyService();

        addTabbedViewPager(new SectionsPagerAdapter(getSupportFragmentManager(), new String[] {"Activity", "Application", "Service"}) {
            @Override
            public Fragment getItem(int position) {
                return DemoFragment.newInstance(position, mMyServiceController);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyServiceController.stopMyService();
    }

    public static class DemoFragment extends BaseFragment {
        private static final String ARG_POSITION = "position";

        private int mPosition;
        private MyServiceController mMyServiceController;

        public static DemoFragment newInstance(int position, MyServiceController myServiceController) {
            DemoFragment fragment = new DemoFragment(myServiceController);
            Bundle args = new Bundle();
            args.putInt(ARG_POSITION, position);
            fragment.setArguments(args);
            return fragment;
        }

        public DemoFragment() {}

        public DemoFragment(MyServiceController myServiceController) {
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
            return inflater.inflate(R.layout.fragment_sdk_banner_basic, container, false);
        }

        @Override
        protected void onViewVisible(View view) {
            ((LinearLayout) view.findViewById(R.id.banner_container_320_50)).removeAllViews();
            ((LinearLayout) view.findViewById(R.id.banner_container_300_250)).removeAllViews();
            ((LinearLayout) view.findViewById(R.id.banner_container_320_480)).removeAllViews();

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
            insertBanner(context, (LinearLayout) view.findViewById(R.id.banner_container_320_50), getResources().getString(R.string.zone_320x50), 320, 50);
            insertBanner(context, (LinearLayout) view.findViewById(R.id.banner_container_300_250), getResources().getString(R.string.zone_300x250), 300, 250);
            insertBanner(context, (LinearLayout) view.findViewById(R.id.banner_container_320_480), getResources().getString(R.string.zone_320x480), 320, 480);
        }

        private void insertBanner(Context context, final LinearLayout parent, String zoneId, int width, int height) {
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

            Appier.log("[Sample App]", "====== load Appier Banner ======");
            appierBannerAd.loadAd();
        }
    }
}
