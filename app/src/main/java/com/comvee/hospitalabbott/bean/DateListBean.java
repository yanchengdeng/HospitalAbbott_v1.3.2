package com.comvee.hospitalabbott.bean;

import java.io.Serializable;

public class DateListBean implements Serializable {
    public String dateString;
    public String dateWeek;
    public String dateDay;
    public boolean isSelected;

    public DateListBean(String dateString, String dateWeek,String dateDay) {
        this.dateString = dateString;
        this.dateWeek = dateWeek;
        this.dateDay = dateDay;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getDateWeek() {
        return dateWeek;
    }

    public void setDateWeek(String dateWeek) {
        this.dateWeek = dateWeek;
    }


}
