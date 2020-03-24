package com.appier.android.sample;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SampleNavigationFragment extends Fragment {
    private static final String ARG_POSITION = "position";

    private String mPosition;

    public SampleNavigationFragment() {}

    public static SampleNavigationFragment newInstance(int position) {
        SampleNavigationFragment fragment = new SampleNavigationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getString(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sample_navigation, container, false);
    }
}
