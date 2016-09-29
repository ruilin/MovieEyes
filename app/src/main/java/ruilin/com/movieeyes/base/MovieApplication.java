package ruilin.com.movieeyes.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.yolanda.nohttp.NoHttp;

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
        NoHttp.initialize(this);

//        MobclickAgent.setScenarioType(this, EScenarioType.E_UM_NORMAL);

        initUMStatistics();
        initUMPush();
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

    /* 统计 */
    private void initUMStatistics() {
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, "57e8915267e58eccde000122", "10000", MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent. startWithConfigure(config);
    }
    /* 推送 */
    public void initUMPush() {
        final PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //注册推送服务，每次调用register方法都会回调该接口
                mPushAgent.register(new IUmengRegisterCallback() {

                    @Override
                    public void onSuccess(String deviceToken) {
                        //注册成功会返回device token
                    }

                    @Override
                    public void onFailure(String s, String s1) {

                    }
                });
            }
        }).start();

    }
}
