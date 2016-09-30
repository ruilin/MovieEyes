package ruilin.com.movieeyes.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.base.BaseActivity;

public class RecordActivity extends BaseActivity {


    public static void start(Activity act) {
        Intent intent = new Intent(act, RecordActivity.class);
        act.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
    }
}
