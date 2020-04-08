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
import com.appier.android.sample.common.MyRecyclerViewItemDecoration;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierNativeHelper;

import java.util.Arrays;

public class NativeRecyclerViewFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private AppierRecyclerAdapter mAppierRecyclerAdapter;

    public NativeRecyclerViewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_recycler_view, container, false);
        mRecyclerView = view.findViewById(R.id.recycler);

        // add space between items
        mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(Dimension.dipsToIntPixels(12, getContext())));

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

        AppierNativeHelper.insertAppierNativeToRecyclerView(context, mAppierRecyclerAdapter, 1,
                getResources().getString(R.string.zone_native), R.layout.template_native_ad_compact_2
        );
    }

}
