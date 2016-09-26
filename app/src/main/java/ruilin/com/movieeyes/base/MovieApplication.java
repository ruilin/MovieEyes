package ruilin.com.movieeyes.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.yolanda.nohttp.NoHttp;

import ruilin.com.movieeyes.Helper.DeviceHelper;

/**
 * Created by Ruilin on 2016/9/13.
 */
public class MovieApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static MovieApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        NoHttp.initialize(this);

//        MobclickAgent.setScenarioType(this, EScenarioType.E_UM_NORMAL);

        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, "57e8915267e58eccde000122", "10000", MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent. startWithConfigure(config);
    }

    public static MovieApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
