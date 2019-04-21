package com.comvee.blelibrary.util;

import java.io.Serializable;

public class sfloat
        implements Serializable
{

    public sfloat(byte byte0, short word0)
    {
        NaN = 2047;
        NRes = 2048;
        PInf = 2046;
        NInf = 2050;
        Res = 2049;
        exponent = (byte)(byte0 & 15);
        if((byte0 & 8) > 0)
            exponent = (byte)(240 | exponent);
        mantissa = (short)(word0 & 4095);
        if((word0 & 2048) > 0)
            mantissa = (short)(61440 | mantissa);
    }

    public sfloat(byte abyte0[])
    {
        this(abyte0, true);
    }

    public sfloat(byte abyte0[], boolean flag)
    {
        NaN = 2047;
        NRes = 2048;
        PInf = 2046;
        NInf = 2050;
        Res = 2049;
        if(abyte0.length == 2)
        {
            int i = 1;
            int j = 0;
            if(flag)
            {
                i = 0;
                j = 1;
            }
            exponent = (byte)((240 & abyte0[j]) >> 4);
            if((128 & abyte0[j]) > 0)
                exponent = (byte)(240 | exponent);
            mantissa = (short)((15 & abyte0[j]) << 8 | 255 & abyte0[i]);
            if((8 & abyte0[j]) > 0)
                mantissa = (short)(61440 | mantissa);
            return;
        } else
        {
            exponent = 0;
            mantissa = 2047;
            return;
        }
    }

    public float getAsFloat()
    {
        if(!isNan())
        {
            if(isPInf())
                return (1.0F / 0.0F);
            if(isNInf())
                return (-1.0F / 0.0F);
            if(!isNRes() && !isRes()) {
                float f = (float) ((double) mantissa * Math.pow(10D, exponent));
                return f;
            }
        }
        return (0.0F / 0.0F);
    }

    public double getAsDouble()
    {
        if(!isNan())
        {
            if(isPInf())
                return (1.0F / 0.0F);
            if(isNInf())
                return (-1.0F / 0.0F);
            if(!isNRes() && !isRes()) {
                double f = ((double) mantissa * Math.pow(10D, exponent));
                return f;
            }
        }
        return (0.0F / 0.0F);
    }

    public byte getExponent()
    {
        return exponent;
    }

    public short getMantissa()
    {
        return mantissa;
    }

    public boolean isNInf()
    {
        boolean flag = true;
        boolean flag1;
        if(exponent == 0)
            flag1 = flag;
        else
            flag1 = false;
        if(mantissa != 2050)
            flag = false;
        return flag1 & flag;
    }

    public boolean isNRes()
    {
        boolean flag = true;
        boolean flag1;
        if(exponent == 0)
            flag1 = flag;
        else
            flag1 = false;
        if(mantissa != 2048)
            flag = false;
        return flag1 & flag;
    }

    public boolean isNan()
    {
        boolean flag = true;
        boolean flag1;
        if(exponent == 0)
            flag1 = flag;
        else
            flag1 = false;
        if(mantissa != 2047)
            flag = false;
        return flag1 & flag;
    }

    public boolean isPInf()
    {
        boolean flag = true;
        boolean flag1;
        if(exponent == 0)
            flag1 = flag;
        else
            flag1 = false;
        if(mantissa != 2046)
            flag = false;
        return flag1 & flag;
    }

    public boolean isRes()
    {
        boolean flag = true;
        boolean flag1;
        if(exponent == 0)
            flag1 = flag;
        else
            flag1 = false;
        if(mantissa != 2049)
            flag = false;
        return flag1 & flag;
    }

    public void setExponent(byte byte0)
    {
        exponent = byte0;
    }

    public void setMantissa(short word0)
    {
        mantissa = word0;
    }

    private static final long serialVersionUID = 1L;
    public final short NInf;
    public final short NRes;
    public final short NaN;
    public final short PInf;
    public final short Res;
    private byte exponent;
    private short mantissa;
}