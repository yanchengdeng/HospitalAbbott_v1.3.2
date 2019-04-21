package com.comvee.hospitalabbott.network.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.tool.ActivityMrg;
import com.comvee.hospitalabbott.tool.AppUtils;
import com.comvee.hospitalabbott.tool.ThreadHandler;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.tool.WiseFy;
import com.comvee.hospitalabbott.ui.login.LoginActivity;
import com.comvee.hospitalabbott.widget.LoadingDialog;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.concurrent.CountDownLatch;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by F011512088 on 2017/12/28.
 */

public abstract class HttpCall<T> implements Observer<T> {

    private Context context;
    private boolean isShow;
    private String dialogMsg;
    private DialogFragment dialog;
    private Intent intent;
    private Disposable disposable;
    private WiseFy mWiseFy;
    public static boolean resetWifi = false;

    public HttpCall(Context context) {
        this.context = context;
        isShow = false;
        dialogMsg = "努力加载中...";
        mWiseFy = new WiseFy.withContext(XTYApplication.getInstance()).logging(false).getSmarts();
        initDialog();
    }

    public HttpCall(Context context, Intent intent) {
        this(context);
        this.intent = intent;
    }

    private void initDialog() {
        if (dialog == null)
            dialog = new DialogFragment();
    }


    /**
     * 随时调用取消连接
     *
     * @param d
     */
    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onError(Throwable e) {
        /*String errorStr = e.getMessage();
        if (!TextUtils.isEmpty(errorStr) && errorStr.contains("Failed to connect")
                && ActivityMrg.getInstance().getCurrentActivity() != null
                && Utils.isNetwork(ActivityMrg.getInstance().getCurrentActivity())) {
            if (!resetWifi) {
                resetWifi = true;
                mWiseFy.disableWifi();
                ThreadHandler.postUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWiseFy.enableWifi();
                    }
                }, 2000);
                *//*ThreadHandler.postUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!Utils.isNetwork(ActivityMrg.getInstance().getCurrentActivity())) {
                            ToastUtil.showToast(ActivityMrg.getInstance().getCurrentActivity(), "请检查网络连接...");
                        }
                    }
                },5000);*//*
            }

        }*/

        if (e instanceof HttpFaultException) {
            HttpFaultException httpException = (HttpFaultException) e;
            if (httpException.getCode() == HttpFaultException.TOKEN_EXPIRED) {


            }
            showToast(httpException.getMessage());
        } else if (e instanceof SocketTimeoutException) { //指的是服务器响应超时 , 如果是响应失败，就说明用户提交是成功了的，应该防止用户提交。
            showToast("服务器响应超时...");
            return;
        } else if (e instanceof ConnectException) { //指的是服务器请求超时
            showToast("服务器请求超时...");
            return;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {//解析异常
            Log.e("onError : ", "--解析异常--");
            showToast("解析异常...");
            return;
        } else {
            if (!AppUtils.isNetworkConnected(XTYApplication.getInstance())) {
                showToast("网络断开，请查看网络...");
            } else {
                Log.e("onError : ", "请求错误:" + e.getMessage());
            }
        }
        e.printStackTrace();
    }

    /**
     * 已完成发送基于推送的通知。
     */
    @Override
    public void onComplete() {

    }

    public HttpCall setDialogMsg(String dialogMsg) {
        this.dialogMsg = dialogMsg;
        return this;
    }

    public HttpCall setShow(boolean show) {
        isShow = show;
        return this;
    }

    public HttpCall setDialog(DialogFragment dialog) {
        this.dialog = dialog;
        return this;
    }

    public Intent getIntent() {
        return intent;
    }

    private void showToast(String toastStr) {
        if (context != null && toastStr != null)
            ToastUtil.showToast(context, toastStr);
    }

    public Disposable getDisposable() {
        return disposable;
    }
}
