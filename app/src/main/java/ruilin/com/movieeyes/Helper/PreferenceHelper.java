package ruilin.com.movieeyes.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import ruilin.com.movieeyes.base.MovieApplication;

/**
 * Created by Ruilin on 2016/9/21.
 */
public class PreferenceHelper {
    public static final String PREFERENCE_NAME = "saveInfo";
    public static final String KEY_SEARCH_KEY = "KEY_SEARCH_KEY";

    private static PreferenceHelper mInstance;
    private SharedPreferences mPreference;
    private SharedPreferences.Editor mEditor;

    private PreferenceHelper(Context ctx) {
        mPreference = ctx.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        mEditor = mPreference.edit();
    }

    public static PreferenceHelper getInstance() {
        if (mInstance == null) {
            mInstance = new PreferenceHelper(MovieApplication.getInstance());
        }
        return mInstance;
    }

    public void saveSearchKeys(String keys) {
        mEditor.putString(KEY_SEARCH_KEY, keys);
        mEditor.commit();
    }

    public String getSearchKeys() {
        return mPreference.getString(KEY_SEARCH_KEY, "");
    }

    public void clearSearchKeys() {
        mEditor.remove(KEY_SEARCH_KEY);
        mEditor.commit();
    }
}
