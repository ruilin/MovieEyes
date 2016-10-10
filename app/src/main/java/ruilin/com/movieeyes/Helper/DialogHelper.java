package ruilin.com.movieeyes.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import ruilin.com.movieeyes.R;
import ruilin.com.movieeyes.activity.MainActivity;
import ruilin.com.movieeyes.base.AppInfo;
import ruilin.com.movieeyes.util.ViewUtil;

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

    public static void showAbout(final Activity activity) {
        Resources res = activity.getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        TextView tv = new TextView(activity);
        tv.setText("程序猿正在加班改进中," +
                "请您多多支持哦!!" +
                "\nwww.pgyer.com/search");
        tv.setTextSize(res.getDimensionPixelOffset(R.dimen.text_size_1));
        tv.setAutoLinkMask(Linkify.ALL);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        int padding = (int)ViewUtil.dip2px(activity, 20);
        tv.setPadding(padding, padding, padding, padding);
        builder.setIcon(R.drawable.ic_tips)
                .setTitle(AppInfo.getVersionName(activity))
                .setView(tv)
                .setPositiveButton("点赞", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.doUrl(activity, "http://www.coolapk.com/apk/ruilin.com.movieeyes");
                    }
                })
                .setNegativeButton("吐槽", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent data=new Intent(Intent.ACTION_SENDTO);
                        data.setData(Uri.parse("mailto:zruilin@126.com"));
                        data.putExtra(Intent.EXTRA_SUBJECT, AppInfo.getVersionName(activity)+" 反馈");
                        data.putExtra(Intent.EXTRA_TEXT, "吐槽");
                        activity.startActivity(data);
                    }
                })
                .setNeutralButton("关闭", new DialogInterface.OnClickListener() {
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
