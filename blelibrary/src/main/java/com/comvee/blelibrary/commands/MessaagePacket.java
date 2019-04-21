package com.comvee.blelibrary.commands;


import com.comvee.blelibrary.crc16.CRCManager;

/**
 * Created by 黄嘉晖 on 2017/5/8.
 */

public class MessaagePacket {
    /**
     * 报文数据
     */
    private byte[] messageBytes;
    /**
     * 多少个包  因为最大20个字节 所以会分包
     */
    private int packetCount;
    /**
     * 默认构造函数
     */
    public MessaagePacket(){
        this.messageBytes = null;
        setPacketCount(1);
    }

    public MessaagePacket(byte[] paramArrayOfByte)
    {
        this.messageBytes = createICDByteArray(paramArrayOfByte);
        setPacketCount(1 + length() / 19);
    }

    public byte[] createICDByteArray(byte[] paramArrayOfByte)
    {
        int i = 7 + paramArrayOfByte.length;
        byte[] arrayOfByte1 = new byte[i];
        arrayOfByte1[0] = 2;
        arrayOfByte1[1] = ((byte)i);
        arrayOfByte1[2] = 0;
        arrayOfByte1[3] = 3;
        int j = paramArrayOfByte.length;
        int k = 0;
        int i3;
        int m = 4;
        for (; k < j; m = i3)
        {
            int i2 = paramArrayOfByte[k];
            i3 = m + 1;
            arrayOfByte1[m] = (byte) i2;
            k++;
        }
        int n = m + 1;
        arrayOfByte1[m] = 3;
        byte[] arrayOfByte2 = new CRCManager().crc16ccitt(arrayOfByte1);
        int i1 = n + 1;
        arrayOfByte1[n] = arrayOfByte2[0];
        arrayOfByte1[i1] = arrayOfByte2[1];
        return arrayOfByte1;
    }

    public int length()
    {
        byte[] arrayOfByte = this.messageBytes;
        int i = 0;
        if (arrayOfByte != null) {
            i = this.messageBytes.length;
        }
        return i;
    }

    public void setPacketCount(int paramInt)
    {
        this.packetCount = paramInt;
    }

    public void clearMessage()
    {
        this.messageBytes = null;
        setPacketCount(1);
    }




    public byte[] createPacketByteArray(int paramInt)
    {
        int i = 1 + length() / 19;
        byte[] arrayOfByte = null;
        int j = 0;
        int k = 0;
        if (paramInt < i)
        {
            j = paramInt * 19;
            k = Math.min(length() - j, 19);
            arrayOfByte = new byte[k + 1];
            if (paramInt != 0) {
                arrayOfByte[0] = 64;
                arrayOfByte[0] = ((byte)(paramInt | arrayOfByte[0]));
            }else{
                arrayOfByte[0] = 0;
                arrayOfByte[0] = ((byte)(i | arrayOfByte[0]));
            }
        }
        for (;;)
        {
            System.arraycopy(this.messageBytes, j, arrayOfByte, 1, k);
            return arrayOfByte;

        }
    }

    public byte[] getMessageBytes()
    {
        return this.messageBytes;
    }

    public int getPacketCount()
    {
        return this.packetCount;
    }

    public int getRequiredPacketCount()
    {
        return (-1 + (19 + length())) / 19;
    }

    public void setMessageBytes(byte[] paramArrayOfByte)
    {
        this.messageBytes = paramArrayOfByte;
    }

}
