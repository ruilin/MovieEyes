package ruilin.com.movieeyes.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.base.BaseActivity;

/**
 * @author Ruilin
 *       网页
 */
public class WebViewActivity extends BaseActivity {
    private static final String PARAM_URL = "PARAM_URL";
    private static final String PARAM_HTML = "PARAM_HTML";

    private WebView mWebView;

    public static void startForData(Activity act, String html) {
        Intent intent = new Intent(act, WebViewActivity.class);
        intent.putExtra(PARAM_HTML, html);
        act.startActivity(intent);
    }

    public static void startForUrl(Activity act, String url) {
        Intent intent = new Intent(act, WebViewActivity.class);
        intent.putExtra(PARAM_URL, url);
        act.startActivity(intent);
    }

    public static void startForLocalFile(Activity act, String fileName) {
        Intent intent = new Intent(act, WebViewActivity.class);
        intent.putExtra(PARAM_URL, "file:/sdcard/"+fileName);
        act.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initWebView();

        Intent intent = getIntent();
        String html = intent.getStringExtra(PARAM_HTML);
        String url = intent.getStringExtra(PARAM_URL);
        if (html != null) {
            mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        } else if (url != null) {
            mWebView.loadUrl(url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if(mWebView.canGoBack())
            {
                mWebView.goBack();//返回上一页面
                return true;
            }
            else
            {
                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不需要缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setJavaScriptEnabled(true);// 允许js交互
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webSettings.setUseWideViewPort(true);
        //webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//有些手机可能存在兼容性，页面排版错乱

        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

        webSettings.setLoadWithOverviewMode(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //在当前WebView跳转
                //FIXME:某些链接在goBack时会一直跳转回到最后的连接,暂时用这个判断解决
                WebHistoryItem item = mWebView.copyBackForwardList().getCurrentItem();
                if (item == null || !url.equals(item.getUrl())) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                //加载错误页面
//                view.loadUrl(UrlPathHelper.getErrorUrl());
                //这里已经是主线程
            }

        });

    }
}
