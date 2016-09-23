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
import android.widget.Toast;

import java.util.ArrayList;

import ruilin.com.movieeyes.Helper.JsoupHelper;
import ruilin.com.movieeyes.Helper.SearchKeyHelper;
import ruilin.com.movieeyes.Helper.ToastHelper;
import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.adapter.SearchAdapter;
import ruilin.com.movieeyes.fragment.HotFragment;
import ruilin.com.movieeyes.fragment.MovieListFragment;
import ruilin.com.movieeyes.modle.HotKey;
import ruilin.com.movieeyes.modle.MovieUrl;

/**
 * @author Ruilin
 */
public class MainActivity extends AppCompatActivity implements OnClickListener, MovieListFragment.OnListFragmentInteractionListener, HotFragment.OnHotKeyClickedListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final static int FRAGMENT_TYPE_MOVIE_SEARCH = 0;
    private final static int FRAGMENT_TYPE_HOT_KEY = 1;

    // UI references.
    private AutoCompleteTextView mKeyView;
    private View mProgressView;
    private View mContentView;
    private LinearLayout mFragmentLayout;
    private HotFragment mHotFra;
    private MovieListFragment mMovieFra;
    private int mCurrentFraType;
    private ArrayList<MovieUrl> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mMovieList = new ArrayList<>();

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

        mHotFra = HotFragment.newInstance();
        mMovieFra = MovieListFragment.newInstance();
        setFragment(FRAGMENT_TYPE_HOT_KEY);
    }

    public void setFragment(int type) {
        if (mCurrentFraType == type) {
            Log.e(TAG, "fra already seted");
            return;
        }
        mCurrentFraType = type;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (type) {
            case FRAGMENT_TYPE_MOVIE_SEARCH:
                ft.setCustomAnimations(R.anim.push_bottom_in, R.anim.push_top_out);
                ft.replace(R.id.ll_result, mMovieFra);
                break;
            case FRAGMENT_TYPE_HOT_KEY:
            default:
                ft.setCustomAnimations(R.anim.push_top_in, R.anim.push_bottom_out);
                ft.replace(R.id.ll_result, mHotFra);
                break;
        }
//        ft.addToBackStack(null);
        ft.commit();
    }

    public ArrayList<MovieUrl> getMovieList() {
        return mMovieList;
    }

    private void updateKeyTips() {
        SearchAdapter adapter = new SearchAdapter<>(MainActivity.this,
                android.R.layout.simple_dropdown_item_1line, SearchKeyHelper.getInstance().getList(),
                SearchAdapter.ALL);//速度优先
        mKeyView.setAdapter(adapter);
    }

    public static void doUrl(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "url>"+url);
            ToastHelper.show(context, "链接失效");
            e.printStackTrace();
        }
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
//        PlayerActivity.start(MainActivity.this, "ok", "http://pan.baidu.com/share/link?shareid=1039547194&uk=3943590444&fid=542233410763175");
//        WebViewActivity.startForUrl(this, item.url);
    }

    @Override
    public void onMovielistClose() {
        setFragment(FRAGMENT_TYPE_HOT_KEY);
    }

    @Override
    public void onHotKeyClicked(HotKey key) {
        new LoadUrlTask(key.getKey()).execute();
    }

    /**
     *
     */
    public class LoadUrlTask extends AsyncTask<Void, Void, Integer> {
        String key;

        LoadUrlTask(String key) {
            this.key = key;
            mKeyView.setText(key);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return JsoupHelper.parseHtmlForSearch(key, mMovieList);
        }

        @Override
        protected void onPostExecute(final Integer resultCode) {
            showProgress(false);
            switch (resultCode) {
                case JsoupHelper.RESULT_CODE_SUCCESS:
                    Log.i(TAG, "success!!! "+ mMovieList.size());
                    if (mCurrentFraType == FRAGMENT_TYPE_MOVIE_SEARCH) {
                        mMovieFra.update();
                    } else {
                        setFragment(FRAGMENT_TYPE_MOVIE_SEARCH);
                    }
                    SearchKeyHelper.getInstance().add(key);
                    updateKeyTips();
                    break;
                case JsoupHelper.RESULT_CODE_TIMEOUT:
                    ToastHelper.show(MainActivity.this, getResources().getString(R.string.main_net_timeout_tips));
                    break;
                case JsoupHelper.RESULT_CODE_ERROR:
                default:
                    ToastHelper.show(MainActivity.this, getResources().getString(R.string.main_net_error_tips));
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }


}

