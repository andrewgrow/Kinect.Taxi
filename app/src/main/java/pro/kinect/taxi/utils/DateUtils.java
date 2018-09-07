package pro.kinect.taxi.utils;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_FORMAT_STANDART = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MM_DD_YYYY = "MM/dd/yyyy";
    public static final String FILE_DATE_FORMAT = "yyyyMMdd_HHmm";

    @StringDef({DATE_FORMAT, TIME_FORMAT, DATE_FORMAT_STANDART, DATE_FORMAT_MM_DD_YYYY, FILE_DATE_FORMAT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DateFormat {}

    public static String getCurrentDateAsString(@DateFormat String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(new Date());
    }
}
