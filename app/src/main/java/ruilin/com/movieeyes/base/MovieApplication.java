package ruilin.com.movieeyes.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.orm.SugarContext;

import net.youmi.android.AdManager;

import ruilin.com.movieeyes.Helper.AdHelper;
import ruilin.com.movieeyes.Helper.UMHelper;

/**
 * Created by Ruilin on 2016/9/13.
 */
public class MovieApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static MovieApplication mInstance;

    public static MovieApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        SugarContext.init(this);
//        NoHttp.initialize(this);

//        MobclickAgent.setScenarioType(this, EScenarioType.E_UM_NORMAL);

        UMHelper.initUMStatistics(this);
        UMHelper.initUMPush(this);
        AdHelper.initYoumi(this);

    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
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
