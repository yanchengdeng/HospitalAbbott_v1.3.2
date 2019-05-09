package com.comvee.hospitalabbott;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.comvee.greendao.gen.DaoMaster;
import com.comvee.greendao.gen.DaoSession;
import com.comvee.hospitalabbott.receiver.WifiStateReceiver;
import com.comvee.hospitalabbott.tool.LogUtils;
import com.comvee.hospitalabbott.tool.ThreadHandler;
import com.comvee.hospitalabbott.tool.db.MyDaoMaster;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.vise.baseble.ViseBle;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

/**
 * Created by F011512088 on 2018/1/9.
 */

public class XTYApplication extends Application {

    private static final String ACTION_USB_PERMISSION = "com.comvee.hospitalabbott.USB_PERMISSION";
    public static final String EXIT = "EXIT";
    private static XTYApplication application;
    //血糖仪 需要将CH34x的驱动类写在APP类下面，使得帮助类的生命周期与整个应用程序的生命周期是相同的
    public static CH34xUARTDriver driver;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ThreadHandler.init(); //线程
        setDatabase();
        CrashReport.initCrashReport(getApplicationContext(), "0d7b718e71", false);

        XTYApplication.driver = new CH34xUARTDriver(
                (UsbManager) getSystemService(Context.USB_SERVICE), this,
                ACTION_USB_PERMISSION);

      // createListener();
    }

    private void createListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new WifiStateReceiver(), filter);
    }

    /**
     * 捕获全局的任意异常
     */
    private void getAnyException() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();

                LogUtils.e("捕获到异常:" + ex.toString());
                //1.把捕获到的异常存储到本地文件中
                //2.处理把异常文件上传到服务器
                //3.正常退出App(避免出现弹出对话框强制退出)
                System.exit(0);
            }
        });
    }

    private void initBLE() {
        //蓝牙相关配置修改
        ViseBle.config()
                .setScanTimeout(10000)//扫描超时时间，这里设置为20s扫描
                .setConnectTimeout(5 * 1000)//连接超时时间
                .setOperateTimeout(5 * 1000)//设置数据操作超时时间
                .setConnectRetryCount(3)//设置连接失败重试次数
                .setConnectRetryInterval(1000)//设置连接失败重试间隔时间
                .setOperateRetryCount(3)//设置数据操作失败重试次数
                .setOperateRetryInterval(1000)//设置数据操作失败重试间隔时间
                .setMaxConnectCount(1);//设置最大连接设备数量
        ViseLog.plant(new LogcatTree());
        //蓝牙信息初始化，全局唯一，必须在应用初始化时调用
        ViseBle.getInstance().init(this);
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.fragment_background_color, android.R.color.black);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。

//        db = new HMROpenHelper(this, "xty_info.db", null).getWritableDatabase();
//        mDaoMaster = new DaoMaster(db);
//        mDaoSession = mDaoMaster.newSession();



        //TODO 修复 重新创建表  无法识别表明 修改  言诚
        MyDaoMaster helper = new MyDaoMaster(this, "xty_info.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();


    }

    public static XTYApplication getInstance() {
        return application;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
