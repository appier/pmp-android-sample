package com.appier.android.sample.activity.mediation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appier.android.sample.R;
import com.appier.android.sample.activity.BaseActivity;
import com.appier.android.sample.fragment.BaseFragment;
import com.appier.ads.Appier;
import com.appier.ads.common.AppierDataKeys;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.HashMap;
import java.util.Map;

public class MoPubBannerBasicActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new DemoFragment());
    }

    public static class DemoFragment extends BaseFragment {
        private MoPubView mMoPubView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_mediation_mopub_banner_basic, container, false);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (mMoPubView != null) {
                mMoPubView.destroy();
            }
        }

        @Override
        protected void onViewVisible(View view) {
            loadBanner(getResources().getString(R.string.mopub_adunit_banner_300x250), 300, 250);
        }

        private void loadBanner(String adUnitId, int width, int height) {
            Map<String, Object> localExtras = new HashMap<>();
            localExtras.put(AppierDataKeys.AD_WIDTH_LOCAL, width);
            localExtras.put(AppierDataKeys.AD_HEIGHT_LOCAL, height);

            mMoPubView = getView().findViewById(R.id.banner_container_300_250);
            mMoPubView.setLocalExtras(localExtras);
            mMoPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
                // Sent when the banner has successfully retrieved an ad.
                @Override
                public void onBannerLoaded(MoPubView banner) {
                    Appier.log("[Sample App]", "onBannerLoaded()");
                }

                // Sent when the banner has failed to retrieve an ad. You can use the MoPubErrorCode value to diagnose the cause of failure.
                @Override
                public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                    Appier.log("[Sample App]", "onBannerFailed():", errorCode.toString());
                }

                // Sent when the user has tapped on the banner.
                @Override
                public void onBannerClicked(MoPubView banner) {
                    Appier.log("[Sample App]", "onBannerClicked()");
                }

                // Sent when the banner has just taken over the screen.
                @Override
                public void onBannerExpanded(MoPubView banner) {
                    Appier.log("[Sample App]", "onBannerExpanded()");
                }

                // Sent when an expanded banner has collapsed back to its original size.
                @Override
                public void onBannerCollapsed(MoPubView banner) {
                    Appier.log("[Sample App]", "onBannerCollapsed()");
                }
            });
            mMoPubView.setAdUnitId(adUnitId); // Enter your Ad Unit ID from www.mopub.com
            Appier.log("[Sample App]", "====== make request ======");
            mMoPubView.loadAd();
        }
    }
}
