package ruilin.com.movieeyes.activity;


import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.base.BasePageActivity;

/**
 * @author Ruilin
 *       网页
 */
public class WebViewActivity extends BasePageActivity {
    private static final String PARAM_URL = "PARAM_URL";
    private static final String PARAM_HTML = "PARAM_HTML";
    private WebView mWebView;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_web;
    }

    @Override
    protected String setTitle() {
        return "获取资源";
    }

    @Override
    protected void init() {
        super.init();
        mWebView = (com.tencent.smtt.sdk.WebView)findViewById(R.id.tbsContent);
        mWebView.loadUrl("https://pan.baidu.com/share/link?uk=2232927868&shareid=538696945#path=%252Fmovie%252F%25E5%25A4%25A7%25E8%25AF%259D%25E8%25A5%25BF%25E6%25B8%25B8");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
