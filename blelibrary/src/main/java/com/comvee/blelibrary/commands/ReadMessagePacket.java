package com.comvee.blelibrary.commands;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Build;
import android.util.Log;

import com.comvee.blelibrary.BluetoothMode;


/**
 * Created by 黄嘉晖 on 2017/5/8.
 */

public class ReadMessagePacket extends Command{
    /**
     * 命令集合回调
     */
    private GroupCommandListener listener;
    MessaagePacket mMessageIn;

    public ReadMessagePacket(MessaagePacket paramICDMessage, GroupCommandListener listener, Command command, BluetoothMode bluetoothMode, boolean isAutoRun)
    {
        super(command,bluetoothMode,isAutoRun);
        this.mMessageIn = paramICDMessage;
        this.listener = listener;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void execute(BluetoothGatt paramBluetoothGatt){
        byte[] arrayOfByte1;
        int i;
        int j;
        int k = 0;
        arrayOfByte1 = bluetoothMode.gattReadCharacteristic.getValue();
        i = (byte)(0xFFFFFFC0 & arrayOfByte1[0]);
        j = 0x3F & arrayOfByte1[0];
        if(this.mMessageIn!=null)
        k = this.mMessageIn.length();
        byte[] arrayOfByte2 = null;
        System.out.println("ReadMessagePacket re:" + BleMeterSerialData.byteArrayToHexaStr(arrayOfByte1));
        if(i == -128){
            System.out.println("ack");
            listener.moveNextCommand(this);
            return;
        }
        arrayOfByte2 = new byte[1];
        arrayOfByte2[0] = (byte)j;
        if(i != 0 && i != 64) {
            listener.moveNextCommand(this);
            return;
        }

        if(i == 0){//第一个包
            this.mMessageIn.setPacketCount(j);
            j = 0;
        }
        if (this.mMessageIn.getRequiredPacketCount() != j){
            arrayOfByte2[0] = (byte)(0xFFFFFFC0 | arrayOfByte2[0]);
        }else{
            arrayOfByte2[0] = (byte)(0xFFFFFF80 | arrayOfByte2[0]);
        }
        byte[] arrayOfByte3 = new byte[Math.max(k, -1 + (j * 19 + arrayOfByte1.length))];
        if (k > 0){
            System.arraycopy(this.mMessageIn.getMessageBytes(), 0, arrayOfByte3, 0, k);
        }
        System.arraycopy(arrayOfByte1, 1, arrayOfByte3, j * 19, -1 + arrayOfByte1.length);
        this.mMessageIn.setMessageBytes(arrayOfByte3);
        if (this.mMessageIn.getRequiredPacketCount() >= this.mMessageIn.getPacketCount()){
            bluetoothMode.byteArrayMessage = arrayOfByte3;
            insertCommand(new WriteData(listener,null,arrayOfByte2,bluetoothMode,true));
            Log.e("bluetoothcar","结束:"+BleMeterSerialData.byteArrayToHexaStr(arrayOfByte3));
            listener.moveNextCommand(this);
        }else{
            insertCommand(new ReadMessagePacket(mMessageIn,listener,null,bluetoothMode,false));
            insertCommand(new WriteData(listener,null,arrayOfByte2,bluetoothMode,true));
            Log.e("bluetoothcar","data"+BleMeterSerialData.byteArrayToHexaStr(arrayOfByte3));
            listener.moveNextCommand(this);
        }
    }

}
