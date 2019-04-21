package com.comvee.hospitalabbott.bean;

import java.io.Serializable;

public class CountListBean implements Serializable {
    /**
     * num : 2
     * paramCode : a
     */

    private String num;
    private String paramCode;
    private int position;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}