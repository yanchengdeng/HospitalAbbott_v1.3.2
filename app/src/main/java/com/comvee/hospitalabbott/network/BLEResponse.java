package com.comvee.hospitalabbott.network;

/**
 * Created by F011512088 on 2018/1/31.
 */

public class BLEResponse<T> {
    private String msg;
    private T data;
    private String success;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
