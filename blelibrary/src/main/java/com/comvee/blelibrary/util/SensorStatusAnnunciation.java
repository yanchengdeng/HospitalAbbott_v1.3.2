package com.comvee.blelibrary.util;

/**
 * Created by xiaoxinI2000 on 2017/5/10.
 */

import java.io.Serializable;

public class SensorStatusAnnunciation
        implements Serializable
{

    public SensorStatusAnnunciation(byte abyte0[])
    {
        boolean flag = true;
        byte byte0 = abyte0[0];
        byte byte1 = abyte0[flag ? 0x01 : 0x00];
        boolean flag1;
        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        boolean flag6;
        boolean flag7;
        boolean flag8;
        boolean flag9;
        boolean flag10;
        boolean flag11;
        if((byte0 & 1) != 0)
            flag1 = flag;
        else
            flag1 = false;
        batteryLow = flag1;
        if((byte0 & 2) != 0)
            flag2 = flag;
        else
            flag2 = false;
        sensorMalfunction = flag2;
        if((byte0 & 4) != 0)
            flag3 = flag;
        else
            flag3 = false;
        bloodInsufficient = flag3;
        if((byte0 & 8) != 0)
            flag4 = flag;
        else
            flag4 = false;
        stripError = flag4;
        if((byte0 & 16) != 0)
            flag5 = flag;
        else
            flag5 = false;
        stripIncorrect = flag5;
        if((byte0 & 32) != 0)
            flag6 = flag;
        else
            flag6 = false;
        resultHigher = flag6;
        if((byte0 & 64) != 0)
            flag7 = flag;
        else
            flag7 = false;
        resultLower = flag7;
        if((byte0 & 128) != 0)
            flag8 = flag;
        else
            flag8 = false;
        tempTooHigh = flag8;
        if((byte1 & 1) != 0)
            flag9 = flag;
        else
            flag9 = false;
        tempTooLow = flag9;
        if((byte1 & 2) != 0)
            flag10 = flag;
        else
            flag10 = false;
        stripPulled = flag10;
        if((byte1 & 4) != 0)
            flag11 = flag;
        else
            flag11 = false;
        generalFault = flag11;
        if((byte1 & 8) == 0)
            flag = false;
        timeFault = flag;
    }

    public String toString()
    {
        return (new StringBuilder()).append("SensorStatusAnnunciation [batteryLow=").append(batteryLow).append(", sensorMalfunction=").append(sensorMalfunction).append(", bloodInsufficient=").append(bloodInsufficient).append(", stripError=").append(stripError).append(", stripIncorrect=").append(stripIncorrect).append(", resultHigher=").append(resultHigher).append(", resultLower=").append(resultLower).append(", tempTooHigh=").append(tempTooHigh).append(", tempTooLow=").append(tempTooLow).append(", stripPulled=").append(stripPulled).append(", generalFault=").append(generalFault).append(", timeFault=").append(timeFault).append("]").toString();
    }

    private static final long serialVersionUID = 1L;
    private boolean batteryLow;
    private boolean bloodInsufficient;
    private boolean generalFault;
    private boolean resultHigher;
    private boolean resultLower;
    private boolean sensorMalfunction;
    private boolean stripError;
    private boolean stripIncorrect;
    private boolean stripPulled;
    private boolean tempTooHigh;
    private boolean tempTooLow;
    private boolean timeFault;
}