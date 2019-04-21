package com.comvee.hospitalabbott.bean;

import java.io.Serializable;

/**
 * Created by comv098 on 2017/3/7.
 */

public class VersionBean implements Serializable {


    /**
     * insertDt : 2017-12-22 16:27:39
     * isValid :
     * modifyDt : 2017-12-22 16:27:39
     * sid : 170330142100219
     * updateDesc :
     * updateStatus : 0
     * updateUrl : http://192.168.20.58:8080/sugar_manage_plat/apk/sign-app-1.3.10.apk
     * versionDesc : 使用方编码测试
     * versionNum : 117
     * versionText : v1.3.11
     * versionType : 1
     */

    private String insertDt;
    private String isValid;
    private String modifyDt;
    private String sid;
    private String updateDesc;
    private String updateStatus;
    private String updateUrl;
    private String versionDesc;
    private String versionNum;
    private String versionText;
    private String versionType;

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getModifyDt() {
        return modifyDt;
    }

    public void setModifyDt(String modifyDt) {
        this.modifyDt = modifyDt;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUpdateDesc() {
        return updateDesc;
    }

    public void setUpdateDesc(String updateDesc) {
        this.updateDesc = updateDesc;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getVersionText() {
        return versionText;
    }

    public void setVersionText(String versionText) {
        this.versionText = versionText;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }
}
