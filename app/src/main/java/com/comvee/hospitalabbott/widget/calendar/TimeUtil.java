package com.comvee.hospitalabbott.widget.calendar;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    public TimeUtil() {
    }

    public static final String fomateTime(long utd, String fomate) {
        SimpleDateFormat sdf = new SimpleDateFormat(fomate);
        return sdf.format(new Date(utd));
    }

    public static final String fomateTime(String fomateFrom, String fomateTo, String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(fomateFrom);
        Date date = sdf.parse(time);
        sdf = new SimpleDateFormat(fomateTo);
        return sdf.format(date);
    }

    /**
     * 只保留小时
     *
     * @param time
     * @param format
     * @return
     */
    public static final String formatTimeToHours(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(time);
            long timeTime = date.getTime();
            timeTime = timeTime - (30 * 60 * 1000);
            date.setTime(timeTime);
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(date);
    }

    public static final boolean isSameDay(Calendar c1, Calendar c2) {
        int y1 = c1.get(Calendar.YEAR);
        int y2 = c2.get(Calendar.YEAR);
        int d1 = c1.get(Calendar.DAY_OF_YEAR);
        int d2 = c2.get(Calendar.DAY_OF_YEAR);
        return y1 == y2 && d1 == d2;
    }

    public static final boolean isOffer24hour(long utc1, long utc2) {
        return Math.abs(utc1 - utc2) / 3600000L < 24L;
    }

    public static final int getOfferDay(long utc1, long utc2) {
        return (int) Math.ceil((double) ((float) Math.abs(utc1 - utc2) / 8.64E7F));
    }

    public static final boolean isSameDay(long utc1, long utc2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(utc1);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(utc2);
        return isSameDay(c1, c2);
    }

    public static final boolean isSameMouth(Calendar c1, Calendar c2) {
        int m1 = c1.get(Calendar.MONTH);
        int m2 = c2.get(Calendar.MONTH);
        int y1 = c1.get(Calendar.YEAR);
        int y2 = c2.get(Calendar.YEAR);
        return m1 == m2 && y1 == y2;
    }

    public static final boolean isSameMouth(long c1, long c2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(c1);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(c2);
        return isSameMouth(calendar1, calendar);
    }

    public static final long getUTC(String time, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(time);
        return date.getTime();
    }

    public static final boolean isBefore(long time1, long time2) {
        return time1 < time2;
    }

    public  static String getNormalTime(){
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = c1.getTime();
        return sdf.format(c1.getTime());
    }

    /**
     * @param strTime    strTime
     * @param formatType formatType
     * @return long
     * @throws ParseException ParseException
     */
    public static long stringToLong(String strTime, String formatType) {
        if (TextUtils.isEmpty(strTime)){
            return 0;
        }
        Date date = null; // String类型转成date类型
        try {
            date = stringToDate(strTime, formatType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    /**
     * @param strTime    strTime
     * @param formatType formatType
     * @return Date Date
     * @throws ParseException ParseException
     */
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    /**
     * @param date Date
     * @return long
     */
    public static long dateToLong(Date date) {
        return date.getTime();
    }

}
