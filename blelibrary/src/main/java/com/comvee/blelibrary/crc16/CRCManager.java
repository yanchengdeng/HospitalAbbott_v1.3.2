package com.comvee.blelibrary.crc16;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by 黄嘉晖 on 2017/5/8.
 */

public class CRCManager {

    private static final int POLYNOMIAL_BE = 4129;


    public static byte[] crc16ccitt(byte abyte0[])
    {
        byte abyte1[] = Arrays.copyOf(abyte0, -2 + abyte0.length);
        int i = 65535;
        int j = abyte1.length;
        for (int k = 0; k < j; k++)
        {
            byte byte0 = abyte1[k];
            int l = 0;
            while (l < 8)
            {
                boolean flag;
                boolean flag1;
                if ((1 & byte0 >> 7 - l) == 1)
                    flag = true;
                else
                    flag = false;
                if ((1 & i >> 15) == 1)
                    flag1 = true;
                else
                    flag1 = false;
                i <<= 1;
                if (flag1 ^ flag)
                    i ^= 0x1021;
                l++;
            }
        }
        return createByteArray(i & 0xffff);
    }

    public boolean verifyChecksum(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
        byte[] arrayOfByte = crc16ccitt(paramArrayOfByte1);
        if (arrayOfByte.length != paramArrayOfByte2.length) {
            return false;
        }
        for (int i = 0;; i++)
        {
            if (i >= arrayOfByte.length) {
                return true;
            }
            if (arrayOfByte[i] != paramArrayOfByte2[i]) {
                break;
            }
        }
        label42:
        return true;
    }

    private static byte[] createByteArray(int i)
    {
        ByteBuffer bytebuffer = ByteBuffer.allocate(4);
        bytebuffer.order(ByteOrder.LITTLE_ENDIAN);
        bytebuffer.putInt(i);
        byte abyte0[] = new byte[2];
        abyte0[0] = bytebuffer.array()[0];
        abyte0[1] = bytebuffer.array()[1];
        return abyte0;
    }

}
