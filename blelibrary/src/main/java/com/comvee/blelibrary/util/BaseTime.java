package com.comvee.blelibrary.util;

import java.io.Serializable;

// Referenced classes of package com.lifescan.reveal.entity:
//            RevealCalendar

public class BaseTime
        implements Serializable
{

    public BaseTime(long l)
    {
        year = 0;
        month = 0;
        day = 0;
        hours = 0;
        minutes = 0;
        seconds = 0;
        RevealCalendar revealcalendar = new RevealCalendar(946684800000L + 1000L * l);
        year = revealcalendar.get(1);
        month = 1 + revealcalendar.get(2);
        day = revealcalendar.get(5);
        hours = revealcalendar.get(11);
        minutes = revealcalendar.get(12);
        seconds = revealcalendar.get(13);
    }

    public BaseTime(byte abyte0[])
    {
        year = 0;
        month = 0;
        day = 0;
        hours = 0;
        minutes = 0;
        seconds = 0;
        int i = 255 & abyte0[1];
        year = 255 & abyte0[0] | i << 8;
        month = abyte0[2];
        day = abyte0[3];
        hours = abyte0[4];
        minutes = abyte0[5];
        seconds = abyte0[6];
    }

    public int getDay()
    {
        return day;
    }

    public String getDayString(){
        String strDay = "";
        if(day < 10){
            strDay += "0"+day;
        }else{
            strDay = day + "";
        }
        return strDay;
    }

    public int getHours()
    {
        return hours;
    }

    public String getHoursString(){
        String strDay = "";
        if(hours < 10){
            strDay += "0"+hours;
        }else{
            strDay = hours + "";
        }
        return strDay;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public String getMinutesString(){
        String strDay = "";
        if(minutes < 10){
            strDay += "0"+minutes;
        }else{
            strDay = minutes + "";
        }
        return strDay;
    }

    public int getMonth()
    {
        return month;
    }

    public String getMonthString(){
        String strDay = "";
        if(month < 10){
            strDay += "0"+month;
        }else{
            strDay = month + "";
        }
        return strDay;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public String getSecondsString(){
        String strDay = "";
        if(seconds < 10){
            strDay += "0"+seconds;
        }else{
            strDay = seconds + "";
        }
        return strDay;
    }

    public long getTimeInMillis()
    {
        RevealCalendar revealcalendar = new RevealCalendar();
        revealcalendar.set(1, year);
        revealcalendar.set(2, -1 + month);
        revealcalendar.set(5, day);
        revealcalendar.set(11, hours);
        revealcalendar.set(12, minutes);
        revealcalendar.set(13, seconds);
        revealcalendar.set(14, 0);
        return revealcalendar.getTimeInMillis();
    }

    public int getYear()
    {
        return year;
    }

    public String toString()
    {
        return (new StringBuilder()).append("DateTime [year=").append(year).append(", month=").append(month).append(", day=").append(day).append(", hours=").append(hours).append(", minutes=").append(minutes).append(", seconds=").append(seconds).append("]").toString();
    }

    private static final long serialVersionUID = 1L;
    private int day;
    private int hours;
    private int minutes;
    private int month;
    private int seconds;
    private int year;
}