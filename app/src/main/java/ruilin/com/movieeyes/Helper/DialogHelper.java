package ruilin.com.movieeyes.Helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;

import ruilin.com.movieeyes.R;

/**
 * Created by Ruilin on 2016/9/26.
 */

public class DialogHelper {

    public static void show(Context context, String title, String msg, final OnClickListener l) {
        Resources res = context.getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_prompt)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(res.getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (l != null) {
                            l.onConfirm();
                        }
                    }
                })
                .setNegativeButton(res.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();
    }

    //http://www.easyicon.net/1141878-search_cat_icon.html
    public static void showTips(Context context, String title, String msg) {
        Resources res = context.getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_tips)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(res.getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .create().show();
    }

    public interface OnClickListener {
        public void onConfirm();
    }
}
