package com.appier.android.sample.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private static final String ARG_TITLE = "title";

    private String mTitle;

    public BaseFragment() {}

    public String getTitle() {
        return mTitle;
    }

    public void setTitleArgs(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        this.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onViewVisible(getView());
    }

    protected abstract void onViewVisible(View view);
}
