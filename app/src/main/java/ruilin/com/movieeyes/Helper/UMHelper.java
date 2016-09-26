package ruilin.com.movieeyes.Helper;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import static ruilin.com.movieeyes.Helper.PreferenceHelper.KEY_SEARCH_KEY;

/**
 * Created by Ruilin on 2016/9/26.
 */

public class UMHelper {
    public static final String KEY_SEARCH_KEY = "search_key";
    public static final String KEY_SEARCH_BUTTON = "search_button";

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
}
