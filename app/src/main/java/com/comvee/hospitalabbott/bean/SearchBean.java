package com.comvee.hospitalabbott.bean;

import java.io.Serializable;

/**
 * Created by comv098 on 2017/2/8.
 */

public class SearchBean implements Serializable {

    /**
     * bedId : 41
     * bedNo : 1
     * departmentId : 3
     * departmentName : 内二科
     * memberId : 170414180300001
     * memberName : 陈丽萍
     * roomId : 8
     * roomNo : 1
     * sex :
     */

    private String bedId;
    private String bedNo;
    private String departmentId;
    private String departmentName;
    private String memberId;
    private String memberName;
    private String roomId;
    private String roomNo;
    private String sex;
    private String btTxt;//体质类型
    private String diabetesTxt;//糖尿病类型
    private String tempBedNo;//用于排序

    public void setTempUserName(String tempUserName) {
        this.tempBedNo = tempUserName;
    }

    public SearchBean() {
    }

    public SearchBean(MemberModel model) {
        this.bedId = model.getBedId();
        this.bedNo = model.getBedNo();
        this.departmentId = model.getDepartmentId();
        this.departmentName = model.getDepartmentName();
        this.memberId = model.getMemberId();
        this.memberName = model.getMemberName();
        this.sex = model.getSex();
        this.btTxt = model.getBtTxt();
        this.diabetesTxt = model.getDiabetesTxt();
    }

    public SearchBean(HospitalBed hospitalBed) {
        this.bedId = hospitalBed.getBedId();
        this.bedNo = hospitalBed.getBedNo();
        this.departmentId = hospitalBed.getDepartmentId();
        this.departmentName = hospitalBed.getDepartmentName();
        this.memberId = hospitalBed.getMemberId();
        this.memberName = hospitalBed.getMemberName();
        this.sex = hospitalBed.getSex();
        this.btTxt = hospitalBed.getBtTxt();
        this.diabetesTxt = hospitalBed.getDiabetesTxt();
    }

    public String getBtTxt() {
        return btTxt;
    }

    public void setBtTxt(String btTxt) {
        this.btTxt = btTxt;
    }

    public String getDiabetesTxt() {
        return diabetesTxt;
    }

    public void setDiabetesTxt(String diabetesTxt) {
        this.diabetesTxt = diabetesTxt;
    }

    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
