package com.appier.android.sample.activity.mediation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.appier.ads.Appier;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.MyService;
import com.appier.android.sample.common.MyServiceController;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.BaseFragment;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.AppierNativeAdRenderer;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;

public class MoPubNativeBasicActivity extends BaseActivity {
    private MyServiceController mMyServiceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMyServiceController = new MyServiceController(MoPubNativeBasicActivity.this);
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
        private NativeAd.MoPubNativeEventListener moPubNativeEventListener;

        public static NativeAdFragment newInstance(int position, MyServiceController myServiceController) {
            NativeAdFragment fragment = new NativeAdFragment(myServiceController);
            Bundle args = new Bundle();
            args.putInt(ARG_POSITION, position);
            fragment.setArguments(args);
            return fragment;
        }

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
            insertMoPubNative(context, (LinearLayout) view.findViewById(R.id.ad_container), getResources().getString(R.string.mopub_adunit_native));
        }

        private void insertMoPubNative(final Context context, final LinearLayout parentLayout, String adunitId) {

            Appier.setTestMode(true);

            /*
             * (Optional) Set GDPR and COPPA explicitly to follow the regulations
             */
            Appier.setGDPRApplies(true);
            Appier.setCoppaApplies(true);

            /*
             * (Required) MoPub NativeAd mediation integration
             */
            moPubNativeEventListener = new NativeAd.MoPubNativeEventListener() {
                @Override
                public void onImpression(View view) {
                    Appier.log("[Sample App]", "Native ad recorded an impression.");
                    // Impress is recorded - do what is needed AFTER the ad is visibly shown here.
                }

                @Override
                public void onClick(View view) {
                    Appier.log("[Sample App]", "Native ad recorded a click.");
                    // Click tracking.
                }
            };

            MoPubNative.MoPubNativeNetworkListener moPubNativeNetworkListener = new MoPubNative.MoPubNativeNetworkListener() {
                @Override
                public void onNativeLoad(final NativeAd nativeAd) {
                    Appier.log("[Sample App]", "Native ad has loaded.");

                    final AdapterHelper adapterHelper = new AdapterHelper(context, 0, 3); // When standalone, any range will be fine.

                    // Retrieve the pre-built ad view that AdapterHelper prepared for us.
                    View adView = adapterHelper.getAdView(null, null, nativeAd, new ViewBinder.Builder(0).build());

                    // Set the native event listeners (onImpression, and onClick).
                    nativeAd.setMoPubNativeEventListener(moPubNativeEventListener);

                    // Add the ad view to our view hierarchy
                    parentLayout.addView(adView);
                }

                @Override
                public void onNativeFail(NativeErrorCode errorCode) {
                    Appier.log("[Sample App]", "Native ad failed to load with error:", errorCode.toString());
                }
            };

            ViewBinder viewBinder = new ViewBinder.Builder(R.layout.template_native_ad_full_1)
                    .mainImageId(R.id.native_main_image)
                    .iconImageId(R.id.native_icon_image)
                    .titleId(R.id.native_title)
                    .textId(R.id.native_text)
                    .callToActionId(R.id.native_cta)
                    .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                    .build();

            // Note: Appier zone_id is integrated through server extra on MoPub console
            AppierNativeAdRenderer appierNativeAdRenderer = new AppierNativeAdRenderer(viewBinder);
            MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

            MoPubNative moPubNative = new MoPubNative(context, adunitId, moPubNativeNetworkListener);

            moPubNative.registerAdRenderer(appierNativeAdRenderer);
            moPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);

            Appier.log("[Sample App]", "====== make request ======");
            moPubNative.makeRequest();

        }
    }
}
