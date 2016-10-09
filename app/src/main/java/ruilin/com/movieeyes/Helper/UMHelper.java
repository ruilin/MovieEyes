package ruilin.com.movieeyes.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.HashMap;
import java.util.Map;

import ruilin.com.movieeyes.BuildConfig;
import ruilin.com.movieeyes.activity.MainActivity;
import ruilin.com.movieeyes.db.bean.SearchRecordDb;

import static anetwork.channel.http.NetworkSdkSetting.context;
import static ruilin.com.movieeyes.Helper.PreferenceHelper.KEY_SEARCH_KEY;

/**
 * Created by Ruilin on 2016/9/26.
 */

public class UMHelper {
    public static final String KEY_SEARCH_KEY = "search_key";
    public static final String KEY_SEARCH_BUTTON = "search_button";

    /* 统计 */
    public static void initUMStatistics(Context context) {
        String channel = DeviceHelper.getAppMetaData(context, "CHANNEL_NAME");
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(context, "57e8915267e58eccde000122", (channel != null) ? channel : "10000", MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.startWithConfigure(config);
    }

    public static void onSearchKey(Context context, String key) {
        Map<String, String> hash = new HashMap<>();
        hash.put("key", key);
        MobclickAgent.onEventValue(context, KEY_SEARCH_KEY, hash, 0);
    }

    public static void onSearchButton(Context context) {
        MobclickAgent.onEvent(context, KEY_SEARCH_BUTTON);
    }

    public static void reportError(Context context, String error) {
        MobclickAgent.reportError(context, error);
    }

    /* 推送 */
    public static void initUMPush(Context context) {
        final PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.setDebugMode(BuildConfig.DEBUG);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.i("xxx", "xxxxxxxxxxxxxx >> "+deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
//                Log.e("xxx", "xxxxxxxxxxxxxxxxx >> "+s+" "+s1);
            }
        });

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(final Context context, final UMessage msg) {
                String key = msg.extra.get("search_key");
                SearchRecordDb sKey = key != null ? new SearchRecordDb(key, "") : null;
                MainActivity.start(context, sKey);
                DialogHelper.showTips(context, msg.title, msg.custom);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }
    public static void setPushStart(Context context) {
        PushAgent agent = PushAgent.getInstance(context);
        if (agent != null) {
            agent.onAppStart();
        }
    }

    public static void setPushResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void setPushPause(Context context) {
        MobclickAgent.onPause(context);
    }
}
