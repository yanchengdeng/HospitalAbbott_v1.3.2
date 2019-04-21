package com.comvee.blelibrary;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;


import com.comvee.blelibrary.commands.Command;
import com.comvee.blelibrary.commands.GroupCommandListener;
import com.comvee.blelibrary.commands.ReadMeterDieIDCommand;
import com.comvee.blelibrary.commands.ReadRecordCountCommand;
import com.comvee.blelibrary.commands.ReadTestCounterCommand;
import com.comvee.blelibrary.commands.SendPremiumModeCommand;
import com.comvee.blelibrary.commands.StartGlucoseSyncCommand;
import com.comvee.blelibrary.commands.SynSuccessCommand;
import com.comvee.blelibrary.commands.WriteData;
import com.comvee.blelibrary.util.GlucoseMeasurement;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 黄嘉晖 on 2017/5/8.
 */

public class SynTool {

    private Command nowCommand;
    private BluetoothMode bluetoothMode;

    public SynTool(BluetoothMode bl){
        bluetoothMode = bl;
    }

    /**
     * 同步回调事件
     */
    public interface SynListneer{
        public void onReadRecordCount(int count);
        public void onReadTestCount(int count);
        public void onGlucoseMeasurementFound(GlucoseMeasurement glucoseMeasurement);
        void onSynSuccess(int index);
        void onSynFailed();
    }
    SynListneer synListneer;
    /**
     * 同步
     */
    public void syn(BluetoothGatt gatt, final SynListneer synListneer,int index){
        this.synListneer = synListneer;
        synGroupCommandListener.moveNextCommand(null);//先移除所有命令
        synGroupCommandListener.stopTimeoOutTesting();
        synGroupCommandListener.index = index;
        bluetoothMode.subject(listneer);
        new ReadMeterDieIDCommand(synGroupCommandListener,
                new SendPremiumModeCommand(synGroupCommandListener,
                        new ReadRecordCountCommand(synGroupCommandListener,
                                new ReadTestCounterCommand(synGroupCommandListener,
                                        new StartGlucoseSyncCommand(synGroupCommandListener,
                                                new SynSuccessCommand(synGroupCommandListener,null,bluetoothMode,true),bluetoothMode,true),bluetoothMode,true),bluetoothMode,true),bluetoothMode,true),bluetoothMode,false).execute(gatt);
        synGroupCommandListener.startTimeoOutTesting();
    }

    BluetoothMode.Listneer listneer = new BluetoothMode.Listneer() {
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if(nowCommand != null) {
                bluetoothMode.gattReadCharacteristic = characteristic;
                nowCommand.execute(gatt);
            }
        }

        @Override
        public void writeNotify(byte[] bytes) {

        }
    };

    SynGroupCommandListener synGroupCommandListener = new SynGroupCommandListener();

    public class SynGroupCommandListener implements GroupCommandListener {
        public Timer timer;

        /**
         * 最后执行命令时间
         */
        private long time = 0;

        public void startTimeoOutTesting(){
            if(timer == null) {
                timer = new Timer();
            }else{
                timer.cancel();
                timer = new Timer();
            }
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Calendar calendar = Calendar.getInstance();
                    long nowTime = calendar.getTimeInMillis();
                    if(nowTime - time > 10000 && time != 0){
                        onSynFailed();
                    }
                }
            },5000,5000);
        }
        public void stopTimeoOutTesting(){
            if(timer != null) {
                timer.cancel();
            }
        }

        private int testCount = 0;

        private int RecordCount = 0;
        /**
         * 条数的索引值 完成后 这个第几条就是第几条
         */
        private int  index = 0;

        @Override
        public void moveNextCommand(Command command) {
            time = Calendar.getInstance().getTimeInMillis();
            if(command!= null &&command.next() != null) {
                nowCommand = command.next();
                if(nowCommand.isAutoRun()){
                    if(nowCommand instanceof WriteData){
                        nowCommand.execute(bluetoothMode.getBluetoothGatt());
                    }else{
                        nowCommand.execute(bluetoothMode.getBluetoothGatt());
                    }
                }
                return;
            }
            nowCommand = null;
        }

        @Override
        public void writeListener(byte b) {

        }

        @Override
        public void onReadRecordCount(int chount) {
            if(synListneer != null)
                synListneer.onReadRecordCount(chount);
        }

        @Override
        public void onReadTestCount(int chout) {
            testCount = chout;
            if(synListneer != null)
                synListneer.onReadTestCount(chout);
        }

        @Override
        public int getTestCount() {
            return testCount;
        }

        @Override
        public int getRecordCount() {
            return RecordCount;
        }

        @Override
        public void onGlucoseMeasurementFound(GlucoseMeasurement glucosemeasurement) {
            if(synListneer != null)
                synListneer.onGlucoseMeasurementFound(glucosemeasurement);
        }

        @Override
        public void onSynSuccess() {
            if(synListneer != null)
                synListneer.onSynSuccess(index);
            moveNextCommand(null);
            stopTimeoOutTesting();
        }

        @Override
        public void onSynFailed() {
            stopTimeoOutTesting();
        }
        @Override
        public void setIndex(int i){
            this.index = i;
        }
        @Override
        public int getIndex(){
            return index;
        }


    };







}
