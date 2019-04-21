package com.comvee.blelibrary.commands;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.comvee.blelibrary.BluetoothMode;


/**
 * Created by xiaoxinI2000 on 2017/5/10.
 */

public class StartGlucoseSyncCommand extends Command {
    private GroupCommandListener listener;
    public StartGlucoseSyncCommand(GroupCommandListener listener, Command paramCommand, BluetoothMode bluetoothMode, boolean isAutoRun) {
        super(paramCommand, bluetoothMode, isAutoRun);
        this.listener = listener;
    }

    @Override
    public void execute(BluetoothGatt paramBluetoothGatt) {
        int j;
        int k;
        j = listener.getTestCount();
        k = listener.getIndex();
        if(j <= 0 || k >= j){
            listener.moveNextCommand(this);
        }else{
            int l = listener.getRecordCount();
            if(k < j)
                insertCommand(new ReadGlucoseRecordCommand(Math.max(k + 1, -30 + (j - l)),listener,null,bluetoothMode,true));
            listener.moveNextCommand(this);
        }
    }
}
