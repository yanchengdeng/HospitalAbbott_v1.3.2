package com.comvee.blelibrary.commands;


import com.comvee.blelibrary.crc16.CRCManager;

/**
 * Created by xiaoxinI2000 on 2017/5/8.
 */


public class BleMeterSerialData {
    public static String byteArrayToHexaStr(byte[] paramArrayOfByte)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < paramArrayOfByte.length; i++)
        {
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Byte.valueOf(paramArrayOfByte[i]);
            localStringBuilder.append(String.format("%02X", arrayOfObject));
            localStringBuilder.append(" ");
        }
        return localStringBuilder.toString();
    }

    public static byte[] hexaStrToByteArray(String paramString)
    {
        int i = paramString.length();
        if (paramString.codePointBefore(i) == 0)
            i--;
        if (i % 2 == 1)
            paramString = "0" + paramString;
        int j = i / 2;
        byte[] arrayOfByte = new byte[j];
        for (int k = 0; k < j; k++)
            arrayOfByte[k] = (byte)(0xFF & (Character.digit(paramString.charAt(k * 2), 16) << 4 | Character.digit(paramString.charAt(1 + k * 2), 16)));
        return arrayOfByte;
    }

    public static int byteArrayToInt(byte[] paramArrayOfByte)
    {
        int i = 0;
        for (int j = -1 + paramArrayOfByte.length;j >= 0;j--)
        {
            for (int k = 0; k < paramArrayOfByte.length; k++)
            {
                int m = Math.abs(j - k);
                i |= (0xFF & paramArrayOfByte[m]) << m * 8;
            }
        }
        return i;
    }

    public static String toHexString(String s)
    {
        String str="";
        for (int i=0;i<s.length();i++)
        {
            int ch = (int)s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    public static byte[] intToByteArray(int paramInt)
    {
        int paramInt2 = 4;
        byte[] arrayOfByte = new byte[paramInt2];
        for (int i = paramInt2 - 1; i>=0; i--)
        {
            for (int j = 0; j < paramInt2; j++)
            {
                int k = Math.abs(i - j);
                arrayOfByte[k] = (byte)(0xFF & paramInt >> k * 8);
            }
        }
        return arrayOfByte;
    }

    public static byte[] reverseByteArray(byte[] paramArrayOfByte)
    {
        byte[] arrayOfByte = null;
        if (paramArrayOfByte != null)
        {
            int i = paramArrayOfByte.length;
            arrayOfByte = new byte[i];
            for (int j = 0; j < i; j++)
                arrayOfByte[(-1 + (i - j))] = paramArrayOfByte[j];
        }
        return arrayOfByte;
    }

    public static byte[] parse(byte[] paramArrayOfByte){
        if (paramArrayOfByte[1] != paramArrayOfByte.length)
        {
            return null;
        }
        byte[] arrayOfByte = new byte[2];
        arrayOfByte[0] = paramArrayOfByte[(-2 + paramArrayOfByte.length)];
        arrayOfByte[1] = paramArrayOfByte[(-1 + paramArrayOfByte.length)];
        CRCManager crc = new CRCManager();
        if (!crc.verifyChecksum(paramArrayOfByte, arrayOfByte))
        {
            return null;
        }
        if (paramArrayOfByte[4] == 6) {
            return extractData(paramArrayOfByte);
        }
        return null;
    }

    private static byte[] extractData(byte[] paramArrayOfByte)
    {
        byte[] arrayOfByte = new byte[-8 + paramArrayOfByte.length];
        int i = 0;
        for (int j = 5; j < -3 + paramArrayOfByte.length; j++)
        {
            arrayOfByte[i] = paramArrayOfByte[j];
            i++;
        }
        return arrayOfByte;
    }


}
