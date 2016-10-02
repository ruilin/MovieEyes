package ruilin.com.movieeyes.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ruilin on 16/10/2.
 */

public class DateUtil {

    public static final String DATE_STANDER_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static String timeToDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_STANDER_FORMAT);
        String date = format.format(time);
        return date;
    }

    public static String determineDateFormat(String dateStr) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
        try {
            Date date = sdf1.parse(dateStr);
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_STANDER_FORMAT);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }
}
