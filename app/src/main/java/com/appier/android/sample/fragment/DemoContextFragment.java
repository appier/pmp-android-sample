package com.appier.android.sample.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.appier.android.sample.R;

public class DemoContextFragment extends Fragment {
    private static final String ARG_POSITION = "position";

    private int mPosition;

    public DemoContextFragment() {}

    public static DemoContextFragment newInstance(int position) {
        DemoContextFragment fragment = new DemoContextFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_demo_context, container, false);
        return layout;
    }
}
