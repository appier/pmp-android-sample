package com.appier.android.sample.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appier.ads.AppierError;
import com.appier.android.sample.R;

import androidx.fragment.app.Fragment;

public abstract class DemoFlowController {
    Context mContext;
    Fragment mFragment;
    String mRetryTitle = "";
    String mRetryDescription = "";

    public static final int STATE_LOADING = 0;
    public static final int STATE_LOADED = 1;
    public static final int STATE_RETRYABLE = 2;
    int mCurrentState = STATE_LOADING;

    public DemoFlowController(Fragment fragment, Context context) {
        mContext = context;
        mFragment = fragment;
    }

    /*
     * getter and setter
     */
    public int getState() {
        return mCurrentState;
    }

    /*
     * member functions
     */
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mCurrentState == STATE_LOADING || mCurrentState == STATE_LOADED) {
            return createDemoView(inflater, container, savedInstanceState);
        } else {
            return createRetryView(inflater, container, savedInstanceState);
        }
    }

    public View createRetryView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retry, container, false);
        view.findViewById(R.id.button_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateState(STATE_LOADING);
            }
        });
        ((TextView) view.findViewById(R.id.text_title)).setText(mRetryTitle);
        ((TextView) view.findViewById(R.id.text_description)).setText(mRetryDescription);
        return view;
    }

    public void refresh() {
        mFragment
            .getFragmentManager()
            .beginTransaction()
            .detach(mFragment)
            .attach(mFragment)
            .commit();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isDemoAvailable() {
        return mCurrentState == DemoFlowController.STATE_LOADING || mCurrentState == DemoFlowController.STATE_LOADED;
    }

    public boolean shouldRefresh(int nextState) {
        return !(mCurrentState == STATE_LOADING && nextState == STATE_LOADED) && mCurrentState != nextState;
    }

    public void updateState(int nextState) {
        if (!isNetworkAvailable()) {
            nextState = STATE_RETRYABLE;
            mRetryTitle = "No Internet";
            mRetryDescription = "Please check your connection and try again.";
        }
        boolean shouldRefresh = shouldRefresh(nextState);
        mCurrentState = nextState;
        if (shouldRefresh) {
            refresh();
        }
    }

    public void notifyAdBid() {
        mRetryTitle = "";
        mRetryDescription = "";
        updateState(STATE_LOADED);
    }

    public void notifyAdNoBid() {
        mRetryTitle = "No Bid";
        mRetryDescription = "No bidders bid this time. Please try again.";
        updateState(STATE_RETRYABLE);
    }

    public void notifyAdError(AppierError appierError) {
        mRetryTitle = "Unable to Connect to the Server";
        mRetryDescription = "Please try again. If this error keeps happening, please contact Appier.";
        updateState(STATE_RETRYABLE);
    }

    /*
     * abstract member functions
     */
    abstract public View createDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
