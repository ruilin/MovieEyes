package ruilin.com.movieeyes.modle;

/**
 * Created by Ruilin on 2016/9/20.
 */

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.View;

import java.util.ArrayList;

/**
 * 搜索结果数据
 */
public class SearchResult {
    String msg = "";
    ArrayList<MovieUrl> urls;

    private static SearchResult mIntance;

    private SearchResult() {
        urls = new ArrayList<>();
    }

    public static SearchResult getInstance() {
        if (mIntance == null) {
            mIntance = new SearchResult();
        }
        return mIntance;
    }

    public void clean() {
        urls.clear();
    }

    public void addUrl(MovieUrl movie) {
        urls.add(movie);
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public ArrayList<MovieUrl> getMovieList() {
        return urls;
    }

}