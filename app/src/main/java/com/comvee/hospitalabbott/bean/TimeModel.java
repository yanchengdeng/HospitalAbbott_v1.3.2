package com.comvee.hospitalabbott.bean;

import java.io.Serializable;

/**
 * Created by F011512088 on 2017/5/3.
 */

public class TimeModel implements Serializable {

    private String timeParamCode;
    private int position;

    public TimeModel(String timeParamCode, int position) {
        this.timeParamCode = timeParamCode;
        this.position = position;
    }

    public String getTimeParamCode() {
        return timeParamCode;
    }

    public void setTimeParamCode(String timeParamCode) {
        this.timeParamCode = timeParamCode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
