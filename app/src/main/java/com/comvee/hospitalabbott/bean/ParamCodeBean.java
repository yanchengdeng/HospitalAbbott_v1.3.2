package com.comvee.hospitalabbott.bean;


import java.io.Serializable;

/**
 * 时间段的血糖记录信息
 * Created by F011512088 on 2017/4/13.
 */

public class ParamCodeBean implements Serializable {


    /**
     * 久
     * level :
     * memberId :
     * paramCode :
     * paramLogId :
     * recordOrigin :
     * recordTime :
     * remark :
     * status :
     * value :
     * 新
     * level
     * mi
     * pc
     * pli
     * ro
     * rt
     * remark
     * status
     * value
     */

//    private String level="";
//    private String memberId="";
//    private String paramCode="";
//    private String paramLogId="";
//    private String recordOrigin="";
//    private String recordTime="";
//    private String remark="";
//    private String status="";
//    private String value="";
    private String level = "";
    private String mi = "";
    private String pc = "";
    private String pli = "";
    private String ro = "";
    private String rt = "";
    private String remark = "";
    private String status = "";
    private String value = "";
    private boolean isNetWork = false;

    public ParamCodeBean(TestInfo info) {
        this.mi = info.getMemberId();
        this.pc = info.getParamCode();
        this.rt = info.getRecordTime();
        this.remark = info.getRemark();
        this.value = info.getValue();
        this.status = info.getStateInte() + "";
    }

    public ParamCodeBean() {
        level = "";
        mi = "";
        pc = "";
        pli = "";
        ro = "";
        rt = "";
        remark = "";
        status = "";
        value = "";
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMemberId() {
        return mi;
    }

    public void setMemberId(String memberId) {
        this.mi = memberId;
    }

    public String getParamCode() {
        return pc;
    }

    public void setParamCode(String paramCode) {
        this.pc = paramCode;
    }

    public String getParamLogId() {
        return pli;
    }

    public void setParamLogId(String paramLogId) {
        this.pli = paramLogId;
    }

    public String getRecordOrigin() {
        return ro;
    }

    public void setRecordOrigin(String recordOrigin) {
        this.ro = recordOrigin;
    }

    public String getRecordTime() {
        return rt;
    }

    public void setRecordTime(String recordTime) {
        this.rt = recordTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isNetWork() {
        return isNetWork;
    }

    public void setNetWork(boolean netWork) {
        isNetWork = netWork;
    }

    public String getMi() {
        return mi;
    }

    public void setMi(String mi) {
        this.mi = mi;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getPli() {
        return pli;
    }

    public void setPli(String pli) {
        this.pli = pli;
    }

    public String getRo() {
        return ro;
    }

    public void setRo(String ro) {
        this.ro = ro;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    @Override
    public String toString() {
        return "ParamCodeBean{" +
                "level='" + level + '\'' +
                ", memberId='" + mi + '\'' +
                ", paramCode='" + pc + '\'' +
                ", paramLogId='" + pli + '\'' +
                ", recordOrigin='" + ro + '\'' +
                ", recordTime='" + rt + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", value='" + value + '\'' +
                ", isNetWork=" + isNetWork +
                '}';
    }
}
