package com.comvee.blelibrary.commands;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.comvee.blelibrary.BluetoothMode;


/**
 * Created by 黄嘉晖 on 2017/5/9.
 */

public class ReadMeterDieIDCommand extends Command {
    private static final byte[] COMMAND_PORTION_READ_METER_DIE_ID ;
    static
    {
        COMMAND_PORTION_READ_METER_DIE_ID = new byte[] { -26, 2, 8 };
    }
    private GroupCommandListener listener;
    public ReadMeterDieIDCommand(GroupCommandListener groupCommandListener, Command paramCommand, BluetoothMode bluetoothMode, boolean isAutoRun) {
        super(paramCommand, bluetoothMode,isAutoRun);
        this.listener = groupCommandListener;
    }

    @Override
    public void execute(BluetoothGatt paramBluetoothGatt) {
        MessaagePacket localICDMessage1 = new MessaagePacket();
        MessaagePacket localICDMessage2 = new MessaagePacket(COMMAND_PORTION_READ_METER_DIE_ID);
        int i = -1 + localICDMessage2.getRequiredPacketCount();
        insertCommand(new WriteData(listener,new ReadMessagePacket(localICDMessage1,listener,new ReadMessagePacket(localICDMessage1,listener,new ProcessMeterDieID(listener,null,bluetoothMode,true),bluetoothMode,false),bluetoothMode,false),localICDMessage2.createPacketByteArray(i),bluetoothMode,true));
        listener.moveNextCommand(this);
    }
}
