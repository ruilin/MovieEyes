package ruilin.com.movieeyes.Helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ruilin on 2016/9/23.
 */

public class ToastHelper {

    public static void show(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
