package ruilin.com.movieeyes;

import android.app.Application;

import com.yolanda.nohttp.NoHttp;

/**
 * Created by Administrator on 2016/9/13.
 */
public class MovieApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
    }
}
