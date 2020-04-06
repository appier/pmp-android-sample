package com.appier.android.sample.activity.sdk;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appier.ads.Appier;
import com.appier.ads.AppierAdAdapter;
import com.appier.ads.AppierError;
import com.appier.ads.AppierNativeAd;
import com.appier.ads.AppierNativeViewBinder;
import com.appier.ads.AppierRecyclerAdapter;
import com.appier.ads.common.Dimension;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.MyListViewAdapter;
import com.appier.android.sample.common.MyRecyclerViewAdapter;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.BaseFragment;

import java.util.Arrays;

public class NativeListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTabbedViewPager(new SectionsPagerAdapter(getSupportFragmentManager(), new String[] {"ListView", "RecyclerView"}) {
            @Override
            public Fragment getItem(int position) {
                return NativeListFragment.newInstance(position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class NativeListFragment extends BaseFragment {
        private static final String ARG_POSITION = "position";

        private Context mContext;
        private int mPosition;

        private ListView mListView;
        private AppierAdAdapter mAppierAdAdapter;

        private RecyclerView mRecyclerView;
        private AppierRecyclerAdapter mAppierRecyclerAdapter;

        public static NativeListFragment newInstance(int position) {
            NativeListFragment fragment = new NativeListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_POSITION, position);
            fragment.setArguments(args);
            return fragment;
        }

        public NativeListFragment() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mContext = getActivity();
            if (getArguments() != null) {
                mPosition = getArguments().getInt(ARG_POSITION);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_common_list, container, false);
            mListView = view.findViewById(R.id.list);
            mRecyclerView = view.findViewById(R.id.recycler);

            // add space between items
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int space = Dimension.dipsToIntPixels(12, getContext());
                    outRect.left = space;
                    outRect.right = space;
                    outRect.bottom = space;

                    // Add top margin only for the first item to avoid double space between items
                    if (parent.getChildPosition(view) == 0)
                        outRect.top = space;
                }
            });
            return view;
        }

        @Override
        protected void onViewVisible(View view) {
            Context context = getActivity();
            String[] items;
            switch (mPosition) {
                case 0:
                    mRecyclerView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);

                    items = new String[] {"", "", "", "", "", "", "", "", "", ""};
                    ArrayAdapter<String> arrayAdapter = new MyListViewAdapter(context, Arrays.asList(items));
                    mAppierAdAdapter = new AppierAdAdapter(arrayAdapter);
                    mListView.setAdapter(mAppierAdAdapter);

                    insertNativeAdUnit(2, getResources().getString(R.string.zone_native));
                    break;

                case 1:
                    mListView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    items = new String[]{"", "", ""};
                    MyRecyclerViewAdapter recyclerAdapter = new MyRecyclerViewAdapter(context, Arrays.asList(items));
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    mAppierRecyclerAdapter = new AppierRecyclerAdapter(recyclerAdapter);
                    mRecyclerView.setAdapter(mAppierRecyclerAdapter);

                    insertNativeAdUnit(1, getResources().getString(R.string.zone_native));
                    break;
            }
        }

        /*
         * A helper to create Native Ad and insert into specific position when the ad is loaded
         */
        private void insertNativeAdUnit(final int insertPosition, String zoneId) {

            int layoutId;
            if (mPosition == 0) {
                layoutId = R.layout.template_native_ad_compact_1;
            } else {
                layoutId = R.layout.template_native_ad_compact_2;
            }

            Appier.setTestMode(true);

            AppierNativeViewBinder appierNativeViewBinder = new AppierNativeViewBinder.Builder(layoutId)
                    .mainImageId(R.id.native_main_image)
                    .iconImageId(R.id.native_icon_image)
                    .titleId(R.id.native_title)
                    .textId(R.id.native_text)
                    .callToActionId(R.id.native_cta)
                    .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                    .build();

            AppierNativeAd appierNativeAd = new AppierNativeAd(mContext, new AppierNativeAd.EventListener() {
                @Override
                public void onAdLoaded(AppierNativeAd appierNativeAd) {

                    Appier.log("[Sample App] onAdLoaded()");

                    try {
                        if (mPosition == 0) {
                            mAppierAdAdapter.insertAd(insertPosition, appierNativeAd);
                        } else if (mPosition == 1) {
                            mAppierRecyclerAdapter.insertAd(insertPosition, appierNativeAd);
                        }
                    } catch (Exception e) {
                        Appier.log("[Sample App] Fail to insert ad into list. Maybe the position is out of bound or is already used.");
                    }

                }

                @Override
                public void onAdNoBid(AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App]", "[Native]", "onAdNoBid()");
                }

                @Override
                public void onAdLoadFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App]", "[Native]", "onAdLoadFail()", appierError.toString());
                }

                @Override
                public void onAdShown(AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App]", "[Native]", "onAdShown()");
                }

                @Override
                public void onImpressionRecorded(AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App]", "[Native]", "onImpressionRecorded()");
                }

                @Override
                public void onImpressionRecordFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App]", "[Native]", "onImpressionRecordFail()", appierError.toString());
                }

                @Override
                public void onAdClick(AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App]", "[Native]", "onAdClick()");
                }

                @Override
                public void onAdClickFail(AppierError appierError, AppierNativeAd appierNativeAd) {
                    Appier.log("[Sample App]", "[Native]", "onAdClickFail()");
                }
            });
            appierNativeAd.setViewBinder(appierNativeViewBinder);
            appierNativeAd.setZoneId(zoneId);

            Appier.log("[Sample App]", "====== load Appier Native ======");
            appierNativeAd.loadAd();
        }

    }
}
