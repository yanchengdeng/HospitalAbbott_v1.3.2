package com.comvee.blelibrary.util;


import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class RevealCalendar
        extends GregorianCalendar
{
    private static final long serialVersionUID = 1L;
    private boolean mIsLocal;

    public RevealCalendar()
    {
        this(false);
    }

    public RevealCalendar(long paramLong)
    {
        this(paramLong, false);
    }

    public RevealCalendar(long paramLong, boolean paramBoolean)
    {
        this(paramBoolean);
        setTimeInMillis(paramLong);
    }

    public RevealCalendar(String paramString)
            throws ParseException
    {
        this(paramString, false);
    }

    public RevealCalendar(String paramString, boolean paramBoolean)
            throws ParseException
    {
        this(paramBoolean);
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTime(CommonUtil.getDateFormat(paramBoolean).parse(paramString));
        set(localCalendar.get(YEAR), localCalendar.get(MONTH), localCalendar.get(DAY_OF_MONTH), localCalendar.get(HOUR_OF_DAY), localCalendar.get(MINUTE), localCalendar.get(SECOND));
    }

    public RevealCalendar(boolean paramBoolean)
    {
        this.mIsLocal = paramBoolean;
        if (!paramBoolean)
        {
            setTimeZone(TimeZone.getTimeZone("UTC"));
            computeTime();
        }
    }

    /*private boolean isSpecificGMTDayLight()
    {
        String str;
        int i = 0;
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
                    return false;
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
        return true;
    }*/

    private Calendar setCalendar(Calendar paramCalendar, int paramInt)
    {
        paramCalendar.setTimeInMillis(getTimeInMillis());
        paramCalendar.set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY) - paramInt);
        paramCalendar.set(Calendar.MINUTE, get(Calendar.MINUTE));
        paramCalendar.set(Calendar.SECOND, get(Calendar.SECOND));
        paramCalendar.set(Calendar.YEAR, get(Calendar.YEAR));
        paramCalendar.set(Calendar.MONTH, get(Calendar.MONTH));
        paramCalendar.set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH));
        return paramCalendar;
    }

    /*public Calendar getChartGMT()
    {

        for (int i = 1;; i = 0)
        {
            Object localObject = this;
            if (!this.mIsLocal) {
                localObject = setCalendar(DateUtilsLifescan.removeDayLightOfCalendar(Calendar.getInstance()), i);
            }
            return (Calendar)localObject;
        }
    }*/

    public Calendar getGMT()
    {
        Object localObject = this;
        if (!this.mIsLocal) {
            localObject = setCalendar(Calendar.getInstance(), 0);
        }
        return (Calendar)localObject;
    }
}