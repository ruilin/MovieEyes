package ruilin.com.movieeyes.Helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;

import ruilin.com.movieeyes.R;

import static anetwork.channel.http.NetworkSdkSetting.context;

/**
 * Created by Ruilin on 2016/10/8.
 */

public class AdHelper {

//    public static void initAdmob(View contentView) {
//        AdView mAdView = (AdView) contentView.findViewById(R.id.adView);
////        mAdView.setAdUnitId(getString(R.string.banner_ad_unit_id));
////        mAdView.setAdSize(AdSize.WIDE_SKYSCRAPER);
//        final View progressBar = contentView.findViewById(R.id.pb_ad);
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                super.onAdFailedToLoad(errorCode);
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//    }

    public static void initYoumi(Activity activity) {
        // 获取广告条
        View bannerView = BannerManager.getInstance(activity)
                .getBannerView(new BannerViewListener() {
                    @Override
                    public void onRequestSuccess() {

                    }

                    @Override
                    public void onSwitchBanner() {

                    }

                    @Override
                    public void onRequestFailed() {

                    }
                });

        // 获取要嵌入广告条的布局
        LinearLayout bannerLayout = (LinearLayout) activity.findViewById(R.id.ll_banner);

        // 将广告条加入到布局中
        bannerLayout.addView(bannerView);
    }
}
