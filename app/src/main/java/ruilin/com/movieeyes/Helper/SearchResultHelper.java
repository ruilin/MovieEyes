package ruilin.com.movieeyes.Helper;

import com.orm.SugarRecord;

import java.util.List;

import ruilin.com.movieeyes.db.bean.SearchResultDb;
import ruilin.com.movieeyes.util.DateUtil;

/**
 * Created by ruilin on 16/10/2.
 */

public class SearchResultHelper {
    private static SearchResultHelper mInstance = null;
    private List<SearchResultDb> mList;

    private SearchResultHelper() {
    }

    public static SearchResultHelper getInstance() {
        if (mInstance == null) {
            mInstance = new SearchResultHelper();
        }
        return mInstance;
    }

    public void add(String url, String tag, String author, String authorUrl) {
        String date = DateUtil.timeToDate(System.currentTimeMillis());
        SearchResultDb record = new SearchResultDb(url, tag, author, authorUrl, date);
        if (mList != null) {
            mList.add(record);
        }
        record.save();
    }

    public void add(SearchResultDb item) {
        if (mList != null) {
            mList.add(item);
        }
        item.save();
    }

    public List<SearchResultDb> getList() {
        if (mList == null) {
            mList = SugarRecord.listAll(SearchResultDb.class);
        }
        return mList;
    }

    public boolean delete(SearchResultDb item) {
        if (mList != null) {
            mList.remove(item);
        }
        return SearchResultDb.delete(item);
    }

    public void clear() {
        if (mList != null) {
            mList.clear();
            mList = null;
        }
        SugarRecord.deleteAll(SearchResultDb.class);
    }

}
