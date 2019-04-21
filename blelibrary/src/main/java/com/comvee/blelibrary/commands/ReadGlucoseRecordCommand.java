package com.comvee.blelibrary.commands;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.comvee.blelibrary.BluetoothMode;
import com.comvee.blelibrary.util.GlucoseMeasurement;
import com.comvee.blelibrary.util.SensorStatusAnnunciation;
import com.comvee.blelibrary.util.sfloat;


/**
 * Created by xiaoxinI2000 on 2017/5/10.
 */

public class ReadGlucoseRecordCommand extends Command {
    /**
     *
     */
    private GroupCommandListener listener;

    private static final byte[] COMMAND_PORTION_GET_RECORD = { -77 };

    private int mIndex;

    public ReadGlucoseRecordCommand(int index, GroupCommandListener listener, Command paramCommand, BluetoothMode bluetoothMode, boolean isAutoRun) {
        super(paramCommand, bluetoothMode, isAutoRun);
        this.listener = listener;
        this.mIndex = index;
    }

    @Override
    public void execute(BluetoothGatt paramBluetoothGatt) {
        if(mIndex < listener.getTestCount()){
            listener.setIndex(listener.getIndex() + 1);
            insertCommand(new ReadGlucoseRecordCommand(listener.getIndex(),listener,null,bluetoothMode,true));
        }
        byte[] commandData = new byte[0x3];
        byte[] indexValue = BleMeterSerialData.intToByteArray(mIndex);
        commandData[0x0] = COMMAND_PORTION_GET_RECORD[0x0];
        commandData[0x1] = indexValue[0x0];
        commandData[0x2] = indexValue[0x1];
        MessaagePacket icdMessageIn = new MessaagePacket();
        MessaagePacket icdMessageOut = new MessaagePacket(commandData);
        insertCommand(new ReadMessagePacket(icdMessageIn,listener,new ProcessBGRecord(null,bluetoothMode,icdMessageIn,true),bluetoothMode,false));
        for(int packetCount = (icdMessageOut.getRequiredPacketCount() - 0x1); packetCount >= 0; packetCount = packetCount - 0x1) {
            insertCommand(new WriteData(listener,new ReadMessagePacket(icdMessageOut,listener,null,bluetoothMode,false),icdMessageOut.createPacketByteArray(packetCount),bluetoothMode,true));
        }
        listener.moveNextCommand(this);
    }

    private class ProcessBGRecord extends Command{

        private MessaagePacket messaagePacket;

        public ProcessBGRecord(Command paramCommand, BluetoothMode bluetoothMode,MessaagePacket messaagePacket, boolean isAutoRun) {
            super(paramCommand, bluetoothMode, isAutoRun);
            this.messaagePacket = messaagePacket;
        }

        @Override
        public void execute(BluetoothGatt paramBluetoothGatt) {
            bluetoothMode.byteArrayMessage = BleMeterSerialData.parse(messaagePacket.getMessageBytes());
            byte abyte0[] = messaagePacket.getMessageBytes();
            try{
            if(abyte0[14] == 0){
                if(abyte0[11] == 0){

                }
                byte abyte1[];
                byte abyte2[];
                byte abyte3[];
                GlucoseMeasurement glucosemeasurement;
                abyte1 = new byte[4];
                System.arraycopy(abyte0, 5, abyte1, 0, 4);
                abyte2 = new byte[2];
                System.arraycopy(abyte0, 9, abyte2, 0, 2);
                abyte3 = (new byte[] {0, 0});
                switch (abyte0[15]){
                    case 0:
                        break;
                    case 1:
                        abyte3[0] = 1;
                        break;
                    case 2:
                        abyte3[0] = 2;
                        break;
                    case 3:
                        abyte3[0] = 45;
                        break;
                    case 4:
                        abyte3[0] = 31;
                        break;
                    case 5:
                        abyte3[0] = 64;
                        break;
                    case 6:
                        abyte3[0] = -128;
                        break;
                    case 7:
                        abyte3[1] = 1;
                        break;
                }
                glucosemeasurement = new GlucoseMeasurement(mIndex, BleMeterSerialData.byteArrayToInt(abyte1),
                        (byte)0, new sfloat((byte)-5, (short)BleMeterSerialData.byteArrayToInt(abyte2)),
                        (byte)1, (byte)15, new SensorStatusAnnunciation(abyte3));
                if(glucosemeasurement.getGlucoseConcentrationInMGDL() >= 20F){
                    if(glucosemeasurement.getGlucoseConcentrationInMGDL() >= 600F){
                        glucosemeasurement.setGlucoseConcentrationHigh();
                    }
                }else{
                    if(glucosemeasurement.getGlucoseConcentrationInMGDL() <= 20F)
                    glucosemeasurement.setGlucoseConcentrationLow();
                }
                listener.onGlucoseMeasurementFound(glucosemeasurement);
            }
            }catch (Exception e){
                //该条已经被删除
            }
            listener.moveNextCommand(this);
        }
    }

}
