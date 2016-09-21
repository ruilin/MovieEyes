package ruilin.com.movieeyes.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    /**
     * 用来标志请求的what, 类似handler的what一样，这里用来区分请求
     */
    private static final int NOHTTP_WHAT_TEST = 0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mMovieFra = MovieListFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.ll_result, mMovieFra);
        ft.commit();

        Button mEmailSignInButton = (Button) findViewById(R.id.button_search);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
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
//                Request<String> request = NoHttp.createStringRequest("http://www.quzhuanpan.com/source/search.action", RequestMethod.GET);
//                request.add("q", mKeyView.getText().toString());
//                requestQueue.add(NOHTTP_WHAT_TEST, request, onResponseListener);
//                Log.e("url", request.url());
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        parse(mKeyView.getText().toString());
//                    }
//                }).start();
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
    }

    private void updateKeyTips() {
        SearchAdapter adapter = new SearchAdapter<>(MainActivity.this,
                android.R.layout.simple_dropdown_item_1line, SearchKeyHelper.getInstance().getList(),
                SearchAdapter.ALL);//速度优先
        mKeyView.setAdapter(adapter);
    }

    private int parse(String key) {
        Log.i(TAG, "start loading");
        mMovieList.clear();
        try {
            Document doc = Jsoup.connect("http://www.quzhuanpan.com/source/search.action").data("q", key).get();
            Elements links = doc.select("a[href]");
            //注意这里是Elements不是Element。同理getElementById返回Element，getElementsByClass返回时Elements
            for (Element link : links) {
                // 过滤链接
                String tag = link.text().toString();
                String url = link.attr("abs:href");
//                Log.e("links", tag + " " + url);
                if (tag.contains(key) && url.contains(BAIDU_PAN_HOST)) {
                    MovieUrl movie = new MovieUrl();
                    movie.url = url;
                    movie.tag = tag;
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
     * 回调对象，接受请求结果
     */
    private OnResponseListener<String> onResponseListener = new OnResponseListener<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == NOHTTP_WHAT_TEST) {// 判断what是否是刚才指定的请求
                // 请求成功
                final String result = response.get();// 响应结果
                // 响应头
                Headers headers = response.getHeaders();
                int code = headers.getResponseCode();// 响应码
                long time = response.getNetworkMillis();// 请求花费的时间

                Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();

                Log.e("http", "result: " + result);
                Log.i("http", "msg: " + response.responseMessage());
                Log.i("http", "time: " + time);
//                mWebView.loadData(result, "text/html", "UTF-8");
                try {
                    File f = new File(Environment.getExternalStorageDirectory(), "result.html");
                    if (f.exists()) {
                        f.delete();
                    }
                    FileOutputStream fos = new FileOutputStream(f, true);
                    fos.write(result.getBytes());
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                WebViewActivity.startForLocalFile(MainActivity.this, "result.html");
            }
        }

        @Override
        public void onStart(int what) {
            // 请求开始，显示dialog
        }

        @Override
        public void onFinish(int what) {
            // 请求结束，关闭dialog
        }

        @Override
        public void onFailed(int what, Response response) {
            // 请求失败
        }
    };

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
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(item.url);
        intent.setData(content_url);
        startActivity(intent);

//        PlayerActivity.start(MainActivity.this, "ok", "http://pan.baidu.com/share/link?shareid=1039547194&uk=3943590444&fid=542233410763175");
//        WebViewActivity.startForUrl(this, item.url);
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

