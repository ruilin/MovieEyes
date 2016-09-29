package ruilin.com.movieeyes.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import static anetwork.channel.http.NetworkSdkSetting.context;

/**
 * Created by Ruilin on 2016/9/26.
 */

public class BaseActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(context).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
