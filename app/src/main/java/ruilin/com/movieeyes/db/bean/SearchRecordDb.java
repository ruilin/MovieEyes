package ruilin.com.movieeyes.db.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.io.Serializable;

/**
 * Created by ruilin on 16/10/1.
 */

@Table
public class SearchRecordDb extends SugarRecord implements Serializable {

    @Unique
    private String key;

    private long time;

    private String url;

    public SearchRecordDb(){}

    public SearchRecordDb(String key, String url, long time) {
        this.key = key;
        this.url = url;
        this.time = time;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
