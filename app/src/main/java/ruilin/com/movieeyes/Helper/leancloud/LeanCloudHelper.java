package ruilin.com.movieeyes.Helper.leancloud;

import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

/**
 * Created by Ruilin on 2016/10/13.
 */

public class LeanCloudHelper {

    public static void init(Context context) {
        AVOSCloud.initialize(context, "LgdtYnlJX57muSpdnUC4yP6N-gzGzoHsz", "8BSkiykj4vLlHq3xAkQ9jkk7");
//        initPush(context);

    }

    public static void selectHotTag(FindCallback<AVObject> callback) {
        AVQuery<AVObject> query = new AVQuery<>("HotTag");
//        query.whereEqualTo("priority", 0);
        query.findInBackground(callback);
    }

    //跟踪统计应用的打开情况
//    public static void setStatistics(Activity activity) {
//        AVAnalytics.trackAppOpened(activity.getIntent());
//
//        AVObject testObject = new AVObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
//    }
//
//    public static void initPush(Context context) {
//        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
//            public void done(AVException e) {
//                if (e == null) {
//                    // 保存成功
//                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
//                    // 关联  installationId 到用户表等操作……
//                } else {
//                    // 保存失败，输出错误信息
//                }
//            }
//        });
//        // 开启服务，设置默认打开的Activity
//        PushService.setDefaultPushCallback(context, MainActivity.class);
//    }
//
//    public static void trackAppOpened(MainActivity activity) {
//        AVAnalytics.trackAppOpened(activity.getIntent());
//    }
}
