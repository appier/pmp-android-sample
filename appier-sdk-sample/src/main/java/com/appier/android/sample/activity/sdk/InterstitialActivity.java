package com.appier.android.sample.activity.sdk;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.AppierInterstitialAd;
import com.appier.ads.common.AppierTargeting;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;


public class InterstitialActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load interstitial fragment
        addFragment(InterstitialFragment.newInstance());
    }

    public static class InterstitialFragment extends BaseFragment {

        static final int STATE_UNLOADED = 0;
        static final int STATE_LOADING = 1;
        static final int STATE_LOADED = 2;

        ProgressBar mProgressLoading;
        Button mButtonLoad;
        ImageView mImageSteps;
        TextView mTextIndicator;

        private int mCurrentState = STATE_UNLOADED;
        private AppierInterstitialAd mAppierInterstitialAd;
        private Context mContext;

        // TODO: extract to parent class
        public static InterstitialFragment newInstance() {
            InterstitialFragment fragment = new InterstitialFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }
        // TODO: need refactor
        @Override
        protected void onViewVisible(View view) {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_common_interstitial, container, false);
            mContext = getActivity();

            mProgressLoading = view.findViewById(R.id.progress_loading);
            mImageSteps = view.findViewById(R.id.img_step_progress);
            mButtonLoad = view.findViewById(R.id.button_load);
            mTextIndicator = view.findViewById(R.id.text_step_indicator);

            Appier.setTestMode(true);

            mButtonLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                     * Load/Show interstitial ads by current state
                     */
                    if (mCurrentState == STATE_UNLOADED) {
                        Appier.log("[Sample App]", "====== load Appier Interstitial ======");
                        createAppierInterstitial(mContext);
                        mAppierInterstitialAd.loadAd();

                        mCurrentState = getNextLoadingState(mCurrentState);
                        updateLayoutByState(mCurrentState);

                    } else if (mCurrentState == STATE_LOADED) {
                        Appier.log("[Sample App]", "====== show Appier Interstitial ======");
                        mAppierInterstitialAd.showAd();
                    }

                }
            });

            return view;
        }

        private void updateLayoutByState(int state) {
            if (state == STATE_UNLOADED) {
                mProgressLoading.setVisibility(View.INVISIBLE);
                mTextIndicator.setText("Step 1: Click load and load the ad.");
                mTextIndicator.setTextColor(getResources().getColor(R.color.colorTextDefault));
                mButtonLoad.setText("LOAD");
                mImageSteps.setImageResource(R.drawable.illustration_interstitial_step0);
            } else if (state == STATE_LOADING) {
                mProgressLoading.setVisibility(View.VISIBLE);
                mTextIndicator.setText("Step 1: Click load and load the ad.");
                mTextIndicator.setTextColor(getResources().getColor(R.color.colorTextFaded));
                mButtonLoad.setText("");
                mImageSteps.setImageResource(R.drawable.illustration_interstitial_step1);
            } else if (state == STATE_LOADED) {
                mProgressLoading.setVisibility(View.INVISIBLE);
                mTextIndicator.setText("Step 2: Click show to see the ad.");
                mTextIndicator.setTextColor(getResources().getColor(R.color.colorTextDefault));
                mButtonLoad.setText("SHOW");
                mImageSteps.setImageResource(R.drawable.illustration_interstitial_step2);
            }

        }

        private int getNextLoadingState(int currentState) {
            switch (currentState) {
                case STATE_UNLOADED:
                    return STATE_LOADING;
                case STATE_LOADING:
                    return STATE_LOADED;
                default:
                    return STATE_UNLOADED;
            }
        }

        private void createAppierInterstitial(Context context) {
            if (mAppierInterstitialAd != null) {
                mAppierInterstitialAd.destroy();
            }

            /*
             * (Optional) Set GDPR and COPPA explicitly to follow the regulations
             */
            Appier.setGDPRApplies(true);
            Appier.setCoppaApplies(true);

            /*
             * (Required) Appier Interstitial Ad integration
             */
            mAppierInterstitialAd = new AppierInterstitialAd(context, new AppierInterstitialAd.EventListener() {
                @Override
                public void onAdLoaded() {
                    Appier.log("[Sample App]", "Interstitial loaded");

                    // Add a delay for visual effect
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            mCurrentState = getNextLoadingState(mCurrentState);
                            updateLayoutByState(mCurrentState);
                        }}, 500
                    );
                }

                @Override
                public void onAdNoBid() {
                    Appier.log("[Sample App]", "Interstitial ad returns no bid");
                    mCurrentState = getNextLoadingState(mCurrentState);
                    updateLayoutByState(mCurrentState);
                }

                @Override
                public void onAdLoadFail(AppierError appierError) {
                    Appier.log("[Sample App]", "Interstitial load failed");
                    mCurrentState = getNextLoadingState(mCurrentState);
                    updateLayoutByState(mCurrentState);
                }

                @Override
                public void onShown() {
                    Appier.log("[Sample App]", "Interstitial shown");
                    mCurrentState = getNextLoadingState(mCurrentState);
                    updateLayoutByState(mCurrentState);
                }

                @Override
                public void onShowFail(AppierError appierError) {
                    Appier.log("[Sample App]", "Interstitial show failed with error: " + appierError);
                    mCurrentState = getNextLoadingState(mCurrentState);
                    updateLayoutByState(mCurrentState);
                }

                @Override
                public void onDismiss() {
                    Appier.log("[Sample App]", "Interstitial dismissed");
                    mCurrentState = STATE_UNLOADED;
                    updateLayoutByState(mCurrentState);
                }
            });

            mAppierInterstitialAd.setAdDimension(320, 480);
            mAppierInterstitialAd.setZoneId("6242");

            /*
             * (Optional) Set Appier Targeting
             * Set targeting could bring more precise ads, which may increase revenue for App developers
             */
            mAppierInterstitialAd.setYob(2001);
            mAppierInterstitialAd.setGender(AppierTargeting.Gender.MALE);
            mAppierInterstitialAd.addKeyword("interest", "sports");

        }
    }
}
