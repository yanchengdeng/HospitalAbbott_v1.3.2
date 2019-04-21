package com.comvee.hospitalabbott.ui.scan;

import android.util.Log;

import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.tool.ThreadHandler;
import com.comvee.hospitalabbott.tool.device.CH34xDriverUtil;

public class ScanDevicePresenter extends BasePresenter<ScanDeviceViewController> {

    private final ScanDeviceViewController viewModel;

    private boolean isOpen = false;
    // ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
    private int retval;


    public ScanDevicePresenter(ScanDeviceViewController viewModel) {
        super(viewModel);
        this.viewModel = viewModel;

    }

    @Override
    public void initData() {

    }


    @Override
    public void unBind() {

    }

    /**
     * 打开设备
     */
    public void openBloodDevice() {

        if (!isOpen) {

            if (retval == -1) {
                viewModel.onOpenFail();
                return;
            } else if (retval == 0) {
                if (!XTYApplication.driver.UartInit()) {//对串口设备进行初始化操作
                    viewModel.onOpenFail();
                    return;
                }
                isOpen = true;
                //初始化串口数据
                initConfig();
                viewModel.onOpenSucess();

            } else {
                viewModel.onOpenFail();
            }
        }
    }

    /**
     * 建立请求连接
     */
    public void requestDataConnect() {
        viewModel.onReadDoing();
       // bootDevice();
       /* ThreadHandler.postUiThread(new Runnable() {
            @Override
            public void run() {
                new readThread().start();//开线程读取数据
                writeInitApiData();//初始化主站请求建立连接帧
            }
        },1000);*/
        new readThread().start();//开线程读取数据
        writeInitApiData();//初始化主站请求建立连接帧
    }

    /**
     * 激活设备
     */
    public void bootDevice(){
        //写入初始化，固定返回随机帧
        byte[] to_send = CH34xDriverUtil.toByteArray("ff ff ff");
        //写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
        int retval = XTYApplication.driver.WriteData(to_send, to_send.length);
        if (retval < 0) {
            Log.e("bootDevice", "retval<0");
        } else {
            Log.e("bootDevice", "ffffff");
        }
    }


    /**
     * 设备是否连接
     */
    public void isConnect() {
        // ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
        if (!XTYApplication.driver.isConnected())
            retval = XTYApplication.driver.ResumeUsbList();
        if (XTYApplication.driver.isConnected()) {
            viewModel.onUsbAttached();
        } else {
            viewModel.onUsbDetached();
        }
    }

    private class readThread extends Thread {

        public void run() {

            byte[] buffer = new byte[64];

            while (true) {
                if (!isOpen) {
                    break;
                }

                final int length = XTYApplication.driver.ReadData(buffer, 64);
                if (length > 0) {

                    final String recv = CH34xDriverUtil.toHexString(buffer, length);

                    ThreadHandler.postUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //读取结果
                            if (!recv.contains("02 00 00 01 00 33 31")) {
                                Log.e("打开摄像头成功","==="+recv);
                                //将16进制转陈ASCII
                                String asciiString = CH34xDriverUtil.parseAscii(recv);
                                viewModel.onReadSucess(asciiString, recv);

                            }

                        }
                    });

                }
            }


        }
    }

    /**
     * 关闭设备，终止读取
     */
    public void closeDevice() {
        //设备一直打开着就行
//        XTYApplication.driver.CloseDevice();
        isOpen = false;
    }



    /**
     * 初始化主站请求建立连接帧
     */
    private void writeInitApiData() {
        //写入初始化，固定返回随机帧
        byte[] to_send = CH34xDriverUtil.toByteArray("7E 00 08 01 00 02 01 AB CD");
        //写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
        int retval = XTYApplication.driver.WriteData(to_send, to_send.length);

        if (retval < 0) {
            Log.e("onReadFail", "retval=====" + retval);
            viewModel.onReadFail();
        } else {
            viewModel.onOpenCameraSucess();
            Log.e("writeInitApiData", "writeInitApiData");
        }
    }


    /**
     * 配置串口波特率
     */
    private void initConfig() {
        int baudRate = 115200;
        byte dataBit = 8;
        byte stopBit = 1;
        byte parity = 0;
        byte flowControl = 0;
        XTYApplication.driver.SetConfig(baudRate, dataBit, stopBit, parity,
                flowControl);
    }
}
