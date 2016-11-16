package ruilin.com.movieeyes.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ruilin.com.movieeyes.R;

/**
 * Created by Ruilin on 2016/9/30.
 */

public abstract class BasePageActivity extends BaseActivity {
    private LinearLayout mContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_title_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = setTitle();
        if (title != null) {
            toolbar.setTitle(title);
        }
        setSupportActionBar(toolbar);
        initToolBar(toolbar);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        mContentLayout = (LinearLayout)findViewById(R.id.ll_base_content);
        View contentView = getLayoutInflater().inflate(setContentView(), null);
        mContentLayout.addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        init();
    }

    protected String setTitle() {
        return null;
    }
    protected void initToolBar(Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }
    protected abstract @LayoutRes int setContentView();
    protected void init(){};

    public void close() {
        finish();
    }
}

