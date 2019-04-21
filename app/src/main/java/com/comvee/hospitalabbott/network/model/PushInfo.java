package com.comvee.hospitalabbott.network.model;

/**
 * Created by comv098 on 2017/1/20.
 */

public class PushInfo {
    private String title;
    private String msg;
    private int id;

    public PushInfo(String title, String msg, int id) {
        this.title = title;
        this.msg = msg;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
