package com.comvee.hospitalabbott.bean;

import java.io.Serializable;

public class DiffBloodValue implements Serializable {

    public String dateString ;

    public DiffBloodValue(String dateString, float valueData) {
        this.dateString = dateString;
        this.valueData = valueData;
    }

    public float valueData;

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public float getValueData() {
        return valueData;
    }

    public void setValueData(float valueData) {
        this.valueData = valueData;
    }
}
