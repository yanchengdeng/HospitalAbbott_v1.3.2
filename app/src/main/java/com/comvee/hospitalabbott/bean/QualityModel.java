package com.comvee.hospitalabbott.bean;

import java.io.Serializable;

/**
 * Created by F011512088 on 2018/2/27.
 */

public class QualityModel implements Serializable {

    private String date; //时间
    private float high;
    private float low;
    private String qcPagerNo; // 质控试纸编号
    private String qcLiquidNo; //质控液编号
    private String qcLiquidLevel; // 质控液等级
    private String machineId; //设备编号

    private String readTime; //记录时间
    private float value;//血糖值

    public QualityModel(String date, float high, float low, String qcPagerNo, String qcLiquidNo, String qcLiquidLevel, String machineId) {
        this.date = date;
        this.high = high;
        this.low = low;
        this.qcPagerNo = qcPagerNo;
        this.qcLiquidNo = qcLiquidNo;
        this.qcLiquidLevel = qcLiquidLevel;
        this.machineId = machineId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public String getQcPagerNo() {
        return qcPagerNo;
    }

    public void setQcPagerNo(String qcPagerNo) {
        this.qcPagerNo = qcPagerNo;
    }

    public String getQcLiquidNo() {
        return qcLiquidNo;
    }

    public void setQcLiquidNo(String qcLiquidNo) {
        this.qcLiquidNo = qcLiquidNo;
    }

    public String getQcLiquidLevel() {
        return qcLiquidLevel;
    }

    public void setQcLiquidLevel(String qcLiquidLevel) {
        this.qcLiquidLevel = qcLiquidLevel;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
