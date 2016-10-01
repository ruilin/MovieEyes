package ruilin.com.movieeyes.Helper;

import android.database.sqlite.SQLiteException;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import ruilin.com.movieeyes.db.bean.SearchRecordDb;

/**
 * Created by Ruilin on 2016/9/21.
 */
public class SearchKeyHelper {

    private static SearchKeyHelper mInstance = null;
    private List<SearchRecordDb> mList;

    private SearchKeyHelper() {
    }

    public static SearchKeyHelper getInstance() {
        if (mInstance == null) {
            mInstance = new SearchKeyHelper();
        }
        return mInstance;
    }

    public void add(String key) {
        SearchRecordDb record = new SearchRecordDb(key, null, System.currentTimeMillis());
        if (mList != null) {
            mList.add(record);
        }
        record.save();
    }

    public List<SearchRecordDb> getList() {
        if (mList == null) {
            mList = new ArrayList<>();
            try {
                mList.addAll(SugarRecord.listAll(SearchRecordDb.class));
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
        return mList;
    }

    public boolean delete(SearchRecordDb item) {
        if (mList != null) {
            mList.remove(item);
        }
        return SugarRecord.delete(item);
    }

    public void clear() {
        if (mList != null) {
            mList.clear();
            mList = null;
        }
        SugarRecord.deleteAll(SearchRecordDb.class);
    }
}