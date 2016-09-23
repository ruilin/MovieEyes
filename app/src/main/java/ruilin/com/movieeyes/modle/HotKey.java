package ruilin.com.movieeyes.modle;

import ruilin.com.movieeyes.widget.TagView.Tag;

/**
 * Created by Ruilin on 2016/9/23.
 */

public class HotKey extends Tag {
    private String key;
    private String url;

    public void setKey(String key) {
        this.key = key;
        setTitle(key);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }
}
