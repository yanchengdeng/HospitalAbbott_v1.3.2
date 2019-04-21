package com.comvee.blelibrary.commands;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.os.Build;


import com.comvee.blelibrary.BluetoothMode;
import com.comvee.blelibrary.crc16.CRCManager;

import java.util.Arrays;

/**
 * Created by 黄嘉晖 on 2017/5/9.
 */

public class ProcessMeterDieID extends Command {
    GroupCommandListener listener;
    public ProcessMeterDieID(GroupCommandListener listener, Command paramCommand, BluetoothMode bluetoothMode, boolean isAutoRun) {
        super(paramCommand, bluetoothMode,isAutoRun);
        this.listener = listener;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void execute(BluetoothGatt paramBluetoothGatt) {
        byte[] parse = parse(bluetoothMode.byteArrayMessage);
        String str = "";
        for (int j = 0; j < parse.length; j += 2) {
            str = str + (char)BleMeterSerialData.byteArrayToInt(Arrays.copyOfRange(parse, j, j + 2));
        }
        bluetoothMode.byteArrayMessage = BleMeterSerialData.hexaStrToByteArray(str);
        if (bluetoothMode.byteArrayMessage.length == 8) {
            this.listener.moveNextCommand(this);
        }
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
