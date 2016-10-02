package ruilin.com.movieeyes.db.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.io.Serializable;
import java.util.Date;

import ruilin.com.movieeyes.util.DateUtil;

/**
 * Created by ruilin on 16/10/1.
 */

@Table
public class SearchRecordDb extends SugarRecord implements Serializable {

    @Unique
    private String key;

    private String time;

    private String url;

    public SearchRecordDb(){}

    public SearchRecordDb(String key, String url) {
        this.key = key;
        this.url = url;
        setTime(System.currentTimeMillis());
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setTime(long time) {
        this.time = DateUtil.timeToDate(time);
    }

    public String getTime() {
        return time;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
