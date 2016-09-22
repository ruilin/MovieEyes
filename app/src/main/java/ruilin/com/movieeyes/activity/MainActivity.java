package ruilin.com.movieeyes.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import ruilin.com.movieeyes.Helper.SearchKeyHelper;
import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.adapter.SearchAdapter;
import ruilin.com.movieeyes.fragment.MovieListFragment;
import ruilin.com.movieeyes.modle.MovieUrl;

/**
 * @author Ruilin
 */
public class MainActivity extends AppCompatActivity implements OnClickListener, MovieListFragment.OnListFragmentInteractionListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String BAIDU_PAN_HOST = "pan.baidu";
    private final static String ZHUAN_PAN_HOST = "http://www.quzhuanpan.com";
    public static final int RESULT_CODE_SUCCESS = 0;
    public static final int RESULT_CODE_ERROR = 1;
    public static final int RESULT_CODE_TIMEOUT = 2;

    // UI references.
    private AutoCompleteTextView mKeyView;
    private View mProgressView;
    private View mContentView;
    private LinearLayout mFragmentLayout;
    private MovieListFragment mMovieFra;
    private TextView tvUrl;
    private ArrayList<MovieUrl> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        Button searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProgressView.getVisibility() == View.VISIBLE) {
                    Log.i(TAG, "cannot operate");
                    return;
                }
                String key = mKeyView.getText().toString();
                if (key.length() == 0) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.main_search_null_tips), Toast.LENGTH_SHORT).show();
                    return;
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                new LoadUrlTask(key).execute();
            }
        });

    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mContentView = findViewById(R.id.fl_content);
        mProgressView = findViewById(R.id.pv_loading);
        mKeyView = (AutoCompleteTextView) findViewById(R.id.key);
        tvUrl = (TextView) findViewById(R.id.tv_tips);
        mFragmentLayout = (LinearLayout) findViewById(R.id.ll_result);

        updateKeyTips();
        mKeyView.setThreshold(1);
        mKeyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* 关闭键盘 */
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        mMovieFra = MovieListFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.ll_result, mMovieFra);
        ft.commit();
    }

    private void updateKeyTips() {
        SearchAdapter adapter = new SearchAdapter<>(MainActivity.this,
                android.R.layout.simple_dropdown_item_1line, SearchKeyHelper.getInstance().getList(),
                SearchAdapter.ALL);//速度优先
        mKeyView.setAdapter(adapter);
    }



//    <div class="search-classic" style="margin-bottom:0px;" id="1892274" typeid="-1">
//        <h4 class="result4">
//          <a title="<span style='color:red'>大话西游</span>三" id="607871276" data="2416252861" data1="1ntF0mEl" style="color:#3244ea" class="source-title" href="https://pan.baidu.com/share/home?uk=2416252861" target="_blank"><span class=""><span style="color:red">大话西游</span>三</span>&nbsp;&nbsp;
//          </a>
//        </h4>
//        <div class="result">文件链接：<a title="<span style='color:red'>大话西游</span>三" class="source-title2" href="https://pan.baidu.com/share/link?uk=2416252861&amp;shareid=607871276" target="_blank"><span style="color:red">大话西游</span>三&nbsp;&nbsp;</a>| 所在位置：<span style="color:#3244ea">度盘&nbsp;&nbsp;</span>|分享者：
//            <a href="https://pan.baidu.com/share/home?uk=2416252861" target="_blank">
//                495211374
//            </a>
//            <a href="https://pan.baidu.com/share/home?uk=2416252861" target="_blank" style="color:#62636B;">
//                他的贡献
//            </a>
//        </div>
//        <div class="result">提示：<span>来自搜索引擎.</span> |分享时间：2015-09-08 15:35</div>
//    </div>

    private int parse(String key) {
        Log.i(TAG, "start loading");
        mMovieList.clear();
        try {
            Document doc = Jsoup.connect(ZHUAN_PAN_HOST + "/source/search.action").data("q", key).get();
//            Elements links = doc.select("a[href]");
            Elements links = doc.select("div[class=search-classic]");
            //注意这里是Elements不是Element。同理getElementById返回Element，getElementsByClass返回时Elements
            for (Element link : links) {
                // 过滤链接
//                String tag = link.text().toString();
//                String url = link.attr("abs:href");
                Elements authorEle = link.select("h4[class=result4]");
                Elements urlEle = authorEle.select("a[href]");
                String url = urlEle.attr("href");
                String tag = authorEle.text();
                if (tag.contains(key)) {
                    MovieUrl movie = new MovieUrl();
                    movie.tag = tag;
                    movie.url = "";
                    if (url.contains(BAIDU_PAN_HOST)) {
                        movie.url = url;
                    } else {
                        /* 载入详情页 */
                        Document subHtml = Jsoup.connect(ZHUAN_PAN_HOST + url).get();
                        Elements subLinks = subHtml.select("li[class=list-group-item]");
                        final String TAG_AUTHOR = "分享人：";
                        final String TAG_DATE = "分享日期：";

                        for (Element subLink : subLinks) {
                            String text = subLink.text();
                            if (text.contains("下载链接")) {
                                Elements baiduLinks = subLink.getElementsByAttribute("href");
                                for (Element baiduLink : baiduLinks) {
                                    String baiduUrl = baiduLink.attr("href");
                                    if (baiduUrl != null && baiduUrl.contains(BAIDU_PAN_HOST)) {
                                        movie.url = baiduUrl;
                                        break;
                                    }
                                }
                            } else if (text.startsWith(TAG_AUTHOR)) {
                                movie.author = text.substring(TAG_AUTHOR.length());
                            } else if (text.startsWith(TAG_DATE)) {
                                movie.date = text.substring(TAG_DATE.length());
                            }
                            movie.print();
                        }
                    }
                    Log.e("links", tag + " ***** " + url);
                    mMovieList.add(movie);
                }
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return RESULT_CODE_TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
            return RESULT_CODE_ERROR;
        }

        return RESULT_CODE_SUCCESS;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mContentView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onListFragmentInteraction(MovieUrl item) {
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.VIEW");
//        Uri content_url = Uri.parse(item.url);
//        intent.setData(content_url);
//        startActivity(intent);

//        PlayerActivity.start(MainActivity.this, "ok", "http://pan.baidu.com/share/link?shareid=1039547194&uk=3943590444&fid=542233410763175");
        WebViewActivity.startForUrl(this, item.url);
    }

    @Override
    public void onMovieListCreated(ArrayList<MovieUrl> list) {
        mMovieList = list;
    }

    /**
     *
     */
    public class LoadUrlTask extends AsyncTask<Void, Void, Integer> {
        String key;

        LoadUrlTask(String key) {
            this.key = key;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return parse(key);
        }

        @Override
        protected void onPostExecute(final Integer resultCode) {
            showProgress(false);
            switch (resultCode) {
                case RESULT_CODE_SUCCESS:
                    Log.i(TAG, "success!!! "+ mMovieList.size());
                    mMovieFra.update();
                    SearchKeyHelper.getInstance().add(key);
                    updateKeyTips();
                    break;
                case RESULT_CODE_TIMEOUT:
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.main_net_timeout_tips), Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CODE_ERROR:
                default:
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.main_net_error_tips), Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }


}

