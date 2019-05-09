package com.comvee.hospitalabbott.ui.detection;

import com.comvee.hospitalabbott.base.BaseViewController;

/**
 * Created by F011512088 on 2018/1/25.
 * @author dengyancheng
 * 新增 onReadFinish  表示读取完成
 */
public interface TestBloodViewApiController extends BaseViewController {

    void onUsbAttached();//设备链接

    void onUsbDetached();//设备未链接


    void onOpenFail();//打开设备失败

    void onOpenSucess();//打开设备成功


    void onReadSucess(String recv,String bloodData);//读取成功

    void onReadFinish();//读取完成

    void onReadFail();//读取失败

    void onReadDoing();//正在读取中

    void onFinishActivity();

    void onTestPaperNoPullOut();//试纸未拔出
    void log(String log);





}
