package ruilin.com.movieeyes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * @author Ruilin
 */
public class MainActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mKeyView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvUrl;

    /**
     * 用来标志请求的what, 类似handler的what一样，这里用来区分请求
     */
    private static final int NOHTTP_WHAT_TEST = 0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the login form.
        mKeyView = (AutoCompleteTextView) findViewById(R.id.key);
        tvUrl = (TextView) findViewById(R.id.tv_url);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
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

                new LoadUrlTask(mKeyView.getText().toString()).execute();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    private void parse(String key) {
        StringBuffer sb = new StringBuffer();
        try {
            Document doc = Jsoup.connect("http://www.quzhuanpan.com/source/search.action").data("q", key).get();
            Elements links = doc.select("a[href]");
            //注意这里是Elements不是Element。同理getElementById返回Element，getElementsByClass返回时Elements
            boolean go = true;
            for (Element link : links) {
                // 过滤链接
                String tag = link.text().toString();
                String url = link.attr("abs:href");
                Log.e("links", tag + " " + url);
                if (tag.contains(key) && url.contains("pan.baidu")) {
                    sb.append(link.text() + " " + link.attr("abs:href"));
                    sb.append("\n");

                    if (go) {
                        go = false;
//                        WebViewActivity.startForUrl(MainActivity.this, link.attr("abs:href"));

//                        Intent intent= new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        Uri content_url = Uri.parse(link.attr("abs:href"));
//                        intent.setData(content_url);
//                        startActivity(intent);

//                        PlayerActivity.start(MainActivity.this, "ok", "http://pan.baidu.com/share/link?shareid=1039547194&uk=3943590444&fid=542233410763175");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Message msg = new Message();
        msg.what = 1;
        msg.obj = sb.toString();
        mHandler.sendMessage(msg);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String urls = (String) msg.obj;
                    tvUrl.setText(urls);
                    break;
                default:
                    break;
            }
        }
    };

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

                Log.e("http", "result: "+result);
                Log.i("http", "msg: "+response.responseMessage());
                Log.i("http", "time: "+time);
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     *
     */
    public class LoadUrlTask extends AsyncTask<Void, Void, Boolean> {
        String key;

        LoadUrlTask(String key) {
            this.key = key;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            parse(key);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
}

