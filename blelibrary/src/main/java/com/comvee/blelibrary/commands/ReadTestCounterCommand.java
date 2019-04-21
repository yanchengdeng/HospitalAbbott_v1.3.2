package com.comvee.blelibrary.commands;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.comvee.blelibrary.BluetoothMode;


/**
 * Created by xiaoxinI2000 on 2017/5/10.
 */

public class ReadTestCounterCommand extends Command {
    private GroupCommandListener listener;
    public static final byte[] COMMAND_PORTION_READ_TEST_COUNTER = { 10, 2, 6 };
    /**
     * @param paramCommand
     * @param bluetoothMode
     * @param isAutoRun
     */
    public ReadTestCounterCommand(GroupCommandListener listener, Command paramCommand, BluetoothMode bluetoothMode, boolean isAutoRun) {
        super(paramCommand, bluetoothMode, isAutoRun);
        this.listener = listener;
    }

    @Override
    public void execute(BluetoothGatt paramBluetoothGatt) {
        MessaagePacket icdMessageIn = new MessaagePacket();
        MessaagePacket icdMessageOut = new MessaagePacket(COMMAND_PORTION_READ_TEST_COUNTER);
        insertCommand(new ReadMessagePacket(icdMessageIn,listener,new ProcessTestCount(null,bluetoothMode,true,icdMessageIn),bluetoothMode,false));
        for(int packetCount = (icdMessageOut.getRequiredPacketCount() - 0x1); packetCount >= 0; packetCount = packetCount - 0x1) {
            insertCommand(new WriteData(listener,new ReadMessagePacket(icdMessageIn,listener,null,bluetoothMode,false),icdMessageOut.createPacketByteArray(packetCount),bluetoothMode,true));
        }
        listener.moveNextCommand(this);
    }

    public class ProcessTestCount extends Command{

        private MessaagePacket messaagePacket;

        public ProcessTestCount(Command paramCommand, BluetoothMode bluetoothMode, boolean isAutoRun,MessaagePacket messaagePacket) {
            super(paramCommand, bluetoothMode, isAutoRun);
            this.messaagePacket = messaagePacket;
        }

        @Override
        public void execute(BluetoothGatt paramBluetoothGatt) {
            byte[] byteArrayMessage = new BleMeterSerialData().parse(messaagePacket.getMessageBytes());
            ReadTestCounterCommand.this.listener.onReadTestCount(BleMeterSerialData.byteArrayToInt(byteArrayMessage));
            ReadTestCounterCommand.this.listener.moveNextCommand(this);
        }

    }

}
