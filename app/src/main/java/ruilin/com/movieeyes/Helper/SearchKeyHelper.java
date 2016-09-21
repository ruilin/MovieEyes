package ruilin.com.movieeyes.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ruilin on 2016/9/21.
 */
public class SearchKeyHelper {
    private static final String FRAG = "#";
    private static SearchKeyHelper mInstance = null;
    private LinkedList<String> mKeys = null;

    private SearchKeyHelper() {
    }

    public static SearchKeyHelper getInstance() {
        if (mInstance == null) {
            mInstance = new SearchKeyHelper();
        }
        return mInstance;
    }

    public void add(String key) {
        List<String> keys = getList();
        if (keys.size() >= 50) {
            keys.remove(0);
        }
        keys.add(keys.size(), key);
        StringBuffer sb = new StringBuffer();
        for (String item : keys) {
            sb.append(item);
            sb.append(FRAG);
        }
        PreferenceHelper.getInstance().saveSearchKeys(sb.toString());
    }

    public List<String> getList() {
        if (mKeys == null) {
            String keyString = PreferenceHelper.getInstance().getSearchKeys();
            mKeys = new LinkedList<>(Arrays.asList(keyString.split(FRAG)));
        }
        return mKeys;
    }

    public void clear() {
        mKeys.clear();
        PreferenceHelper.getInstance().clearSearchKeys();
    }
}
