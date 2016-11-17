package ruilin.com.movieeyes.Helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import net.youmi.android.AdManager;
import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;

import ruilin.com.movieeyes.R;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

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

    public static void initYoumi(Context context) {
        AdManager.getInstance(context).init("51e07d0de01cb66f", "e985ad03f56b4dcc", false, true);// BuildConfig.DEBUG
    }
    public static void setYoumi(Activity activity) {
        final View bannerLayout = activity.findViewById(R.id.fl_banner);

        // 获取广告条
        View bannerView = BannerManager.getInstance(activity)
                .getBannerView(new BannerViewListener() {
                    @Override
                    public void onRequestSuccess() {
                        bannerLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSwitchBanner() {
                        bannerLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onRequestFailed() {
                        bannerLayout.setVisibility(View.GONE);
                    }
                });

        // 获取要嵌入广告条的布局
        LinearLayout adLayout = (LinearLayout) activity.findViewById(R.id.ll_banner);
        // 将广告条加入到布局中
        adLayout.addView(bannerView);
    }
}
