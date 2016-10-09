package ruilin.com.movieeyes.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ruilin.com.movieeyes.Helper.UMHelper;

/**
 * Created by Ruilin on 2016/9/26.
 */

public class BaseActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UMHelper.setPushStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMHelper.setPushResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        UMHelper.setPushPause(this);
    }
}
