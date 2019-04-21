package com.comvee.hospitalabbott.ui.scan;

import com.comvee.hospitalabbott.base.BaseViewController;

public interface ScanDeviceViewController  extends BaseViewController {

    void onUsbAttached();//设备链接

    void onUsbDetached();//设备未链接


    void onOpenFail();//打开设备失败

    void onOpenSucess();//打开设备成功


    void onReadSucess(String bloodData,String recv);//读取成功

    void onReadFail();//读取失败

    void onReadDoing();//正在读取中
    void onOpenCameraSucess();//打开摄像头
}
