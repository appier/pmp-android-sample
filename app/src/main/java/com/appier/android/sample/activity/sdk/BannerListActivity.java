package com.appier.android.sample.activity.sdk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdAdapter;
import com.appier.ads.AppierBannerAd;
import com.appier.ads.AppierError;
import com.appier.ads.AppierRecyclerAdapter;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.MyListViewAdapter;
import com.appier.android.sample.common.MyRecyclerViewAdapter;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.BaseFragment;

import java.util.Arrays;

public class BannerListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTabbedViewPager(new SectionsPagerAdapter(getSupportFragmentManager(), new String[] {"ListView", "RecyclerView"}) {
            @Override
            public Fragment getItem(int position) {
                return DemoFragment.newInstance(position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class DemoFragment extends BaseFragment {
        private static final String ARG_POSITION = "position";

        private int mPosition;
        private String[] items = new String[] {"", "", "", "", "", "", "", "", "", ""};
        private ListView mListView;
        private AppierAdAdapter mAppierAdAdapter;
        private RecyclerView mRecyclerView;
        private AppierRecyclerAdapter mAppierRecyclerAdapter;

        public static DemoFragment newInstance(int position) {
            DemoFragment fragment = new DemoFragment();
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_sdk_banner_list, container, false);
            mListView = view.findViewById(R.id.list);
            mRecyclerView = view.findViewById(R.id.recycler);
            // add line between items
            // mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.demo_list_divider));
            mRecyclerView.addItemDecoration(divider);
            return view;
        }

        @Override
        protected void onViewVisible(View view) {
            Context context = getActivity();
            switch (mPosition) {
                case 0:
                    mRecyclerView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> arrayAdapter = new MyListViewAdapter(context, Arrays.asList(items));
                    mAppierAdAdapter = new AppierAdAdapter(arrayAdapter);
                    mListView.setAdapter(mAppierAdAdapter);
                    break;

                case 1:
                    mListView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    MyRecyclerViewAdapter recyclerAdapter = new MyRecyclerViewAdapter(context, Arrays.asList(items));
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    mAppierRecyclerAdapter = new AppierRecyclerAdapter(recyclerAdapter);
                    mRecyclerView.setAdapter(mAppierRecyclerAdapter);
                    break;
            }
            insertBanner(2, getResources().getString(R.string.zone_320x50), 320, 50);
        }

        private void insertBanner(final int insertPosition, String zoneId, int width, int height) {
            AppierBannerAd appierBannerAd = new AppierBannerAd(getActivity(), new AppierBannerAd.EventListener() {
                @Override
                public void onAdLoaded(AppierBannerAd appierBannerAd) {
                    Appier.log("[Sample App]", "[Banner]", "onAdLoaded()");
                    try {
                        if (mPosition == 0) {
                            mAppierAdAdapter.insertAd(insertPosition, appierBannerAd);
                        } else if (mPosition == 1) {
                            mAppierRecyclerAdapter.insertAd(insertPosition, appierBannerAd);
                        }
                    } catch (Exception e) {
                        Appier.log("[Sample App] Fail to insert ad into list. Maybe the position is out of bound or is already used.");
                    }
                }

                @Override
                public void onAdNoBid(AppierBannerAd appierBannerAd) {
                    Appier.log("[Sample App]", "[Banner]", "onAdNoBid()");
                }

                @Override
                public void onAdLoadFail(AppierError appierError, AppierBannerAd appierBannerAd) {
                    Appier.log("[Sample App]", "[Banner]", "onAdLoadFail()", appierError.toString());
                }

                @Override
                public void onViewClick(AppierBannerAd appierBannerAd) {
                    Appier.log("[Sample App]", "[Banner]", "onViewClick()");
                }
            });
            appierBannerAd.setAdDimension(width, height);
            appierBannerAd.setZoneId(zoneId);

            Appier.log("[Sample App]", "====== load Appier Banner ======");
            appierBannerAd.loadAd();
        }
    }
}
