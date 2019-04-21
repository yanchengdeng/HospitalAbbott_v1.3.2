package com.comvee.hospitalabbott.network.rxbus;

/**
 * Created by xingkong on 2018/5/16.
 */

public class IndexUiRxModel {
    public String command;
    public Object data;

    public IndexUiRxModel(String command) {
        this.command = command;
    }

    public IndexUiRxModel(String command, Object data) {
        this.command = command;
        this.data = data;
    }
}
