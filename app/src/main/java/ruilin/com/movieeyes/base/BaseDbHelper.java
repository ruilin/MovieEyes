package ruilin.com.movieeyes.base;

import com.orm.SugarRecord;

import java.util.List;

import ruilin.com.movieeyes.Helper.SearchKeyHelper;

/**
 * Created by ruilin on 16/10/2.
 */

//public class BaseDbHelper {
//
//    private static SearchKeyHelper mInstance = null;
//    private List<? extends SugarRecord> mList;
//    private Class<? extends SugarRecord> mType;
//
//    public BaseDbHelper(Class<? extends SugarRecord> type) {
//        mType = type;
//    }
//    public <T extends SugarRecord>void add(T item) {
//        if (mList != null) {
//            mList.add(item);
//        }
//        item.save();
//    }
//
//    public List<? extends SugarRecord> getList() {
//        if (mList == null) {
//            mList = SugarRecord.listAll(mType);
//        }
//        return mList;
//    }
//
//    public boolean delete(Class<SugarRecord> item) {
//        if (mList != null) {
//            mList.remove(item);
//        }
//        return SugarRecord.delete(item);
//    }
//
//    public void clear() {
//        if (mList != null) {
//            mList.clear();
//            mList = null;
//        }
//        SugarRecord.deleteAll(mType);
//    }
//}
