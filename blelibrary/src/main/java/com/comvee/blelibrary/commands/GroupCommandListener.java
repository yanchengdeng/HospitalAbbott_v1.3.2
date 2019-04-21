package com.comvee.blelibrary.commands;

import android.bluetooth.BluetoothGattCharacteristic;

import com.comvee.blelibrary.util.GlucoseMeasurement;

/**
 * 当多条命令时执行的事件
 * Created by 黄嘉晖 on 2017/5/8.
 */

public interface GroupCommandListener {

    /**
     * 移动到下一条命令
     */
    public void moveNextCommand(Command command);

    /**
     * 发送数据时回调
     * @param b
     */
    public void writeListener(byte b);

    /**
     * 读取到条数
     * @param chount
     */
    public void onReadRecordCount(int chount);

    public void onReadTestCount(int chout);

    /**
     * 获取条数
     * @return
     */
    public int getTestCount();

    /**
     * 获取条数
     * @return
     */
    public int getRecordCount();

    /**
     * 发现血糖数据
     * @param glucosemeasurement
     */
    void onGlucoseMeasurementFound(GlucoseMeasurement glucosemeasurement);

    public void onSynSuccess();

    public void onSynFailed();

    public int getIndex();

    public void setIndex(int i);


}
