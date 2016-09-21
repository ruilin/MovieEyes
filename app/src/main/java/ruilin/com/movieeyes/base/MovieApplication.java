package ruilin.com.movieeyes.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.yolanda.nohttp.NoHttp;

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
