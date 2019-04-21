package com.comvee.blelibrary.commands;

import android.bluetooth.BluetoothGatt;

import com.comvee.blelibrary.BluetoothMode;


/**
 * Created by xiaoxinI2000 on 2017/5/11.
 */

public class SynSuccessCommand extends Command {
    private GroupCommandListener listener;
    public SynSuccessCommand(GroupCommandListener listener, Command paramCommand, BluetoothMode bluetoothMode, boolean isAutoRun) {
        super(paramCommand, bluetoothMode, isAutoRun);
        this.listener = listener;
    }

    @Override
    public void execute(BluetoothGatt paramBluetoothGatt) {
        listener.onSynSuccess();
        listener.moveNextCommand(this);
    }
}
