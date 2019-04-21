package com.comvee.hospitalabbott.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by F011512088 on 2017/4/12.
 */

@Entity
public class QualityResultBean {

    @Id
    private Long id;

    private String time; //时间
    private float high;
    private float low;
    private float bloodValues; //血糖值
    private String qcPagerNo; // 质控试纸编号
    private String qcLiquidNo; //质控液编号
    private String qcLiquidLevel; // 质控液等级
    private String passStatus; //通过状态 0-不通过 1-通过
    private String machineId; //设备编号
    private String explainText; //质控异常备注

    public QualityResultBean(String data, float high, float low, float bloodValues, String qcPagerNo, String qcLiquidNo,
                             String qcLiquidLevel, String passStatus, String machineId, String explainText) {
        this.time = data;
        this.high = high;
        this.low = low;
        this.bloodValues = bloodValues;
        this.qcPagerNo = qcPagerNo;
        this.qcLiquidNo = qcLiquidNo;
        this.qcLiquidLevel = qcLiquidLevel;
        this.passStatus = passStatus;
        this.machineId = machineId;
        this.explainText = explainText;
    }

    @Generated(hash = 1179319350)
    public QualityResultBean(Long id, String time, float high, float low, float bloodValues, String qcPagerNo,
                             String qcLiquidNo, String qcLiquidLevel, String passStatus, String machineId, String explainText) {
        this.id = id;
        this.time = time;
        this.high = high;
        this.low = low;
        this.bloodValues = bloodValues;
        this.qcPagerNo = qcPagerNo;
        this.qcLiquidNo = qcLiquidNo;
        this.qcLiquidLevel = qcLiquidLevel;
        this.passStatus = passStatus;
        this.machineId = machineId;
        this.explainText = explainText;
    }

    @Generated(hash = 181776196)
    public QualityResultBean() {
    }

    public QualityResultBean(QualityModel qualityModel) {
        if (qualityModel != null) {
            this.time = qualityModel.getDate();
            this.high = qualityModel.getHigh();
            this.low = qualityModel.getLow();
            this.bloodValues = qualityModel.getValue();
            this.qcPagerNo = qualityModel.getQcPagerNo();
            this.qcLiquidNo = qualityModel.getQcLiquidNo();
            this.qcLiquidLevel = qualityModel.getQcLiquidLevel();
//        this.passStatus = passStatus;
//        this.machineId = machineId;
//        this.explainText = explainText;
        }
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public float getBloodValues() {
        return bloodValues;
    }

    public void setBloodValues(float bloodValues) {
        this.bloodValues = bloodValues;
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

    public String getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(String passStatus) {
        this.passStatus = passStatus;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExplainText() {
        return explainText;
    }

    public void setExplainText(String explainText) {
        this.explainText = explainText;
    }
}
