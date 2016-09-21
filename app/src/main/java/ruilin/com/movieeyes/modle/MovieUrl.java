package ruilin.com.movieeyes.modle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ruilin on 2016/9/19.
 */
public class MovieUrl implements Parcelable {
    public String tag;
    public String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tag);
        dest.writeString(url);
    }

    public static final Creator<MovieUrl> CREATOR = new Creator<MovieUrl>() {
        @Override
        public MovieUrl createFromParcel(Parcel source) {
            MovieUrl movie = new MovieUrl();
            movie.tag = source.readString();
            movie.url = source.readString();
            return movie;
        }

        @Override
        public MovieUrl[] newArray(int size) {
            return new MovieUrl[size];
        }
    };
}
