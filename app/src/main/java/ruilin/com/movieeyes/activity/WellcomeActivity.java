package ruilin.com.movieeyes.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.base.BaseActivity;

public class WellcomeActivity extends BaseActivity {

    InterstitialAd mInterstitialAd;
    Button mNewGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);

        mNewGameButton=(Button)findViewById(R.id.bt_skip);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7146181069155443/2988545218");
        requestNewInterstitial();

        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginPlayingGame();
            }
        });

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                beginPlayingGame();
            }

            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });

//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("YOUR_DEVICE_HASH").build();

        mInterstitialAd.loadAd(adRequest);

    }

    private void beginPlayingGame() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
