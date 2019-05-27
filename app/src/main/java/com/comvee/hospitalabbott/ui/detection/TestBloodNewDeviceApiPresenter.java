package com.comvee.hospitalabbott.ui.detection;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.comvee.greendao.gen.TestInfoDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.bean.BLEBloodModel;
import com.comvee.hospitalabbott.bean.BloodRangeBean;
import com.comvee.hospitalabbott.bean.TestInfo;
import com.comvee.hospitalabbott.helper.RangeHelper;
import com.comvee.hospitalabbott.helper.RecordHelper;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.network.rxbus.BloodUpdateRxModel;
import com.comvee.hospitalabbott.network.rxbus.IndexUiRxModel;
import com.comvee.hospitalabbott.network.rxbus.RxBus;
import com.comvee.hospitalabbott.tool.ActivityCommunicationUtil;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.tool.ThreadHandler;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.tool.device.CH34xDriverUtil;
import com.comvee.hospitalabbott.ui.main.MainActivity;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by F011512088 on 2018/1/25.
 * @author dengyancheng
 * 修改2019-04-22
 */
public class TestBloodNewDeviceApiPresenter extends BasePresenter<TestBloodViewApiController> {


    private final TestBloodViewApiController viewModel;

    private boolean isSecondCode = false;
    private boolean isOpen = false;
    private Lock lock = new ReentrantLock();
    private Lock lockScan = new ReentrantLock();
    // ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
    private int retval;

//    连续不间断的一天血糖数据就是96个   采集14天   14*96=1344   换16进制 --》0540
//    public static String SEND_ORDER = "68 01 68 06 00 89 16";
    public static String SEND_ORDER = "68 01 68 01 05 40 89 16";
//    public static String SEND_ORDER = "68 01 68 03 00 06 89 16";



    public TestBloodNewDeviceApiPresenter(TestBloodViewApiController viewModel) {
        super(viewModel);
        this.viewModel = viewModel;

    }


    @Override
    public void unBind() {

    }

    /**
     * 打开设备
     */
    public void openBloodDevice() {
        viewModel.log("设备是否打开：" + isOpen);
        if (!isOpen) {
            if (retval == -1) {
                viewModel.log("retval为-1");
                viewModel.onOpenFail();
                return;
            } else if (retval == 0) {
                viewModel.log("retval为0");
                if (!XTYApplication.driver.UartInit()) {//对串口设备进行初始化操作
                    viewModel.log("初始化串口失败");
                    viewModel.onOpenFail();
                    return;
                }
                viewModel.log("初始化串口成功");
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
        isOpen = true;
        //  bootDevice();
       /* ThreadHandler.postUiThread(new Runnable() {
            @Override
            public void run() {
                new readThread().start();//开线程读取数据
                writeInitApiData();//初始化主站请求建立连接帧
            }
        },1000);*/
        viewModel.log("开线程读取数据");
        writeInitApiData();//初始化主站请求建立连接帧
        new readThread().start();//开线程读取数据
    }

    /**
     * 激活设备
     */
    public void bootDevice() {
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


    int countByteSizeIsZero ;//计算请求的字节码 为0 的 次数 ，初略估算为0 的超过3次表示数据已读完

    private class readThread extends Thread {
        public void run() {
            viewModel.log("进入子线程");
            byte[] buffer = new byte[256];
            while (true) {
                try{
                    Thread.sleep(new Random().nextInt(1000));
                }catch (Exception e){
                }
                try
                {
                    lock.lock();
                    if (!isOpen) {
                        viewModel.log("循环结束");
                        Log.e("isOpen", "====" + isOpen + "===循环结束");
                        break;
                    }
                    //viewModel.log("循环数据读取");
                    final int length = XTYApplication.driver.ReadData(buffer, 256);
                     Log.w("收到数据length","长度为"+ length);
                    if (length > 0) {

                        if (timer != null) {
                            ThreadHandler.postUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast("收到回应");
                                }
                            });
                            timer.cancel();
                            timer =null;
                        }
                        final String recv = CH34xDriverUtil.toHexString(buffer, length);
                        viewModel.log("具体数据： " + recv);
                        ThreadHandler.postUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (recv.trim().equals(SEND_ORDER)) {
                                    Log.e("onTestPaperNoPullOut", "====onTestPaperNoPullOut");
                                    viewModel.onTestPaperNoPullOut();
                                } else {
                                    //读取成功
                                    //将16进制转陈ASCII
                                    String asciiString = CH34xDriverUtil.parseAscii(recv);
                                    viewModel.onReadSucess(recv, asciiString);
                                }
                            }
                        });

                    }else{
                        countByteSizeIsZero++;
                        Log.w("dyc","countByteSizeIsZero"+countByteSizeIsZero);
                        if (countByteSizeIsZero>3){
                            countByteSizeIsZero = 0;
                            viewModel.onReadFinish();
                            return;
                        }
                    }
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    /**
     * 关闭设备
     */
    public void closeDevice() {
        //设备一直打开着就行
//        XTYApplication.driver.CloseDevice();
        isOpen = false;
    }

    /**
     * 返回的随机帧，再次写入
     */
    public void toWriteStringData(String recv) {
        /**
         * 返回的随机值  68 01 68 02 02 A0 A1 CS 16
         *数据域 A0、A1 为两个字节的随机数，主站接到这个随机数后，依照以下公式计算连接密 码:
         * M0=(int((A0+58H)/03H))+33H 取不计 256 的溢出值 M1=(int((A1+18H)/03H))+33H 取不计 256 的溢出值 CS 为校验码
         * 16H 为结束符
         *
         */
        String[] split = recv.split(" ");

        int A0 = Integer.parseInt(split[5], 16);
        int A1 = Integer.parseInt(split[6], 16);

        int M0 = ((int) ((A0 + 88) / 3)) + 51;
        int M1 = ((int) ((A1 + 24) / 3)) + 51;

        /**
         * 再次请求主站请求数据帧
         68 00 68 03 02 M0 M1 CS 16
         从站接到请求后先校验 M0 与 M1 是否正确，如果正确则发送数据正常应答帧，如果不正确
         不回应，同时该次连接密码失效。需主站再次发出连接请求，从站再次生成随机数给主站。 从站随机数发出后，1 秒内主站无回应，则该随机数失效，需主站再次发出连接请求。
         */
        byte[] to_for_send = CH34xDriverUtil.toByteArray("68 00 68 03 02 " + Integer.toHexString(M0) + " " + Integer.toHexString(M1) + " " + 89 + " " + 16);
        //返回帧16进制
        int retvalCode = XTYApplication.driver.WriteData(to_for_send, to_for_send.length);//写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
        if (retvalCode < 0) {
            viewModel.onReadFail();
        } else {
            Log.e("第二次写入", "=====" + "68 00 68 03 02 " + Integer.toHexString(M0) + " " + Integer.toHexString(M1) + " " + 89 + " " + 16);
            isSecondCode = false;
            isOpen = true;
        }


    }


    Timer timer;
    int count= 1;

    /**
     * 初始化主站请求建立连接帧
     */
    private void writeInitApiData() {
        viewModel.log("初始化主站请求建立");
        //写入初始化，固定返回随机帧
        count = 1;
        final byte[] to_send = CH34xDriverUtil.toByteArray(SEND_ORDER);


        //写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
        int retval = XTYApplication.driver.WriteData(to_send, to_send.length);
        viewModel.log("写入数据成功" + retval);
        Log.e("第一次写入", SEND_ORDER);
      //  ToastUtil.showToast("第一次发起");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    lock.lock();  //锁住
                    count++;
                    if (count >= 6) {
                        timer.cancel();
                        timer =null;
                        viewModel.onReadFail();
                        return;
                    }
                    XTYApplication.driver.WriteData(to_send, to_send.length);
                    ThreadHandler.postUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast("第" + count + "次发起");
                        }
                    });
                }finally {
                    lock.unlock();
                }
            }
        }, 5000, 5000);


        /*if (retval < 0) {
            Log.e("onReadFail", "retval=====" + retval);
            viewModel.log("写入数据失败");
            viewModel.onReadFail();
        } else {
            viewModel.log("写入数据成功"+ retval);
            Log.e("第一次写入", "68 00 68 01 00 89 16");
            isSecondCode = true;
        }*/
    }


    /**
     * 配置串口波特率
     */
    private void initConfig() {
        viewModel.log("配置串口波特率");
        int baudRate = 115200;
        byte dataBit = 8;
        byte stopBit = 1;
        byte parity = 0;
        byte flowControl = 0;
        XTYApplication.driver.SetConfig(baudRate, dataBit, stopBit, parity,
                flowControl);
    }


    //业务需求
    private int level;
    private BLEBloodModel bleBloodModel;
    private String timeLien = "";
    private TestInfo testInfo;

    @Override
    public void initData() {
        timeLien = System.currentTimeMillis() + "";

    }

    /**
     * 获取血糖范围方案
     *
     * @param activity
     * @param memberId
     */
    public void getRange(Activity activity, final String memberId) {
        ComveeLoader.getInstance().getMemberRange(memberId)
                .subscribe(new HttpCall<BloodRangeBean>(activity) {
                    @Override
                    public void onNext(BloodRangeBean rangeBean) {
                        RangeHelper.setBloodRangeBean(memberId, rangeBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    /**
     * 保存血糖值
     *
     * @param selectedTime
     */
    public void completeBlood(String selectedTime) {
        viewModel.showProgress();
        initDataTestInfo(selectedTime); //更新TestInfoDao库
        String dateStr = bleBloodModel.getReadTime();
        Log.e("上传时间====", "" + dateStr);
        if (Utils.isNetwork(viewModel.getActivity())) {
            getSysTimeToPost(dateStr);
        } else {
            RecordHelper.recordBloodSugar(testInfo, dateStr, level, true); //更新病床库，跟历史记录库
        }
        RxBus.getDefault().post(new BloodUpdateRxModel());
        //回传血糖数据给主界面
        ActivityCommunicationUtil.getSingle().getCallback(MainActivity.class.getName()).onCall(testInfo);
        RxBus.getDefault().post(new IndexUiRxModel("getAllData"));
        ThreadHandler.postUiThread(new Runnable() {
            @Override
            public void run() {
                viewModel.onFinishActivity();
            }
        }, 500);
    }

    public void initDataTestInfo(String selectedTime) {
        String nowParamCode = TextUtils.isEmpty(selectedTime) ? TestResultDataUtil.toTimeSlot(new Date()) : selectedTime;
        String postParamCode = TestResultDataUtil.getParamCode(nowParamCode);
        testInfo = new TestInfo(UserHelper.getUserId(),
                bleBloodModel.getValue() + "",
                postParamCode,
                bleBloodModel.getReadTime(),
                bleBloodModel.getMemberId(),
                1, "", timeLien);
        TestInfoDao testInfoDao = XTYApplication.getInstance().getDaoSession().getTestInfoDao();
        List<TestInfo> localList = testInfoDao.queryBuilder().where(TestInfoDao.Properties.MemberId.eq(testInfo.getMemberId()),
                TestInfoDao.Properties.RecordTime.eq(testInfo.getRecordTime()),
                TestInfoDao.Properties.Value.eq(testInfo.getValue())).build().list();
        if (!localList.isEmpty()) {
            testInfoDao.deleteInTx(localList);
        }
        testInfoDao.insertInTx(testInfo);
    }

    private void getSysTimeToPost(String dateStr) {
        RecordHelper.recordBloodSugar(testInfo, dateStr, level, false);//更新病床库，跟历史记录库
        RxBus.getDefault().post(new BloodUpdateRxModel()); //通知BloodUpdateService
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setBleBloodModel(BLEBloodModel bleBloodModel) {
        this.bleBloodModel = bleBloodModel;
    }
}
