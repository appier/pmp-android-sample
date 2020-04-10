package com.appier.android.sample.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appier.ads.Appier;
import com.appier.android.sample.R;

public abstract class BaseInterstitialFragment extends BaseFragment {

    protected final int STATE_UNLOADED = 0;
    protected final int STATE_LOADING = 1;
    protected final int STATE_LOADED = 2;

    private Context mContext;

    private int mCurrentState = STATE_UNLOADED;

    private ProgressBar mProgressLoading, mProgressStepsArt;
    private Button mButtonLoad;
    private ImageView mImageStepsArt, mImageSteps;
    private TextView mTextIndicator;

    public BaseInterstitialFragment() {}

    @Override
    protected void onViewVisible(View view) {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_common_interstitial, container, false);
        mContext = getActivity();

        mProgressLoading = view.findViewById(R.id.progress_loading);
        mProgressStepsArt = view.findViewById(R.id.progress_steps_art);
        mImageStepsArt = view.findViewById(R.id.img_steps_art);
        mImageSteps = view.findViewById(R.id.img_step_progress);
        mButtonLoad = view.findViewById(R.id.button_load);
        mTextIndicator = view.findViewById(R.id.text_step_indicator);

        mCurrentState = STATE_UNLOADED;
        mButtonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Load/Show interstitial ads by current state
                 */
                if (mCurrentState == STATE_UNLOADED) {
                    Appier.log("[Sample App]", "====== load Appier Interstitial ======");

                    createInterstitial(mContext);
                    loadInterstitial();

                    mCurrentState = getNextLoadingState(mCurrentState);
                    updateLayoutByState(mCurrentState);

                } else if (mCurrentState == STATE_LOADED) {
                    Appier.log("[Sample App]", "====== show Appier Interstitial ======");

                    showInterstitial();
                }

            }
        });

        return view;
    }

    protected void updateLayoutByState(int state) {
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

    protected int getCurrentState() {
        return mCurrentState;
    }

    protected void setCurrentState(int mCurrentState) {
        this.mCurrentState = mCurrentState;
    }

    protected int getNextLoadingState(int currentState) {
        switch (currentState) {
            case STATE_UNLOADED:
                return STATE_LOADING;
            case STATE_LOADING:
                return STATE_LOADED;
            default:
                return STATE_UNLOADED;
        }
    }

    protected abstract void createInterstitial(Context context);

    protected abstract void loadInterstitial();

    protected abstract void showInterstitial();

}
