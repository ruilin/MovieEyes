package ruilin.com.movieeyes.Jni;

/**
 * Created by ruilin on 16/9/27.
 */

public class LibJni {
    static {
        System.loadLibrary("movieeyes");
    }

    public static native String getHost();
}
