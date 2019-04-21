package com.comvee.blelibrary.util;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by xiaoxinI2000 on 2017/5/10.
 */

public class CommonUtil {

    public static SimpleDateFormat getDateFormat(boolean paramBoolean)
    {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        if (!paramBoolean) {
            localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return localSimpleDateFormat;
    }
}
