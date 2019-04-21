package com.comvee.hospitalabbott.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by comv098 on 2017/2/19.
 */
@Entity
public class TestInfo {
    @Id
    private Long id; // 来设置ID
    //用户Id
    private String userId;
    // 血糖值
    private String value;
    //血糖时间段code
    private String paramCode;
    //记录时间
    private String recordTime;
    //患者ID
    private String memberId;
    /*state 2 拒绝 、1 血糖记录*/
    private int stateInte;
    /*备注*/
    private String remark;
    /*时间戳用于记录血糖结果页是否是同一条数据*/
    private String timeLine = "";

    private boolean isManual;

    public TestInfo(String userId, String value, String paramCode, String recordTime, String memberId, int stateInte, String remark, String timeLine) {
        this.userId = userId;
        this.value = value;
        this.paramCode = paramCode;
        this.recordTime = recordTime;
        this.memberId = memberId;
        this.stateInte = stateInte;
        this.remark = remark;
        this.timeLine = timeLine;
        this.isManual = true;
    }

    public TestInfo(String userId, String value, String paramCode, String recordTime, String memberId, int stateInte, String remark,boolean isManual) {
        this.userId = userId;
        this.value = value;
        this.paramCode = paramCode;
        this.recordTime = recordTime;
        this.memberId = memberId;
        this.stateInte = stateInte;
        this.remark = remark;
        this.isManual = isManual;
    }

    @Generated(hash = 1964291635)
    public TestInfo(Long id, String userId, String value, String paramCode, String recordTime, String memberId, int stateInte, String remark,
            String timeLine, boolean isManual) {
        this.id = id;
        this.userId = userId;
        this.value = value;
        this.paramCode = paramCode;
        this.recordTime = recordTime;
        this.memberId = memberId;
        this.stateInte = stateInte;
        this.remark = remark;
        this.timeLine = timeLine;
        this.isManual = isManual;
    }

    @Generated(hash = 1499832600)
    public TestInfo() {
    }

    public int getStateInte() {
        return stateInte;
    }

    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(String timeLine) {
        this.timeLine = timeLine;
    }

    public boolean isManual() {
        return isManual;
    }

    public void setManual(boolean manual) {
        isManual = manual;
    }

    public boolean getIsManual() {
        return this.isManual;
    }

    public void setIsManual(boolean isManual) {
        this.isManual = isManual;
    }

    @Override
    public String toString() {
        return "TestInfo{" +
                ", userId='" + userId + '\'' +
                ", value='" + value + '\'' +
                ", paramCode='" + paramCode + '\'' +
                ", recordTime='" + recordTime + '\'' +
                ", memberId='" + memberId + '\'' +
                ", stateInte=" + stateInte +
                ", remark='" + remark + '\'' +
                ", timeLine='" + timeLine + '\'' +
                ", isManual=" + isManual +
                '}';
    }
}
