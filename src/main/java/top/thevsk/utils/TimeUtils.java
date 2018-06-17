package top.thevsk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static final String FORMAT_CURRENT_TIME = "yyyy-MM-dd HH:mm:ss";

    public static String dateFormatString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String formatCurrentTime(Date date) {
        return dateFormatString(date, FORMAT_CURRENT_TIME);
    }

    public static String getCurrentTime() {
        return formatCurrentTime(new Date(System.currentTimeMillis()));
    }
}
