package ruilin.com.movieeyes.Helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import ruilin.com.movieeyes.R;

/**
 * Created by ruilin on 16/9/29.
 */

public class ShareHelper {

    public static void share(Context context, String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static void shareApp(Context context, String message) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("https://static.pgyer.com/app/qrcode/search"));
        sendIntent.setType("image/*");
        sendIntent.putExtra("sms_body", message);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(sendIntent, "把‘" + context.getString(R.string.app_name) + "’分享到："));
//        context.startActivity(sendIntent);
    }
}
