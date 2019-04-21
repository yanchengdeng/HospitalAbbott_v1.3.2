package com.comvee.hospitalabbott.network.config;

import android.text.TextUtils;

/**
 * Created by F011512088 on 2017/12/28.
 */

public class HttpFaultException extends RuntimeException {
    private final static String TAG = HttpFaultException.class.getSimpleName();

    public static final int TOKEN_EXPIRED = -1;

    public static final int PWD_AC = 1;

    public static final int PWD_ERR = 2;

    public static final int NO_DATA = 0x4;

    public static final int NO_DEVICE = 1001;

    public static final int NOT_NULL = 999995;
    private int code;

    public HttpFaultException(int code, String msg) {
        this(TextUtils.isEmpty(getApiExceptionMessage(code))? msg : getApiExceptionMessage(code));
        this.code = code;
    }

    public HttpFaultException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 转换错误数据
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code) {
        String message = "";
        switch (code) {
            case TOKEN_EXPIRED:
                message = "登录超时，请重新登录!";
                break;
            case PWD_AC:
                message = "账号错误!";
                break;
            case PWD_ERR:
                message = "密码错误!";
                break;
            case NO_DATA:
                message = "无数据";
                break;
            case NO_DEVICE:
                message = "设备不存在,请确认";
                break;
            case NOT_NULL:
                message = "param不能为空";
                break;
            case 999999:
                message = "系统错误";
                break;
            case 1002:
                message = "设备未绑定病房";
                break;
            case 8888:
                message = "系统错误";
                break;
            default:
                message = "网络不佳";
                break;

        }
//        Log.d(TAG, message);
        return message;
    }

    public int getCode() {
        return code;
    }
}
