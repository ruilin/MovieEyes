package ruilin.com.movieeyes.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

import ruilin.com.movieeyes.Helper.DialogHelper;
import ruilin.com.movieeyes.Helper.JsoupHelper;
import ruilin.com.movieeyes.Helper.SearchKeyHelper;
import ruilin.com.movieeyes.Helper.ShareHelper;
import ruilin.com.movieeyes.Helper.ToastHelper;
import ruilin.com.movieeyes.Helper.UMHelper;
import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.adapter.SearchAdapter;
import ruilin.com.movieeyes.base.BaseActivity;
import ruilin.com.movieeyes.db.bean.SearchRecordDb;
import ruilin.com.movieeyes.db.bean.SearchResultDb;
import ruilin.com.movieeyes.fragment.HotFragment;
import ruilin.com.movieeyes.fragment.MovieListFragment;
import ruilin.com.movieeyes.modle.SearchKey;

/**
 * @author Ruilin
 */
public class
MainActivity extends BaseActivity implements OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        MovieListFragment.OnListFragmentInteractionListener, HotFragment.OnHotKeyClickedListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String PARAM_KEY = "PARAM_KEY";
    private final static String PARAM_FORCE_SEARCH = "PARAM_FORCE_SEARCH";
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
    private ArrayList<SearchResultDb> mMovieList;

    public static void start(Context context, SearchRecordDb key) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(PARAM_KEY, key);
        context.startActivity(intent);
    }

    public static void startForceSearch(Context context, SearchRecordDb key) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(PARAM_FORCE_SEARCH, true);
        intent.putExtra(PARAM_KEY, key);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashActivity.close();
        initView();
        mMovieList = new ArrayList<>();
        Button searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = mKeyView.getText().toString();
                if (key.length() == 0) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.main_search_null_tips), Toast.LENGTH_SHORT).show();
                    return;
                }
                doSearch(key, 1);
                UMHelper.onSearchButton(MainActivity.this);
            }
        });

    }

    private void initView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContentView = findViewById(R.id.fl_content);
        mProgressView = findViewById(R.id.pv_loading);
        mKeyView = (AutoCompleteTextView) findViewById(R.id.key);
        mFragmentLayout = (LinearLayout) findViewById(R.id.ll_result);

        updateKeyTips();

        mKeyView.setThreshold(1);
        mKeyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        mKeyView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mKeyView.getThreshold() > 10) {
                    mKeyView.setThreshold(1);
                }
            }
        });
        mHotFra = HotFragment.newInstance();
        mMovieFra = MovieListFragment.newInstance();
        setFragment(FRAGMENT_TYPE_HOT_KEY);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getHeaderView(0).findViewById(R.id.iv_nav_header).setOnClickListener(this);

//        AdManager.getInstance(this).init("041d98ac4aebd44e", "2f07918ccd819c43", false, BuildConfig.DEBUG);
//        AdHelper.setYoumi(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            SearchRecordDb key = (SearchRecordDb) intent.getSerializableExtra(PARAM_KEY);
            if (key != null) {
                doSearch(key.getKey(), 1);
            }
        }
        setIntent(null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (intent.getBooleanExtra(PARAM_FORCE_SEARCH, false)) {
                SearchRecordDb key = (SearchRecordDb) intent.getSerializableExtra(PARAM_KEY);
                if (key != null) {
                    doSearch(key.getKey(), 1);
                    setIntent(null);
                    return;
                }
            }
        }
        setIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        setIconsVisible(menu, true);
        return true;
    }

    long mFirstClickTime;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mFirstClickTime < 1500) {
                super.onBackPressed();
            } else {
                mFirstClickTime = System.currentTimeMillis();
                ToastHelper.show(this, getResources().getString(R.string.toast_exit_again));
            }
        }
    }

    private void setIconsVisible(Menu menu, boolean flag) {
        //判断menu是否为空
        if(menu != null) {
            try {
                //如果不为空,就反射拿到menu的setOptionalIconsVisible方法
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                //暴力访问该方法
                method.setAccessible(true);
                //调用该方法显示icon
                method.invoke(menu, flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Resources res = getResources();
        switch (item.getItemId()) {
            case R.id.action_settings:
                ToastHelper.show(this, res.getString(R.string.toast_setting));
                break;
            case R.id.action_clear_record:
                DialogHelper.show(this, res.getString(R.string.dialog_title_delete), res.getString(R.string.dialog_message_delete),
                        new DialogHelper.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        /* 清除记录 */
                        ToastHelper.show(MainActivity.this, getResources().getString(R.string.setting_clear_finish));
                        SearchKeyHelper.getInstance().clear();
                        updateKeyTips();
                    }
                });
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_favorite) {
            // Handle the camera action
            FavoriteActivity.start(this);
        } else if (id == R.id.nav_record) {
            RecordActivity.start(this);
        } else if (id == R.id.nav_share) {
            ShareHelper.share(this, getResources().getString(R.string.share_connent));
        } else if (id == R.id.nav_about) {
            DialogHelper.showAbout(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void closeImm(Activity activity) {
        /* 关闭键盘 */
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View focus = activity.getCurrentFocus();
            if (imm != null && focus != null) {
                imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
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
        ft.commitNow();
    }

    public ArrayList<SearchResultDb> getMovieList() {
        return mMovieList;
    }

    private void updateKeyTips() {
        SearchAdapter adapter = new SearchAdapter<>(MainActivity.this,
                android.R.layout.simple_dropdown_item_1line, SearchKeyHelper.getInstance().genStringList(),
                SearchAdapter.ALL);//速度优先
        mKeyView.setAdapter(adapter);
    }

    public static void doUrl(Activity activity, String url) {
        closeImm(activity);
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            activity.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "url>" + url);
            ToastHelper.show(activity, "链接失效");
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
        switch (v.getId()) {
            case R.id.iv_nav_header:
                LoginActivity.start(this);
                break;
        }
    }

    @Override
    public void onListFragmentInteraction(SearchResultDb item) {
//        PlayerActivity.start(MainActivity.this, "ok", "http://pan.baidu.com/share/link?shareid=1039547194&uk=3943590444&fid=542233410763175");
//        WebViewActivity.startForUrl(this, item.url);
    }

    @Override
    public void onMovielistClose() {
        setFragment(FRAGMENT_TYPE_HOT_KEY);
        closeImm(this);
    }

    @Override
    public void onNextPage(String key, int currentPage) {
        doSearch(key, currentPage + 1);
    }

    @Override
    public void onHotKeyClicked(SearchKey key) {
        doSearch(key.getKey(), 1);
    }

    public void doSearch(String key, int page) {
        if (mProgressView.getVisibility() == View.VISIBLE) {
            Log.i(TAG, "cannot operate");
            return;
        }
        new LoadUrlTask(key, page).execute();
    }

    public void setKeyToEditText(String key) {
        mKeyView.setText(key);
        mKeyView.setSelection(key.length());
        mKeyView.setThreshold(99);
        UMHelper.onSearchKey(this, key);
    }

    /**
     *
     */
    public class LoadUrlTask extends AsyncTask<Void, Void, Integer> {
        private String key;
        private int page;

        LoadUrlTask(String key, int page) {
            this.key = key;
            this.page = page;
            if (this.page <= 0) this.page = JsoupHelper.FIRST_PAGE_NUM;
            setKeyToEditText(key);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            closeImm(MainActivity.this);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return JsoupHelper.parseHtmlForSearch(key, page, mMovieList);
        }

        @Override
        protected void onPostExecute(final Integer resultCode) {
            showProgress(false);
            switch (resultCode) {
                case JsoupHelper.RESULT_CODE_SUCCESS:
                    Log.i(TAG, "success!!! " + mMovieList.size());
                    if (mCurrentFraType == FRAGMENT_TYPE_MOVIE_SEARCH) {
                        mMovieFra.update(key, page);
                    } else {
                        page = JsoupHelper.FIRST_PAGE_NUM;
                        setFragment(FRAGMENT_TYPE_MOVIE_SEARCH);
                        mMovieFra.update(key, page);
                    }
                    SearchKeyHelper.getInstance().add(key);
//                    updateKeyTips();
                    break;
                case JsoupHelper.RESULT_CODE_EMPTY:
                    if (page == JsoupHelper.FIRST_PAGE_NUM) {
                        ToastHelper.show(MainActivity.this, getResources().getString(R.string.main_net_empty));
                    } else {
                        ToastHelper.show(MainActivity.this, getResources().getString(R.string.main_net_no_next_page));
                    }
                    if (mCurrentFraType == FRAGMENT_TYPE_MOVIE_SEARCH) {
                        mMovieFra.update(key, page);
                    }
                    break;
                case JsoupHelper.RESULT_CODE_TIMEOUT:
                    ToastHelper.show(MainActivity.this, getResources().getString(R.string.main_net_timeout_tips));
                    UMHelper.reportError(MainActivity.this, "timeout");
                    break;
                case JsoupHelper.RESULT_CODE_ERROR:
                default:
                    ToastHelper.show(MainActivity.this, getResources().getString(R.string.main_net_error_tips));
                    UMHelper.reportError(MainActivity.this, "http error");
                    break;
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

}

