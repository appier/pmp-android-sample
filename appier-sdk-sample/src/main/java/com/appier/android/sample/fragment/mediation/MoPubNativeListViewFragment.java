package com.appier.android.sample.fragment.mediation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.appier.android.sample.R;
import com.appier.android.sample.common.MyListViewAdapter;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.android.sample.helper.MoPubMediationNativeHelper;

import java.util.Arrays;

public class MoPubNativeListViewFragment extends BaseFragment {

    private ListView mListView;

    public MoPubNativeListViewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * MoPubAdAdapter doesn't support lifecycle control,
         * so enableErrorHandling() only invokes initial render and doesn't handle any error for this sample.
         */
        enableErrorHandling();
    }

    @Override
    public View onCreateDemoView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_list_view, container, false);
        mListView = view.findViewById(R.id.list);
        return view;
    }

    @Override
    protected void onViewVisible(View view) {
        Context context = getActivity();
        String[] items = new String[] {"", "", "", "", "", "", "", "", "", ""};

        MyListViewAdapter myListViewAdapter = new MyListViewAdapter(context, Arrays.asList(items));

        MoPubMediationNativeHelper.insertMoPubNativeListView(
                context, myListViewAdapter, mListView, getResources().getString(R.string.mopub_adunit_native), R.layout.template_native_ad_compact_1
        );

    }

}
