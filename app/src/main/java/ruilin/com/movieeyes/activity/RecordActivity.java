package ruilin.com.movieeyes.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.base.BaseActivity;
import ruilin.com.movieeyes.base.BasePageActivity;
import ruilin.com.movieeyes.fragment.RecordFagment;

public class RecordActivity extends BasePageActivity implements RecordFagment.OnRecordItemListener {


    public static void start(Activity act) {
        Intent intent = new Intent(act, RecordActivity.class);
        act.startActivity(intent);
    }
    @Override
    protected String setTitle() {
        return getString(R.string.record_title);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_record;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.ll_content, RecordFagment.newInstance());
        ft.commit();
    }

    @Override
    public void onRecordItemClicked(Uri uri) {

    }
}
