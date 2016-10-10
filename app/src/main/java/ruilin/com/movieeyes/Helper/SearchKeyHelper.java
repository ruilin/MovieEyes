package ruilin.com.movieeyes.Helper;

import com.orm.SugarRecord;

import java.util.ArrayList;
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
        SearchRecordDb record = new SearchRecordDb(key, null);
        if (mList != null) {
            mList.add(record);
        }
        record.save();
    }

    public List<SearchRecordDb> getList() {
        if (mList == null) {
            mList = SugarRecord.listAll(SearchRecordDb.class);
        }
        return mList;
    }

    public List<String> genStringList() {
        ArrayList<String> list = new ArrayList<>();
        List<SearchRecordDb> mainList = getList();
        for (SearchRecordDb item : mainList) {
            list.add(item.getKey());
        }
        return list;
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