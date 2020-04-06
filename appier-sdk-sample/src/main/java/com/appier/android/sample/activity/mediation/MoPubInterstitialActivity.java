package com.appier.android.sample.activity.mediation;

import android.app.Activity;
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
import android.widget.Toast;

import com.appier.ads.Appier;
import com.appier.ads.AppierError;
import com.appier.ads.AppierInterstitialAd;
import com.appier.ads.common.AppierDataKeys;
import com.appier.ads.common.AppierTargeting;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.fragment.BaseFragment;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.util.HashMap;
import java.util.Map;


public class MoPubInterstitialActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load interstitial fragment
        addFragment(InterstitialFragment.newInstance());
    }

    public static class InterstitialFragment extends BaseFragment implements MoPubInterstitial.InterstitialAdListener {

        static final int STATE_UNLOADED = 0;
        static final int STATE_LOADING = 1;
        static final int STATE_LOADED = 2;

        private static int AD_WIDTH = 320;
        private static int AD_HEIGHT = 480;

        ProgressBar mProgressLoading, mProgressStepsArt;
        Button mButtonLoad;
        ImageView mImageStepsArt, mImageSteps;
        TextView mTextIndicator;

        private int mCurrentState = STATE_UNLOADED;
        private MoPubInterstitial mInterstitial;
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
            mProgressStepsArt = view.findViewById(R.id.progress_steps_art);
            mImageStepsArt = view.findViewById(R.id.img_steps_art);
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
                        createMoPubInterstitial(mContext);
                        mInterstitial.load();

                        mCurrentState = getNextLoadingState(mCurrentState);
                        updateLayoutByState(mCurrentState);

                    } else if (mCurrentState == STATE_LOADED) {
                        Appier.log("[Sample App]", "====== show Appier Interstitial ======");
                        if (mInterstitial.isReady()) {
                            mInterstitial.show();
                        }
                    }

                }
            });

            return view;
        }

        private void updateLayoutByState(int state) {
            if (state == STATE_UNLOADED) {
                mProgressLoading.setVisibility(View.INVISIBLE);
                mProgressStepsArt.setVisibility(View.INVISIBLE);
                mTextIndicator.setText("Step 1: Click load and load the ad.");
                mTextIndicator.setTextColor(getResources().getColor(R.color.colorTextDefault));
                mButtonLoad.setText("LOAD");
                mImageStepsArt.setImageResource(R.drawable.illustration_interstitial_step0_art);
                mImageSteps.setImageResource(R.drawable.illustration_interstitial_step0);
            } else if (state == STATE_LOADING) {
                mProgressLoading.setVisibility(View.VISIBLE);
                mProgressStepsArt.setVisibility(View.VISIBLE);
                mTextIndicator.setText("Step 1: Click load and load the ad.");
                mTextIndicator.setTextColor(getResources().getColor(R.color.colorTextFaded));
                mButtonLoad.setText("");
                mImageStepsArt.setImageResource(R.drawable.illustration_interstitial_step1_art);
                mImageSteps.setImageResource(R.drawable.illustration_interstitial_step1);
            } else if (state == STATE_LOADED) {
                mProgressLoading.setVisibility(View.INVISIBLE);
                mProgressStepsArt.setVisibility(View.INVISIBLE);
                mTextIndicator.setText("Step 2: Click show to see the ad.");
                mTextIndicator.setTextColor(getResources().getColor(R.color.colorTextDefault));
                mButtonLoad.setText("SHOW");
                mImageStepsArt.setImageResource(R.drawable.illustration_interstitial_step2_art);
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

        private void createMoPubInterstitial(Context context) {
            Map<String, Object> localExtras = new HashMap<>();
            localExtras.put(AppierDataKeys.AD_WIDTH_LOCAL, AD_WIDTH);
            localExtras.put(AppierDataKeys.AD_HEIGHT_LOCAL, AD_HEIGHT);

            mInterstitial = new MoPubInterstitial((Activity) mContext, getString(R.string.mopub_adunit_interstitial));
            mInterstitial.setLocalExtras(localExtras);
            mInterstitial.setInterstitialAdListener(this);
            Appier.log("[Sample App]", "====== make request ======");
        }

        @Override
        public void onInterstitialLoaded(MoPubInterstitial interstitial) {
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
        public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
            Appier.log("[Sample App]", "Interstitial load failed");
            mCurrentState = getNextLoadingState(mCurrentState);
            updateLayoutByState(mCurrentState);
        }

        @Override
        public void onInterstitialShown(MoPubInterstitial interstitial) {
            Appier.log("[Sample App]", "Interstitial shown");
            mCurrentState = getNextLoadingState(mCurrentState);
            updateLayoutByState(mCurrentState);
        }

        @Override
        public void onInterstitialClicked(MoPubInterstitial interstitial) {
            Appier.log("[Sample App]", "Interstitial clicked");
        }

        @Override
        public void onInterstitialDismissed(MoPubInterstitial interstitial) {
            Appier.log("[Sample App]", "Interstitial dismissed");
            mCurrentState = STATE_UNLOADED;
            updateLayoutByState(mCurrentState);
        }
    }
}
