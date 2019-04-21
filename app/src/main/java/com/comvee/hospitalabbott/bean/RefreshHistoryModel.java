package com.comvee.hospitalabbott.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 用于患者是否第一次获取血糖历史
 * Created by F011512088 on 2017/5/17.
 */
@Entity
public class RefreshHistoryModel {
    @Id
    private Long id;
    private String memberId;
    private boolean isInit; //false 代表没有加载过  true已经加载过
    private String refreshTime;

    public RefreshHistoryModel(String memberId, boolean isInit, String refreshTime) {
        this.memberId = memberId;
        this.isInit = isInit;
        this.refreshTime = refreshTime;
    }


    @Generated(hash = 308269397)
    public RefreshHistoryModel(Long id, String memberId, boolean isInit, String refreshTime) {
        this.id = id;
        this.memberId = memberId;
        this.isInit = isInit;
        this.refreshTime = refreshTime;
    }


    @Generated(hash = 73867943)
    public RefreshHistoryModel() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public boolean getisInit() {
        return this.isInit;
    }

    public void setisInit(boolean isInit) {
        this.isInit = isInit;
    }

    public String getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }

    public boolean getIsInit() {
        return this.isInit;
    }

    public void setIsInit(boolean isInit) {
        this.isInit = isInit;
    }

}
