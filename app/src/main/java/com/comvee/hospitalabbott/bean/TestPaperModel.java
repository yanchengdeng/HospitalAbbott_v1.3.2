package com.comvee.hospitalabbott.bean;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by F011512088 on 2017/11/8.
 */
@Entity
public class TestPaperModel {

    /**
     * sid: 1002
     * insertDt : 2017-11-08 09:24:12.0
     * level1H : 4                      质控液1级高值
     * level1L : 3                      质控液1级低值
     * level2H : 7                      质控液2级高值
     * level2L : 6                      质控液2级低值
     * modifyDt : 2017-11-08 09:24:15.0
     * testNo : NO0002                  试纸批号
     * userId : 171030153700062
     * waterNo : W002                   测试液批号
     */
    @Id
    private Long id;

    private String sid;
    private String insertDt;
    private String level1H;
    private String level1L;
    private String level2H;
    private String level2L;
    private String modifyDt;
    private String testNo;
    private String userId;
    private String waterNo;

    public TestPaperModel(TestPaperBean.RowsBean rowsBean) {
        this.sid = rowsBean.getSid();
        this.insertDt = rowsBean.getInsertDt();
        this.level1H = rowsBean.getLevel1H();
        this.level1L = rowsBean.getLevel1L();
        this.level2H = rowsBean.getLevel2H();
        this.level2L = rowsBean.getLevel2L();
        this.modifyDt = rowsBean.getModifyDt();
        this.testNo = rowsBean.getTestNo();
        this.userId = rowsBean.getUserId();
        this.waterNo = rowsBean.getWaterNo();
        if (TextUtils.isEmpty(sid))
            this.sid = "";
        if (TextUtils.isEmpty(insertDt))
            this.insertDt = "";
        if (TextUtils.isEmpty(level1H))
            this.level1H = "";
        if (TextUtils.isEmpty(level1L))
            this.level1L = "";
        if (TextUtils.isEmpty(level2H))
            this.level2H = "";
        if (TextUtils.isEmpty(level2L))
            this.level2L = "";
        if (TextUtils.isEmpty(modifyDt))
            this.modifyDt = "";
        if (TextUtils.isEmpty(testNo))
            this.testNo = "";
        if (TextUtils.isEmpty(userId))
            this.userId = "";
        if (TextUtils.isEmpty(waterNo))
            this.waterNo = "";
    }

    public TestPaperModel(String sid, String insertDt, String level1H,
                          String level1L, String level2H, String level2L, String modifyDt,
                          String testNo, String userId, String waterNo) {
        this.sid = sid;
        this.insertDt = insertDt;
        this.level1H = level1H;
        this.level1L = level1L;
        this.level2H = level2H;
        this.level2L = level2L;
        this.modifyDt = modifyDt;
        this.testNo = testNo;
        this.userId = userId;
        this.waterNo = waterNo;
    }

    @Generated(hash = 1632786481)
    public TestPaperModel(Long id, String sid, String insertDt, String level1H,
                          String level1L, String level2H, String level2L, String modifyDt, String testNo,
                          String userId, String waterNo) {
        this.id = id;
        this.sid = sid;
        this.insertDt = insertDt;
        this.level1H = level1H;
        this.level1L = level1L;
        this.level2H = level2H;
        this.level2L = level2L;
        this.modifyDt = modifyDt;
        this.testNo = testNo;
        this.userId = userId;
        this.waterNo = waterNo;
    }

    @Generated(hash = 1054793513)
    public TestPaperModel() {
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getLevel1H() {
        return level1H;
    }

    public void setLevel1H(String level1H) {
        this.level1H = level1H;
    }

    public String getLevel1L() {
        return level1L;
    }

    public void setLevel1L(String level1L) {
        this.level1L = level1L;
    }

    public String getLevel2H() {
        return level2H;
    }

    public void setLevel2H(String level2H) {
        this.level2H = level2H;
    }

    public String getLevel2L() {
        return level2L;
    }

    public void setLevel2L(String level2L) {
        this.level2L = level2L;
    }

    public String getModifyDt() {
        return modifyDt;
    }

    public void setModifyDt(String modifyDt) {
        this.modifyDt = modifyDt;
    }

    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWaterNo() {
        return waterNo;
    }

    public void setWaterNo(String waterNo) {
        this.waterNo = waterNo;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
