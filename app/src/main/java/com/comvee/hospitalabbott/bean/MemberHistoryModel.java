package com.comvee.hospitalabbott.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by F011512088 on 2018/1/15.
 */

public class MemberHistoryModel {

    private String memberId;
    private String date;

    private List<ParamLogListBean> beforeDawnList = new ArrayList<>();
    private List<ParamLogListBean> threeclockList = new ArrayList<>();
    private List<ParamLogListBean> beforeBreakfast = new ArrayList<>();
    private List<ParamLogListBean> afterBreakfast = new ArrayList<>();
    private List<ParamLogListBean> beforeLunch = new ArrayList<>();
    private List<ParamLogListBean> afterLunch = new ArrayList<>();
    private List<ParamLogListBean> beforeDinner = new ArrayList<>();
    private List<ParamLogListBean> afterDinner = new ArrayList<>();
    private List<ParamLogListBean> beforeSleep = new ArrayList<>();
    private List<ParamLogListBean> randomtime = new ArrayList<>();

    public MemberHistoryModel(String date) {
        this.date = date;
    }

    public MemberHistoryModel(String memberId, String date) {
        this.memberId = memberId;
        this.date = date;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ParamLogListBean> getBeforeDawnList() {
        return beforeDawnList;
    }

    public void setBeforeDawnList(List<ParamLogListBean> beforeDawnList) {
        this.beforeDawnList = beforeDawnList;
    }

    public List<ParamLogListBean> getThreeclockList() {
        return threeclockList;
    }

    public void setThreeclockList(List<ParamLogListBean> threeclockList) {
        this.threeclockList = threeclockList;
    }

    public List<ParamLogListBean> getBeforeBreakfast() {
        return beforeBreakfast;
    }

    public void setBeforeBreakfast(List<ParamLogListBean> beforeBreakfast) {
        this.beforeBreakfast = beforeBreakfast;
    }

    public List<ParamLogListBean> getAfterBreakfast() {
        return afterBreakfast;
    }

    public void setAfterBreakfast(List<ParamLogListBean> afterBreakfast) {
        this.afterBreakfast = afterBreakfast;
    }

    public List<ParamLogListBean> getBeforeLunch() {
        return beforeLunch;
    }

    public void setBeforeLunch(List<ParamLogListBean> beforeLunch) {
        this.beforeLunch = beforeLunch;
    }

    public List<ParamLogListBean> getAfterLunch() {
        return afterLunch;
    }

    public void setAfterLunch(List<ParamLogListBean> afterLunch) {
        this.afterLunch = afterLunch;
    }

    public List<ParamLogListBean> getBeforeDinner() {
        return beforeDinner;
    }

    public void setBeforeDinner(List<ParamLogListBean> beforeDinner) {
        this.beforeDinner = beforeDinner;
    }

    public List<ParamLogListBean> getAfterDinner() {
        return afterDinner;
    }

    public void setAfterDinner(List<ParamLogListBean> afterDinner) {
        this.afterDinner = afterDinner;
    }

    public List<ParamLogListBean> getBeforeSleep() {
        return beforeSleep;
    }

    public void setBeforeSleep(List<ParamLogListBean> beforeSleep) {
        this.beforeSleep = beforeSleep;
    }

    public List<ParamLogListBean> getRandomtime() {
        return randomtime;
    }

    public void setRandomtime(List<ParamLogListBean> randomtime) {
        this.randomtime = randomtime;
    }


}
