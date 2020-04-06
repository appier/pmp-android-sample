package com.appier.android.sample.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public BaseFragment() {}

    /*
     * The default FragmentPagerAdapter behavior `BEHAVIOR_SET_USER_VISIBLE_HINT` is deprecated.
     * We set it to `BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT` manually.
     * So the `onResume()` life cycle means `onViewVisible`
     */
    @Override
    public void onResume() {
        super.onResume();
        onViewVisible(getView());
    }

    protected abstract void onViewVisible(View view);
}
