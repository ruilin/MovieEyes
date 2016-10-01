package ruilin.com.movieeyes.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import ruilin.com.movieeyes.Helper.DialogHelper;
import ruilin.com.movieeyes.Helper.SearchKeyHelper;
import ruilin.com.movieeyes.Helper.ToastHelper;
import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.base.BasePageActivity;
import ruilin.com.movieeyes.db.bean.SearchRecordDb;
import ruilin.com.movieeyes.fragment.RecordFagment;

public class RecordActivity extends BasePageActivity implements RecordFagment.OnRecordItemListener {

    RecordFagment mFragment;

    public static void start(Activity act) {
        Intent intent = new Intent(act, RecordActivity.class);
        act.startActivity(intent);
    }
    @Override
    protected String setTitle() {
        return getString(R.string.record_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Resources res = getResources();
        switch (item.getItemId()) {
            case R.id.action_delete:
                DialogHelper.show(this, res.getString(R.string.dialog_title_delete), res.getString(R.string.dialog_message_delete),
                        new DialogHelper.OnClickListener() {
                            @Override
                            public void onConfirm() {
                                /* 清除记录 */
                                ToastHelper.show(RecordActivity.this, getResources().getString(R.string.setting_clear_finish));
                                SearchKeyHelper.getInstance().clear();
                            }
                        });
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
        mFragment = RecordFagment.newInstance();
        ft.replace(R.id.ll_content, mFragment);
        ft.commit();
    }

    @Override
    public void onRecordItemClicked(SearchRecordDb key, int index) {
        close();
        MainActivity.start(this, key);
    }

    @Override
    public void onRecordItemSelected(SearchRecordDb key, int index, boolean isSelected) {

    }

    @Override
    public void close() {
        MainActivity.start(this, null);
        super.close();
    }
}
