package ruilin.com.movieeyes.db.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.io.Serializable;

/**
 * Created by ruilin on 16/10/2.
 */
@Table
public class SearchResultDb extends SugarRecord implements Serializable {
    @Unique
    private String url;
    private String tag;
    private String author;
    private String authorUrl;
    private String date;

    public SearchResultDb() {}
    public SearchResultDb(String url, String tag, String author, String authorUrl, String date) {
        this.url = url;
        this.tag = tag;
        this.author = author;
        this.authorUrl = authorUrl;
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public String getTag() {
        return tag;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public String getDate() {
        return date;
    }
}
