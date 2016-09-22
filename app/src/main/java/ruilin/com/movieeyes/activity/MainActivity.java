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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ruilin.com.movieeyes.Helper.JsoupHelper;
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
            return JsoupHelper.parseHtml(key, mMovieList);
        }

        @Override
        protected void onPostExecute(final Integer resultCode) {
            showProgress(false);
            switch (resultCode) {
                case JsoupHelper.RESULT_CODE_SUCCESS:
                    Log.i(TAG, "success!!! "+ mMovieList.size());
                    mMovieFra.update();
                    SearchKeyHelper.getInstance().add(key);
                    updateKeyTips();
                    break;
                case JsoupHelper.RESULT_CODE_TIMEOUT:
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.main_net_timeout_tips), Toast.LENGTH_SHORT).show();
                    break;
                case JsoupHelper.RESULT_CODE_ERROR:
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

