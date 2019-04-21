package com.comvee.hospitalabbott.network;

import android.util.Log;

import com.comvee.hospitalabbott.helper.AbbottHelper;
import com.comvee.hospitalabbott.network.config.BasicParamsInterceptor;
import com.comvee.hospitalabbott.network.config.LoggingInterceptor;
import com.comvee.hospitalabbott.network.config.LoggingTotalInterceptor;
import com.comvee.hospitalabbott.tool.AppUtils;
import com.comvee.hospitalabbott.helper.UserHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求封装类
 * Created by F011512088 on 2017/12/27.
 */

public class NetWorkManger {
    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private Retrofit mRetrofit;

    public NetWorkManger() {
        Log.d("NetWorkManger", "--init--");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间
        builder.retryOnConnectionFailure(true);
        builder.followRedirects(true);
        // 添加公共参数拦截器
        BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor.Builder()
                .addParam("verNo", AppUtils.APP_VERSION_CODE+"")
                .addParam("machineNo", UserHelper.getMachineNo())
                .addParam("machineSn", UserHelper.getMachineSn())
                .addParam("sessionToken", UserHelper.getSessionToken())
                .addParam("appType", "3")//app类型： 1 旧版血糖仪       2 新版血糖仪app   3otg血糖仪
                .build();

        builder.addInterceptor(basicParamsInterceptor);
        builder.addInterceptor(new LoggingTotalInterceptor());

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UserHelper.getBaseUrl())
                .build();
    }

    /**
     * 获取RetrofitServiceManager
     *
     * @return
     */
    public static NetWorkManger getInstance() {
        if (netWorkManger == null) {
            netWorkManger = new NetWorkManger();
        }
        return netWorkManger;
    }

    public static void reset(){
        netWorkManger = new NetWorkManger();
    }

    /**
     * 本地地址信息、用户信息变换更新网络请求
     */
    public static void refreshNetWorkManger() {
        netWorkManger = new NetWorkManger();
    }

    private static NetWorkManger netWorkManger;

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

}
