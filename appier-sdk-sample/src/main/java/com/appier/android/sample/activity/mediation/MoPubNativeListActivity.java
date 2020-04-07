package com.appier.android.sample.activity.mediation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appier.ads.Appier;
import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.common.MyListViewAdapter;
import com.appier.android.sample.common.MyRecyclerViewAdapter;
import com.appier.android.sample.common.SectionsPagerAdapter;
import com.appier.android.sample.fragment.BaseFragment;
import com.mopub.nativeads.AppierNativeAdRenderer;
import com.mopub.nativeads.MoPubAdAdapter;
import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.ViewBinder;

import java.util.ArrayList;
import java.util.Arrays;

public class MoPubNativeListActivity extends BaseActivity {
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
        private String[] items = new String[] {"", "", "", "", "", "", "", "", "", ""};

        private ListView mListView;
        private MyListViewAdapter myListViewAdapter;

        private RecyclerView mRecyclerView;
        private MyRecyclerViewAdapter myRecyclerViewAdapter;

        public static NativeListFragment newInstance(int position) {
            NativeListFragment fragment = new NativeListFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_POSITION, position);
            fragment.setArguments(args);
            return fragment;
        }

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
            View view = inflater.inflate(R.layout.fragment_common_list_view, container, false);
            mListView = view.findViewById(R.id.list);
            mRecyclerView = view.findViewById(R.id.recycler);
            // add line between items
            DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.demo_list_divider));
            mRecyclerView.addItemDecoration(divider);
            return view;
        }

        @Override
        protected void onViewVisible(View view) {
            Context context = getActivity();

            ArrayList<String> itemArrayList = new ArrayList<String>();
            itemArrayList.addAll(Arrays.asList(items));

            switch (mPosition) {
                // TODO: refactor position selection to the activity
                // TODO: split ListView and RecyclerView to different segment to make code more readable
                case 0:
                    mRecyclerView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);

                    myListViewAdapter = new MyListViewAdapter(mContext, itemArrayList);
                    insertMoPubNativeListView(myListViewAdapter, mListView, getResources().getString(R.string.mopub_adunit_native));
                    break;

                case 1:
                    mListView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    myRecyclerViewAdapter = new MyRecyclerViewAdapter(mContext, itemArrayList);

                    insertMoPubNativeRecyclerView(myRecyclerViewAdapter, mRecyclerView, getResources().getString(R.string.mopub_adunit_native));

                    break;
            }

        }

        /*
         * A helper to create Native Ad and insert into specific position when the ad is loaded
         */
        private void insertMoPubNativeListView(ArrayAdapter<String> adapter, ListView listView, String adunitId) {

            int layoutId;
            if (mPosition == 0) {
                layoutId = R.layout.template_native_ad_compact_1;
            } else {
                layoutId = R.layout.template_native_ad_compact_2;
            }

            ViewBinder viewBinder = new ViewBinder.Builder(layoutId)
                    .mainImageId(R.id.native_main_image)
                    .iconImageId(R.id.native_icon_image)
                    .titleId(R.id.native_title)
                    .textId(R.id.native_text)
                    .callToActionId(R.id.native_cta)
                    .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                    .build();

            AppierNativeAdRenderer appierNativeAdRenderer = new AppierNativeAdRenderer(viewBinder);
            MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

            MoPubAdAdapter moPubAdAdapter = new MoPubAdAdapter((Activity) mContext, adapter);
            moPubAdAdapter.registerAdRenderer(appierNativeAdRenderer);
            moPubAdAdapter.registerAdRenderer(moPubStaticNativeAdRenderer);

            listView.setAdapter(moPubAdAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Appier.log("[Sample App]", "List item", items[(int)id]);
                }
            });

            moPubAdAdapter.loadAds(adunitId);
        }

        /*
         * A helper to create Native Ad and insert into specific position when the ad is loaded
         */
        private void insertMoPubNativeRecyclerView(MyRecyclerViewAdapter adapter, RecyclerView recyclerView, String adunitId) {

            int layoutId;
            if (mPosition == 0) {
                layoutId = R.layout.template_native_ad_compact_1;
            } else {
                layoutId = R.layout.template_native_ad_compact_2;
            }

            ViewBinder viewBinder = new ViewBinder.Builder(layoutId)
                    .mainImageId(R.id.native_main_image)
                    .iconImageId(R.id.native_icon_image)
                    .titleId(R.id.native_title)
                    .textId(R.id.native_text)
                    .callToActionId(R.id.native_cta)
                    .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                    .build();

            AppierNativeAdRenderer appierNativeAdRenderer = new AppierNativeAdRenderer(viewBinder);
            MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

            MoPubRecyclerAdapter moPubAdAdapter = new MoPubRecyclerAdapter((Activity)mContext, adapter);
            moPubAdAdapter.registerAdRenderer(appierNativeAdRenderer);
            moPubAdAdapter.registerAdRenderer(moPubStaticNativeAdRenderer);

            recyclerView.setAdapter(moPubAdAdapter);

            moPubAdAdapter.loadAds(adunitId);
        }

    }
}
