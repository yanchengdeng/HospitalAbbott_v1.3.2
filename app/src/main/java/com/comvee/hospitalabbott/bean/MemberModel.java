package com.comvee.hospitalabbott.bean;


import com.comvee.hospitalabbott.tool.SortUtil;
import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


/**
 * Created by F011512088 on 2017/4/14.
 */
@Entity
public class MemberModel {
    @Id
    private Long id;
    private String bedId;
    private String bedNo;
    private float bedNoNew;
    private String concernStatus;
    private String departmentId;
    private String departmentName;
    private String memberId;
    private String memberName;
    private String btTxt;//体质类型
    private String diabetesTxt;//糖尿病类型

    private String afterBreakfast;
    private String afterDinner;
    private String afterLunch;
    private String beforeBreakfast;
    private String beforeDinner;
    private String beforeLunch;
    private String beforeSleep;
    private String beforedawn;
    private String threeclock;
    private String randomtime;


    /**
     * 监测类型    1 长期  2临时 0 没有
     */
    private int planType;

    //监测方案
    private String smbgScheme;
    private String sex; //性别 1男 2女
    private String patPatientId; //患者登记号

    public MemberModel(Rows rows) {
        this.bedId = rows.getBi();
        this.bedNo = rows.getBn();
        bedNoNew = SortUtil.getBedNumNew(rows.getBn());
        this.concernStatus = rows.getCs();
        this.departmentId = rows.getDi();
        this.departmentName = rows.getDn();
        this.btTxt = rows.getBtTxt();//体质类型
        this.diabetesTxt = rows.getDiabetesTxt();//糖尿病类型
        this.memberId = rows.getMi();
        this.memberName = rows.getMn();
        ParamCodeBean abBean = rows.getNewSugarMap().getAb();
//        if (abBean != null)
        this.afterBreakfast = new Gson().toJson(abBean);
        ParamCodeBean adBean = rows.getNewSugarMap().getAd();
//        if (adBean != null)
        this.afterDinner = new Gson().toJson(adBean);
        ParamCodeBean alBean = rows.getNewSugarMap().getAl();
//        if (alBean != null)
        this.afterLunch = new Gson().toJson(alBean);
        ParamCodeBean bbBean = rows.getNewSugarMap().getBb();
//        if (bbBean != null)
        this.beforeBreakfast = new Gson().toJson(bbBean);
        ParamCodeBean bdBean = rows.getNewSugarMap().getBd();
//        if (bdBean != null)
        this.beforeDinner = new Gson().toJson(bdBean);
        ParamCodeBean blBean = rows.getNewSugarMap().getBl();
//        if (blBean != null)
        this.beforeLunch = new Gson().toJson(blBean);
        ParamCodeBean bsBean = rows.getNewSugarMap().getBs();
//        if (bsBean != null)
        this.beforeSleep = new Gson().toJson(bsBean);
        BeforedawnBean beforedawnBean = rows.getNewSugarMap().getBeforedawn();
//        if (beforedawnBean != null)
        this.beforedawn = new Gson().toJson(beforedawnBean);

        ThreeclockBean threeclockBean = rows.getNewSugarMap().getThreeclock();
        this.threeclock = new Gson().toJson(threeclockBean);

        RandomtimeBean randomtimeBean = rows.getNewSugarMap().getRandomtime();
//        if (randomtimeBean != null)
        this.randomtime = new Gson().toJson(randomtimeBean);
        this.smbgScheme = rows.getSs();
        this.sex = rows.getSex();
        this.patPatientId = rows.getPatPatientId();
        this.planType = rows.getPlanType();
    }


    @Generated(hash = 1407978720)
    public MemberModel(Long id, String bedId, String bedNo, float bedNoNew, String concernStatus, String departmentId,
            String departmentName, String memberId, String memberName, String btTxt, String diabetesTxt,
            String afterBreakfast, String afterDinner, String afterLunch, String beforeBreakfast, String beforeDinner,
            String beforeLunch, String beforeSleep, String beforedawn, String threeclock, String randomtime, int planType,
            String smbgScheme, String sex, String patPatientId) {
        this.id = id;
        this.bedId = bedId;
        this.bedNo = bedNo;
        this.bedNoNew = bedNoNew;
        this.concernStatus = concernStatus;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.memberId = memberId;
        this.memberName = memberName;
        this.btTxt = btTxt;
        this.diabetesTxt = diabetesTxt;
        this.afterBreakfast = afterBreakfast;
        this.afterDinner = afterDinner;
        this.afterLunch = afterLunch;
        this.beforeBreakfast = beforeBreakfast;
        this.beforeDinner = beforeDinner;
        this.beforeLunch = beforeLunch;
        this.beforeSleep = beforeSleep;
        this.beforedawn = beforedawn;
        this.threeclock = threeclock;
        this.randomtime = randomtime;
        this.planType = planType;
        this.smbgScheme = smbgScheme;
        this.sex = sex;
        this.patPatientId = patPatientId;
    }


    @Generated(hash = 1847833359)
    public MemberModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public float getBedNoNew() {
        return bedNoNew;
    }

    public void setBedNoNew(float bedNoNew) {
        this.bedNoNew = bedNoNew;
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

    public String getAfterBreakfast() {
        return afterBreakfast;
    }

    public void setAfterBreakfast(String afterBreakfast) {
        this.afterBreakfast = afterBreakfast;
    }

    public String getAfterDinner() {
        return afterDinner;
    }

    public void setAfterDinner(String afterDinner) {
        this.afterDinner = afterDinner;
    }

    public String getAfterLunch() {
        return afterLunch;
    }

    public void setAfterLunch(String afterLunch) {
        this.afterLunch = afterLunch;
    }

    public String getBeforeBreakfast() {
        return beforeBreakfast;
    }

    public void setBeforeBreakfast(String beforeBreakfast) {
        this.beforeBreakfast = beforeBreakfast;
    }

    public String getBeforeDinner() {
        return beforeDinner;
    }

    public void setBeforeDinner(String beforeDinner) {
        this.beforeDinner = beforeDinner;
    }

    public String getBeforeLunch() {
        return beforeLunch;
    }

    public void setBeforeLunch(String beforeLunch) {
        this.beforeLunch = beforeLunch;
    }

    public String getBeforeSleep() {
        return beforeSleep;
    }

    public void setBeforeSleep(String beforeSleep) {
        this.beforeSleep = beforeSleep;
    }

    public String getBeforedawn() {
        return beforedawn;
    }

    public void setBeforedawn(String beforedawn) {
        this.beforedawn = beforedawn;
    }

    public String getThreeclock() {
        return threeclock;
    }

    public void setThreeclock(String threeclock) {
        this.threeclock = threeclock;
    }

    public String getRandomtime() {
        return randomtime;
    }

    public void setRandomtime(String randomtime) {
        this.randomtime = randomtime;
    }

    public int getPlanType() {
        return planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
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

    public String getPatPatientId() {
        return patPatientId;
    }

    public void setPatPatientId(String patPatientId) {
        this.patPatientId = patPatientId;
    }
}
