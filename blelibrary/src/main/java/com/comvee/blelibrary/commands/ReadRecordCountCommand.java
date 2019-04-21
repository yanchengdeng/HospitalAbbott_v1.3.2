package com.comvee.blelibrary.commands;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.comvee.blelibrary.BluetoothMode;


/**
 * Created by 黄嘉晖 on 2017/5/10.
 */

public class ReadRecordCountCommand extends Command {
    private GroupCommandListener listener;
    public static final byte[] COMMAND_PORTION_READ_RECORD_COUNT = { 39, 0 };
    public ReadRecordCountCommand(GroupCommandListener listener, Command paramCommand, BluetoothMode bluetoothMode, boolean isAutoRun) {
        super(paramCommand, bluetoothMode,isAutoRun);
        this.listener = listener;
    }

    @Override
    public void execute(BluetoothGatt paramBluetoothGatt) {
        MessaagePacket messaagePacketIn = new MessaagePacket();
        MessaagePacket messaagePacketOut = new MessaagePacket(COMMAND_PORTION_READ_RECORD_COUNT);
        insertCommand(new ReadMessagePacket(messaagePacketIn,listener,new ProcessRecordCount(null,bluetoothMode,true,messaagePacketIn),bluetoothMode,false));
        for(int packetCount = (messaagePacketOut.getRequiredPacketCount() - 0x1); packetCount >= 0; packetCount = packetCount - 0x1) {
            insertCommand(new WriteData(listener,new ReadMessagePacket(messaagePacketIn,listener,null,bluetoothMode,false),messaagePacketOut.createPacketByteArray(packetCount),bluetoothMode,true));
        }
        listener.moveNextCommand(this);
    }

    public class ProcessRecordCount extends Command{

        private MessaagePacket messaagePacket;

        public ProcessRecordCount(Command paramCommand, BluetoothMode bluetoothMode, boolean isAutoRun,MessaagePacket messaagePacket) {
            super(paramCommand, bluetoothMode, isAutoRun);
            this.messaagePacket = messaagePacket;
        }

        @Override
        public void execute(BluetoothGatt paramBluetoothGatt) {
            byte[] byteArrayMessage = new BleMeterSerialData().parse(messaagePacket.getMessageBytes());
            ReadRecordCountCommand.this.listener.onReadRecordCount(BleMeterSerialData.byteArrayToInt(byteArrayMessage));
            ReadRecordCountCommand.this.listener.moveNextCommand(this);
        }



    }

}
