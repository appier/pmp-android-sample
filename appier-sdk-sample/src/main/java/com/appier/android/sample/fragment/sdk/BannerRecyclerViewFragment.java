package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appier.ads.AppierRecyclerAdapter;
import com.appier.ads.common.Dimension;
import com.appier.android.sample.R;
import com.appier.android.sample.common.MyRecyclerViewAdapter;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierAdsHelper;

import java.util.Arrays;

public class BannerRecyclerViewFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private AppierRecyclerAdapter mAppierRecyclerAdapter;

    public BannerRecyclerViewFragment() {}

    public static BannerRecyclerViewFragment newInstance() {
        return new BannerRecyclerViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_recycler_view, container, false);
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

        items = new String[]{"", "", ""};
        MyRecyclerViewAdapter recyclerAdapter = new MyRecyclerViewAdapter(context, Arrays.asList(items));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAppierRecyclerAdapter = new AppierRecyclerAdapter(recyclerAdapter);
        mRecyclerView.setAdapter(mAppierRecyclerAdapter);

        AppierAdsHelper.insertAppierBannerToRecyclerView(context, mAppierRecyclerAdapter, 1,
                getResources().getString(R.string.zone_300x250), 300, 250
        );
    }

}
