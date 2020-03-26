package com.appier.android.sample.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    private boolean isStarted;
    private boolean isVisible;

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if (isVisible) {
            onViewVisible(getView());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isStarted = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible && isStarted) {
            onViewVisible(getView());
        }
    }

    protected abstract void onViewVisible(View view);
}
