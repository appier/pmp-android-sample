package com.appier.android.sample.primary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appier.android.sample.R;

public class NavigationFragment extends Fragment {
    private static final String ARG_POSITION = "position";

    private int mPosition;

    public NavigationFragment() {}

    public static NavigationFragment newInstance(int position) {
        NavigationFragment fragment = new NavigationFragment();
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
        View layout = inflater.inflate(R.layout.fragment_primary_navigation, container, false);
        TextView textVersion = layout.findViewById(R.id.text_version);
        if (mPosition == 0) {
            textVersion.setText("Appier SDK version : 1.0.0-rc5");
        } else if (mPosition == 1) {
            textVersion.setText("Mediation SDK version : 1.0.0-rc4");
        }
        return layout;
    }
}
