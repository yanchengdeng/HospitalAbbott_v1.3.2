package com.comvee.hospitalabbott.bean;

/**
 * Created by F011512088 on 2018/1/12.
 */

public class Rows {


    /**
     * bi : 1
     * bn : 01
     * cs : 1
     * departId : 1
     * di : 1
     * dn : 内分泌科
     * mi : 1
     * mn : 沈蔚
     * newSugarMap : {"":{"level":"5","mi":"1","pc":"0am","pli":"180117095800024","remark":"11","ro":null,"rt":"2018-01-17 02:00:0","status":"1","value":"11"},"ab":{"level":"3","mi":"1","pc":"afterBreakfast","pli":"180117101200120","remark":"","ro":null,"rt":"2018-01-17 03:03:0","status":"1","value":"7"},"ad":{"level":"3","mi":"1","pc":"afterDinner","pli":"180117101300128","remark":"","ro":null,"rt":"2018-01-17 10:15:0","status":"1","value":"6"},"al":{"level":"1","mi":"1","pc":"afterLunch","pli":"180117144400026","remark":"","ro":null,"rt":"2018-01-17 14:41:51","status":"2","value":"0"},"bb":{"level":"3","mi":"1","pc":"beforeBreakfast","pli":"180117095900015","remark":"","ro":null,"rt":"2018-01-17 12:01:0","status":"1","value":"5.6"},"bd":{"level":"5","mi":"1","pc":"beforeDinner","pli":"180117101200126","remark":"","ro":null,"rt":"2018-01-17 20:14:0","status":"1","value":"34"},"beforedawn":{"level":"1","mi":"1","pc":"beforedawn","pli":"180117140100005","remark":"3","ro":"web","rt":"2018-01-17 02:03","status":"1","value":"3"},"bl":{"level":"1","mi":"1","pc":"beforeLunch","pli":"180117112500041","remark":"","ro":null,"rt":"2018-01-17 11:24:56","status":"2","value":"0"},"bs":{"level":"3","mi":"1","pc":"beforeSleep","pli":"180117101300130","remark":"","ro":null,"rt":"2018-01-17 10:14:0","status":"1","value":"4"},"randomtime":{"level":"5","mi":"1","pc":"randomtime","pli":"180117101300134","remark":"","ro":null,"rt":"2018-01-17 20:16:0","status":"1","value":"12"}}
     * sex : 1
     * ss :
     */

    private String bi;
    private String bn;
    private String cs;
    private String departId;
    private String di;
    private String dn;
    private String mi;
    private String mn;
    private NewSugarMapBean newSugarMap;
    private String sex;
    private String ss;
    /**
     * 监测类型    1 长期  2临时 0 没有
     */
    private int planType;
    private String patPatientId;

    public String getBi() {
        return bi;
    }

    public void setBi(String bi) {
        this.bi = bi;
    }

    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getMi() {
        return mi;
    }

    public void setMi(String mi) {
        this.mi = mi;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public NewSugarMapBean getNewSugarMap() {
        return newSugarMap;
    }

    public void setNewSugarMap(NewSugarMapBean newSugarMap) {
        this.newSugarMap = newSugarMap;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
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
}
