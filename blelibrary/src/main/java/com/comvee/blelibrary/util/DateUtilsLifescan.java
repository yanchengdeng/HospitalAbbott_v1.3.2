package com.comvee.blelibrary.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtilsLifescan
{
    public static final long MS_IN_DAY = 86400000L;
    public static final long MS_IN_HOUR = 3600000L;
    public static final long MS_IN_MINUTE = 60000L;
    public static final long MS_IN_SECOND = 1000L;
    private static final String TAG = "DateUtilsLifescan";
    public static final String TIME_ZONE_AMERICA_BRASILIA = "America/Brasilia";
    public static final String TIME_ZONE_AMERICA_BUENOS_AIRES = "America/Buenos_Aires";
    public static final String TIME_ZONE_AMERICA_CUIABA = "America/Cuiaba";
    public static final String TIME_ZONE_AMERICA_MANAUS = "America/Manaus";
    public static final String TIME_ZONE_AMERICA_SAO_PAULO = "America/Sao_Paulo";
    public static final long UNIXTIME_TO_METERTIME_DELTA_MILLIS = 946684800000L;



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getDateFormatString(long paramLong, boolean paramBoolean)
    {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "MM/dd/yy"), Locale.getDefault());
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(paramLong);
        if (!paramBoolean) {
            localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return localSimpleDateFormat.format(localCalendar.getTime());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getDateString(long paramLong, boolean paramBoolean)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMM dd yyyy"), Locale.getDefault());
        if (!paramBoolean) {
            localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        localStringBuilder.append(localSimpleDateFormat.format(Long.valueOf(paramLong)));
        return localStringBuilder.toString();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getDateWithoutYearMd(long paramLong, boolean paramBoolean)
    {
        RevealCalendar localRevealCalendar = new RevealCalendar(paramLong);
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "Md"), Locale.getDefault());
        if (!paramBoolean) {
            localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return localSimpleDateFormat.format(localRevealCalendar.getTime());
    }

    public static long getDay(long paramLong)
    {
        RevealCalendar localRevealCalendar = new RevealCalendar(paramLong);
        localRevealCalendar.set(Calendar.HOUR_OF_DAY, 0);
        localRevealCalendar.set(Calendar.MINUTE, 0);
        localRevealCalendar.set(Calendar.SECOND, 0);
        localRevealCalendar.set(Calendar.MILLISECOND, 0);
        return localRevealCalendar.getTimeInMillis();
    }

    public static int getDayDifference(long paramLong1, long paramLong2)
    {
        return (int) Math.round((getDay(paramLong1) - getDay(paramLong2)) / 8.64E7D);
    }

    public static long getDeltaTOD(long paramLong1, long paramLong2)
    {
        long l = paramLong2 - paramLong1;
        if (Math.abs(l) > 43200000L)
        {
            l = paramLong2 + 86400000L - paramLong1;
            if (Math.abs(l) > 43200000L) {
                l = paramLong2 - (paramLong1 + 86400000L);
            }
            if (Math.abs(l) > 43200000L)
            {
                Object[] arrayOfObject = new Object[2];
                arrayOfObject[0] = Long.valueOf(paramLong1);
                arrayOfObject[1] = Long.valueOf(paramLong2);
            }
        }
        return l;
    }

    public static long getEndOfToday()
    {
        RevealCalendar localRevealCalendar = new RevealCalendar();
        localRevealCalendar.set(Calendar.HOUR_OF_DAY, 23);
        localRevealCalendar.set(Calendar.MINUTE, 59);
        localRevealCalendar.set(Calendar.SECOND, 59);
        localRevealCalendar.set(Calendar.MILLISECOND, 999);
        return localRevealCalendar.getTimeInMillis();
    }

    public static long getStartOfCalendarDay(RevealCalendar paramRevealCalendar)
    {
        paramRevealCalendar.set(Calendar.HOUR_OF_DAY, 0);
        paramRevealCalendar.set(Calendar.MINUTE, 0);
        paramRevealCalendar.set(Calendar.SECOND, 0);
        paramRevealCalendar.set(Calendar.MILLISECOND, 0);
        return paramRevealCalendar.getTimeInMillis();
    }

    public static long getTimeOfDayMilliseconds(long paramLong)
    {
        RevealCalendar localRevealCalendar = new RevealCalendar(paramLong);
        return 3600000L * localRevealCalendar.get(Calendar.HOUR_OF_DAY) + 60000L * localRevealCalendar.get(Calendar.MINUTE) + 1000L * localRevealCalendar.get(Calendar.SECOND);
    }

    public static String getTimeString(Context paramContext, long paramLong, boolean paramBoolean)
    {
        if (paramBoolean) {
            return DateUtils.formatDateRange(paramContext, new Formatter(), paramLong, paramLong, 513).toString();
        }
        return DateUtils.formatDateRange(paramContext, new Formatter(), paramLong, paramLong, 513, "UTC").toString();
    }

    public static String getTimeString(Context paramContext, Date paramDate)
    {
        return android.text.format.DateFormat.getTimeFormat(paramContext).format(paramDate);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String recentEventsDate(long paramLong, Context paramContext)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        RevealCalendar localRevealCalendar1 = new RevealCalendar();
        RevealCalendar localRevealCalendar2 = new RevealCalendar(paramLong);
        RevealCalendar localRevealCalendar3 = new RevealCalendar();
        localRevealCalendar3.add(Calendar.DAY_OF_MONTH, -1);
        RevealCalendar localRevealCalendar4 = new RevealCalendar();
        localRevealCalendar4.add(Calendar.DAY_OF_MONTH, -6);
        localRevealCalendar4.set(Calendar.HOUR_OF_DAY, 0);
        localRevealCalendar4.set(Calendar.MINUTE, 0);
        localRevealCalendar4.set(Calendar.SECOND, 0);
        localRevealCalendar4.set(Calendar.MILLISECOND, 0);
        if ((localRevealCalendar1.get(Calendar.YEAR) == localRevealCalendar2.get(Calendar.YEAR)) && (localRevealCalendar1.get(Calendar.MONTH) == localRevealCalendar2.get(Calendar.MONTH)) && (localRevealCalendar1.get(Calendar.DAY_OF_MONTH) == localRevealCalendar2.get(Calendar.DAY_OF_MONTH))) {
            localStringBuilder.append("2131099785");
        }
        for (;;)
        {
            if (localRevealCalendar2.compareTo(localRevealCalendar4) == -1)
            {
                SimpleDateFormat localSimpleDateFormat2 = new SimpleDateFormat(android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMM dd"), Locale.getDefault());
                localSimpleDateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
                localStringBuilder.append(", ");
                localStringBuilder.append(localSimpleDateFormat2.format(Long.valueOf(paramLong)));
            }

            if ((localRevealCalendar3.get(Calendar.YEAR) == localRevealCalendar2.get(Calendar.YEAR)) && (localRevealCalendar3.get(Calendar.MONTH) == localRevealCalendar2.get(Calendar.MONTH)) && (localRevealCalendar3.get(Calendar.DAY_OF_MONTH) == localRevealCalendar2.get(Calendar.DAY_OF_MONTH)))
            {
                localStringBuilder.append("2131099787");
            }
            else
            {
                SimpleDateFormat localSimpleDateFormat1 = new SimpleDateFormat("EEEE", Locale.getDefault());
                localSimpleDateFormat1.setTimeZone(TimeZone.getTimeZone("UTC"));
                localStringBuilder.append(localSimpleDateFormat1.format(Long.valueOf(paramLong)));
            }
        }
    }

    /*public static Calendar removeDayLightOfCalendar(Calendar paramCalendar)
    {
        String str;
        int i;
        if (TimeZone.getDefault().inDaylightTime(new Date()))
        {
            str = Calendar.getInstance().getTimeZone().getID();
            i = -1;
            switch (str.hashCode())
            {
            }
        }
        for (;;)
        {
            switch (i)
            {
                default:
                    return paramCalendar;
                if (str.equals("America/Sao_Paulo"))
                {
                    i = 0;
                    continue;
                    if (str.equals("America/Brasilia"))
                    {
                        i = 1;
                        continue;
                        if (str.equals("America/Cuiaba")) {
                            i = 2;
                        }
                    }
                }
                break;
            }
        }
        paramCalendar.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        return paramCalendar;
        paramCalendar.setTimeZone(TimeZone.getTimeZone("America/Manaus"));
        return paramCalendar;
    }*/
}