package com.appier.android.sample.activity.sdk;

import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.R;
import com.appier.android.sample.fragment.BaseFragment;


public class InterstitialActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load interstitial fragment
        InterstitialFragment interstitialFragment = InterstitialFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame, interstitialFragment);
        fragmentTransaction.commit();

    }

    public static class InterstitialFragment extends BaseFragment {

        static final int STATE_UNLOAD = 0;
        static final int STATE_LOADING = 1;
        static final int STATE_LOADED = 2;

        ProgressBar mProgressLoading;
        Button mButtonLoad;
        ImageView mImageSteps;
        TextView mTextIndicator;

        int state = 0;

        public static InterstitialFragment newInstance() {
            InterstitialFragment fragment = new InterstitialFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_interstitial_content, container, false);
            // Context context;
            // context = getActivity();

            mProgressLoading = view.findViewById(R.id.progress_loading);
            mImageSteps = view.findViewById(R.id.img_step_progress);
            mButtonLoad = view.findViewById(R.id.button_load);
            mTextIndicator = view.findViewById(R.id.text_step_indicator);

            mButtonLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("[Sample App]", "Load Clicked");
                    state += 1;
                    updateLayoutByState(state % 3);  // 3 states
                }
            });

            return view;
        }

        private void updateLayoutByState(int state) {
            if (state == STATE_UNLOAD) {
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

        @Override
        protected void onViewVisible(View view) {

        }
    }
}
