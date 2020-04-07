package com.appier.android.sample.fragment.sdk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appier.ads.AppierAdAdapter;
import com.appier.android.sample.R;
import com.appier.android.sample.common.MyListViewAdapter;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.AppierNativeHelper;

import java.util.Arrays;

public class NativeListViewFragment extends BaseFragment {

    private ListView mListView;
    private AppierAdAdapter mAppierAdAdapter;

    public NativeListViewFragment() {}

    public static NativeListViewFragment newInstance() {
        return new NativeListViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_list_view, container, false);
        mListView = view.findViewById(R.id.list);
        return view;
    }

    @Override
    protected void onViewVisible(View view) {
        Context context = getActivity();
        String[] items;

        items = new String[] {"", "", "", "", "", "", "", "", "", ""};
        ArrayAdapter<String> arrayAdapter = new MyListViewAdapter(context, Arrays.asList(items));
        mAppierAdAdapter = new AppierAdAdapter(arrayAdapter);
        mListView.setAdapter(mAppierAdAdapter);

        AppierNativeHelper.insertAppierNativeToListView(context, mAppierAdAdapter, 2,
                getResources().getString(R.string.zone_native), R.layout.template_native_ad_compact_1
        );
    }

}
