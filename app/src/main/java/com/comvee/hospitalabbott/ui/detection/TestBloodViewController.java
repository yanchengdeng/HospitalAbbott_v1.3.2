package com.comvee.hospitalabbott.ui.detection;

import com.comvee.hospitalabbott.base.BaseViewController;
import com.comvee.hospitalabbott.bean.BLEBloodModel;

/**
 * Created by F011512088 on 2018/1/25.
 */
public interface TestBloodViewController extends BaseViewController {

    void onTestHand();

    void onTestDoing();

    void onTestError(String errorStr);

    void onBloodMode(BLEBloodModel bleBloodModel);

    void onBLEOpening(); //开启中

    void onBLEStateON();//蓝牙开启

    void onBLEStateOFF();//蓝牙关闭

    void onConnecting();//蓝牙连接中

    void onConnectSuccess();//蓝牙连接成功

    void onConnectFailure(String errorStr);//蓝牙连接失败

    void onDisconnect(boolean isActive);//蓝牙状态改变

    void onCompleteBtnEnable(boolean enable);

}
