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
import ruilin.com.movieeyes.Helper.SearchResultHelper;
import ruilin.com.movieeyes.Helper.ToastHelper;
import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.base.BasePageActivity;
import ruilin.com.movieeyes.db.bean.SearchResultDb;
import ruilin.com.movieeyes.fragment.FavoriteFagment;

public class FavoriteActivity extends BasePageActivity implements FavoriteFagment.OnFavoriteItemListener {

    FavoriteFagment mFragment;

    public static void start(Activity act) {
        Intent intent = new Intent(act, FavoriteActivity.class);
        act.startActivity(intent);
    }
    @Override
    protected String setTitle() {
        return getString(R.string.favorite_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Resources res = getResources();
        switch (item.getItemId()) {
            case R.id.action_delete:
                DialogHelper.show(this, res.getString(R.string.dialog_title_delete_favorite), res.getString(R.string.dialog_message_delete_favorite),
                        new DialogHelper.OnClickListener() {
                            @Override
                            public void onConfirm() {
                                /* 清除记录 */
                                ToastHelper.show(FavoriteActivity.this, getResources().getString(R.string.toast_delete_favorite_finish));
                                SearchResultHelper.getInstance().clear();
                                close();
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
        return R.layout.activity_favorite;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mFragment = FavoriteFagment.newInstance();
        ft.replace(R.id.ll_content, mFragment);
        ft.commit();
    }

    @Override
    public void close() {
        MainActivity.start(this, null);
        super.close();
    }

    @Override
    public void onFavoriteItemClicked(SearchResultDb item) {

    }
}
