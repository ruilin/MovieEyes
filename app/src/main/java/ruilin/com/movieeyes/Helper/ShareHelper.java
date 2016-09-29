package ruilin.com.movieeyes.Helper;

import android.content.Context;
import android.content.Intent;

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
}
