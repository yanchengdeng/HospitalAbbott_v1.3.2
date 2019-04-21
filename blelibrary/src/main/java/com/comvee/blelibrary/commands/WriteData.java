package com.comvee.blelibrary.commands;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Build;

import com.comvee.blelibrary.BluetoothMode;


/**
 * Created by 黄嘉晖 on 2017/5/8.
 */

public class WriteData extends Command{
    private byte[] b;
    GroupCommandListener listener;
    BluetoothMode bluetoothMode;
    private boolean isEx = false;
    public WriteData(GroupCommandListener listener, Command paramCommand, byte[] data, BluetoothMode bluetoothMode, boolean isAutoRun) {
        super(paramCommand,bluetoothMode,isAutoRun);
        this.b = data;
        this.listener = listener;
        this.bluetoothMode = bluetoothMode;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void execute(BluetoothGatt paramBluetoothGatt) {
        if(isEx) {
            listener.moveNextCommand(this);
            return;
        }
        bluetoothMode.senData(b);
        if(listener != null)
        listener.moveNextCommand(this);
    }
}
