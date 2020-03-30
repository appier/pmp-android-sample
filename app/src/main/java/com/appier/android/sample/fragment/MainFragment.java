package com.appier.android.sample.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.activity.sdk.BannerBasicActivity;
import com.appier.android.sample.activity.sdk.BannerFloatingWindowActivity;
import com.appier.android.sample.activity.sdk.BannerListActivity;
import com.appier.android.sample.activity.sdk.InterstitialActivity;
import com.appier.android.sample.secondary.NavigationAdapter;

public class MainFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String ARG_TITLE = "title";

    private int mPosition;
    private String mTitle;

    public MainFragment() {}

    public static MainFragment newInstance(int position, String title) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_primary_navigation, container, false);
        if (mPosition == 0) {
            initializeSDKView(layout);
        } else if (mPosition == 1) {
            initializeMediationView(layout);
        }
        return layout;
    }

    private void initializeSDKView(View layout) {
        initializeNavigationList(
            layout.findViewById(R.id.secondary_nav_interstitial),
            new Pair[] {
                new Pair<>("Interstitial", InterstitialActivity.class)
            }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_banner),
                new Pair[] {
                        new Pair<>("Banner - basic format", BannerBasicActivity.class),
                        new Pair<>("Banner - in a listview", BannerListActivity.class),
                        new Pair<>("Banner - in a floating window", BannerFloatingWindowActivity.class)
                }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_native),
                new Pair[] {
                        new Pair<>("Native - basic format", BaseActivity.class),
                        new Pair<>("Native - in a listview", BaseActivity.class),
                        new Pair<>("Native - in a floating window", BaseActivity.class)
                }
        );
        TextView textVersion = layout.findViewById(R.id.text_version);
        textVersion.setText("Appier SDK version : 1.0.0-rc5");
    }

    private void initializeMediationView(View layout) {
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_interstitial),
                new Pair[] {
                        new Pair<>("Interstitial", BaseActivity.class)
                }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_banner),
                new Pair[] {
                        new Pair<>("Banner - basic format", BaseActivity.class),
                        new Pair<>("Banner - in a listview", BaseActivity.class),
                        new Pair<>("Banner - in a floating window", BaseActivity.class)
                }
        );
        initializeNavigationList(
                layout.findViewById(R.id.secondary_nav_native),
                new Pair[] {
                        new Pair<>("Native - basic format", BaseActivity.class),
                        new Pair<>("Native - in a listview", BaseActivity.class),
                        new Pair<>("Native - in a floating window", BaseActivity.class)
                }
        );
        TextView textVersion = layout.findViewById(R.id.text_version);
        textVersion.setText("Mediation SDK version : 1.0.0-rc4");
    }

    private void initializeNavigationList(View view, Pair<String, Class<?>>[] navigations) {
        ListView listView = (ListView) view;
        final NavigationAdapter navigationAdapter = new NavigationAdapter(getContext());
        for (Pair<String, Class<?>> navigation: navigations) {
            navigationAdapter.add(navigation);
        }
        listView.setAdapter(navigationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int position, long paramLong) {
                Pair<String, Class<?>> item = navigationAdapter.getItem(position);
                Intent intent = new Intent(getContext(), item.second);
                intent.putExtra(BaseActivity.EXTRA_TITLE, item.first);
                intent.putExtra(BaseActivity.EXTRA_SUB_TITLE, mTitle);
                startActivity(intent);
            }
        });
    }
}
