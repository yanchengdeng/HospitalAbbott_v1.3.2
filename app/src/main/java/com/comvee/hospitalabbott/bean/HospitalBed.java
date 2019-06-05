package com.comvee.hospitalabbott.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.comvee.hospitalabbott.adapter.GroupBedAdapter;

import java.io.Serializable;

/**
 * Created by F011512088 on 2018/1/3.
 */

public class HospitalBed implements MultiItemEntity, Serializable {

    private String bedId;
    private String bedNo;
    private String concernStatus;
    private String departmentId;
    private String departmentName;
    private String memberId;
    private String memberName;

    private String diabetesTxt;// 1型  2型  病类型
    private String btTxt;//  体弱  妊娠  体质类型

    //监测方案
    private String smbgScheme;
    private String sex; //性别 1男 2女

    /**
     * 监测类型    1 长期  2临时 0 没有
     */
    private int planType;
    private String patPatientId;

    private ParamCodeBean paramCodeModel;//用于首页数据返回

    public HospitalBed(MemberModel bedModel) {
        this.bedId = bedModel.getBedId();
        this.bedNo = bedModel.getBedNo();
        this.concernStatus = bedModel.getConcernStatus();
        this.departmentId = bedModel.getDepartmentId();
        this.departmentName = bedModel.getDepartmentName();
        this.memberId = bedModel.getMemberId();
        this.memberName = bedModel.getMemberName();
        this.smbgScheme = bedModel.getSmbgScheme();
        this.sex = bedModel.getSex();
        this.planType = bedModel.getPlanType();
        this.patPatientId = bedModel.getPatPatientId();
        this.btTxt = bedModel.getBtTxt();
        this.diabetesTxt = bedModel.getDiabetesTxt();
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

    public String getConcernStatus() {
        return concernStatus;
    }

    public void setConcernStatus(String concernStatus) {
        this.concernStatus = concernStatus;
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

    public String getSmbgScheme() {
        return smbgScheme;
    }

    public void setSmbgScheme(String smbgScheme) {
        this.smbgScheme = smbgScheme;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public ParamCodeBean getParamCodeModel() {
        return paramCodeModel;
    }

    public void setParamCodeModel(ParamCodeBean paramCodeModel) {
        this.paramCodeModel = paramCodeModel;
    }

    public int getPlanType() {
        return planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }

    public String getPatPatientId() {
        return patPatientId;
    }

    public void setPatPatientId(String patPatientId) {
        this.patPatientId = patPatientId;
    }

    /**
     * 分组数据
     * @return
     */
    @Override
    public int getItemType() {
        return GroupBedAdapter.TYPE_BED;
    }
}
