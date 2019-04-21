package com.comvee.blelibrary.util;

/**
 * Created by xiaoxinI2000 on 2017/5/10.
 */

import java.io.Serializable;
import java.util.Arrays;

// Referenced classes of package com.lifescan.reveal.entity:
//            BaseTime, sfloat, SensorStatusAnnunciation

public class GlucoseMeasurement
        implements Serializable
{

    public GlucoseMeasurement(int i, long l, byte byte0, sfloat sfloat1, byte byte1, byte byte2,
                              SensorStatusAnnunciation sensorstatusannunciation)
    {
        timeOffsetPresent = false;
        glucoseConcentrationPresent = false;
        sensorStatusPresent = false;
        contextInformationFollows = false;
        sequenceNumber = i;
        baseTime = new BaseTime(l);
        timeOffset = 0;
        timeOffsetPresent = true;
        glucoseConcentrationUnit = byte0;
        glucoseConcentration = sfloat1;
        type = byte1;
        sampleLocation = byte2;
        glucoseConcentrationPresent = true;
        sensorStatusAnnunciation = sensorstatusannunciation;
        sensorStatusPresent = true;
    }

    public GlucoseMeasurement(byte abyte0[])
    {
        timeOffsetPresent = false;
        glucoseConcentrationPresent = false;
        sensorStatusPresent = false;
        contextInformationFollows = false;
        byte byte0 = abyte0[0];
        int i = 0 + 1;
        boolean flag;
        boolean flag1;
        byte byte1;
        boolean flag2;
        boolean flag3;
        int j;
        byte abyte1[];
        if((byte0 & 1) != 0)
            flag = true;
        else
            flag = false;
        timeOffsetPresent = flag;
        if((byte0 & 2) != 0)
            flag1 = true;
        else
            flag1 = false;
        glucoseConcentrationPresent = flag1;
        if((byte0 & 4) == 0)
            byte1 = 0;
        else
            byte1 = 1;
        glucoseConcentrationUnit = byte1;
        if((byte0 & 8) != 0)
            flag2 = true;
        else
            flag2 = false;
        sensorStatusPresent = flag2;
        if((byte0 & 16) != 0)
            flag3 = true;
        else
            flag3 = false;
        contextInformationFollows = flag3;
        sequenceNumber = (abyte0[2] << 8) + abyte0[i];
        j = i + 2;
        abyte1 = new byte[7];
        for(int k = 0; k < 7; k++)
        {
            abyte1[k] = abyte0[j];
            j++;
        }

        baseTime = new BaseTime(abyte1);
        if(timeOffsetPresent)
        {
            timeOffset = (abyte0[j] << 8) + abyte0[j + 1];
            j += 2;
        }
        if(glucoseConcentrationPresent)
        {
            glucoseConcentration = new sfloat(Arrays.copyOfRange(abyte0, j, j + 2));
            j += 2;
            type = (byte)(15 & abyte0[j]);
            sampleLocation = (byte)((240 & abyte0[j]) >> 4);
        }
        if(sensorStatusPresent)
        {
            byte abyte2[] = new byte[2];
            abyte2[0] = abyte0[j + 1];
            abyte2[1] = abyte0[j];
            sensorStatusAnnunciation = new SensorStatusAnnunciation(abyte2);
        }
    }

    public BaseTime getBaseTime()
    {
        return baseTime;
    }

    public float getGlucoseConcentration()
    {
        return glucoseConcentration.getAsFloat();
    }

    public float getGlucoseConcentrationMain()
    {
        return glucoseConcentration.getMantissa();
    }

    public float getGlucoseConcentrationInMGDL()
    {
        sfloat sfloat1 = new sfloat(glucoseConcentration.getExponent(), glucoseConcentration.getMantissa());
        if(glucoseConcentrationUnit == 0)
        {
            sfloat1.setExponent((byte)(5 + sfloat1.getExponent()));
            return (float) Math.floor(sfloat1.getAsFloat());
        } else
        {
            sfloat1.setExponent((byte)(3 + sfloat1.getExponent()));
            return (float) Math.floor(0.5F + sfloat1.getAsFloat() / 0.0555F);
        }
    }

    public float getGlucoseConcentrationInMMOL()
    {
        sfloat sfloat1 = new sfloat(glucoseConcentration.getExponent(), glucoseConcentration.getMantissa());
        if(glucoseConcentrationUnit == 1)
        {
            sfloat1.setExponent((byte)(3 + sfloat1.getExponent()));
            return (float)(Math.floor(10F * (0.05F + sfloat1.getAsFloat())) / 10D);
        } else
        {
            sfloat1.setExponent((byte)(5 + sfloat1.getExponent()));
            return (float)(Math.floor(0.5F + 0.555F * sfloat1.getAsFloat()) / 10D);
        }
    }

    public byte getGlucoseConcentrationUnit()
    {
        return glucoseConcentrationUnit;
    }

    public byte getSampleLocation()
    {
        return sampleLocation;
    }

    public SensorStatusAnnunciation getSensorStatusAnnunciation()
    {
        return sensorStatusAnnunciation;
    }

    public int getSequenceNumber()
    {
        return sequenceNumber;
    }

    public int getTimeOffset()
    {
        return timeOffset;
    }

    public byte getType()
    {
        return type;
    }

    public boolean isGlucoseConcentrationPresent()
    {
        return glucoseConcentrationPresent;
    }

    public void setGlucoseConcentrationHigh()
    {
        glucoseConcentration.setMantissa((short)601);
    }

    public void setGlucoseConcentrationLow()
    {
        glucoseConcentration.setMantissa((short)19);
    }

    public String toString()
    {
        StringBuilder stringbuilder = (new StringBuilder()).append("GlucoseMeasurement [Flags {");
        String s;
        String s1;
        StringBuilder stringbuilder1;
        String s2;
        String s3;
        StringBuilder stringbuilder2;
        String s4;
        String s5;
        StringBuilder stringbuilder3;
        String s6;
        String s7;
        StringBuilder stringbuilder4;
        String s8;
        String s9;
        String s10;
        if(timeOffsetPresent)
            s = "TIME_OFFSET<true>, ";
        else
            s = "TIME_OFFSET<false>, ";
        s1 = stringbuilder.append(s).toString();
        stringbuilder1 = (new StringBuilder()).append(s1);
        if(glucoseConcentrationPresent)
            s2 = "GLUCOSE<true>, ";
        else
            s2 = "GLUCOSE<false>, ";
        s3 = stringbuilder1.append(s2).toString();
        stringbuilder2 = (new StringBuilder()).append(s3);
        if(glucoseConcentrationUnit == 0)
            s4 = "UNITS<kg/L>, ";
        else
            s4 = "UNITS<mol/L>, ";
        s5 = stringbuilder2.append(s4).toString();
        stringbuilder3 = (new StringBuilder()).append(s5);
        if(sensorStatusPresent)
            s6 = "SENSOR_STATUS<true>, ";
        else
            s6 = "SENSOR_STATUS<false>, ";
        s7 = stringbuilder3.append(s6).toString();
        stringbuilder4 = (new StringBuilder()).append(s7);
        if(contextInformationFollows)
            s8 = "CONTEXT<true>}, ";
        else
            s8 = "CONTEXT<false>}, ";
        s9 = stringbuilder4.append(s8).toString();
        s10 = (new StringBuilder()).append(s9).append("sequenceNumber=").append(sequenceNumber).append(", ").append("baseTime=").append(baseTime.toString()).toString();
        if(timeOffsetPresent)
            s10 = (new StringBuilder()).append(s10).append(", timeOffset=").append(timeOffset).toString();
        if(glucoseConcentrationPresent)
            s10 = (new StringBuilder()).append(s10).append(", glucoseConcentration=").append(glucoseConcentration.getAsFloat()).append(", type=").append(type).append(", sampleLocation=").append(sampleLocation).toString();
        if(sensorStatusPresent)
            s10 = (new StringBuilder()).append(s10).append(", sensorStatusAnnunciation=").append(sensorStatusAnnunciation).toString();
        if(contextInformationFollows)
            s10 = (new StringBuilder()).append(s10).append("").toString();
        return s10;
    }

    public static final int AUTO = 0;
    public static final byte GLUCOSE_CONCENTRATION_UNIT_KG_X_L = 0;
    public static final byte GLUCOSE_CONCENTRATION_UNIT_MOL_X_L = 1;
    public static final short GLUCOSE_HIGH = 601;
    public static final float GLUCOSE_LIMIT_HIGH = 600F;
    public static final float GLUCOSE_LIMIT_LOW = 20F;
    public static final short GLUCOSE_LOW = 19;
    public static final int MANUAL = 1;
    public static final byte SAMPLE_LOCATION_ALTERNATE_SITE = 2;
    public static final byte SAMPLE_LOCATION_CONTROL_SOLUTION = 4;
    public static final byte SAMPLE_LOCATION_EARLOBE = 3;
    public static final byte SAMPLE_LOCATION_FINGER = 1;
    public static final byte SAMPLE_LOCATION_NOT_AVALIABLE = 15;
    public static final byte TYPE_ARTERIAL_PLASMA = 6;
    public static final byte TYPE_ARTERIAL_WHOLE_BLOOD = 5;
    public static final byte TYPE_CAPILLARY_PLASMA = 2;
    public static final byte TYPE_CAPILLARY_WHOLE_BLOOD = 1;
    public static final byte TYPE_CONTROL_SOLUTION = 10;
    public static final byte TYPE_INTERSTITIAL_FLUID = 9;
    public static final byte TYPE_UNDETERMINED_PLASMA = 8;
    public static final byte TYPE_UNDETERMINED_WHOLE_BLOOD = 7;
    public static final byte TYPE_VENOUS_PLASMA = 4;
    public static final byte TYPE_VENOUS_WHOLE_BLOOD = 3;
    private static final long serialVersionUID = 1L;
    private BaseTime baseTime;
    private boolean contextInformationFollows;
    private sfloat glucoseConcentration;
    private boolean glucoseConcentrationPresent;
    private byte glucoseConcentrationUnit;
    private byte sampleLocation;
    private SensorStatusAnnunciation sensorStatusAnnunciation;
    private boolean sensorStatusPresent;
    private int sequenceNumber;
    private int timeOffset;
    private boolean timeOffsetPresent;
    private byte type;
}