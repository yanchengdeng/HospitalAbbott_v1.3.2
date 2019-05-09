package com.comvee.hospitalabbott.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
*
* Author: 邓言诚  Create at : 2019/4/22  23:58
* Email: yanchengdeng@gmail.com
* Describle: 血糖记录
*/
@Entity
public class NewBloodItem{

    @Id
    private Long id;
    private int history_id;//id
    private int memberId;//内部id
    private int year;//日期时间
    private int mouth;//日期时间
    private int day;//日期时间
    private int hour;//日期时间
    private int minus;//日期时间
    private String sugar;//血糖：血糖值为:169/18=9.3888

    @Generated(hash = 83632873)
    public NewBloodItem(Long id, int history_id, int memberId, int year, int mouth, int day, int hour, int minus,
            String sugar) {
        this.id = id;
        this.history_id = history_id;
        this.memberId = memberId;
        this.year = year;
        this.mouth = mouth;
        this.day = day;
        this.hour = hour;
        this.minus = minus;
        this.sugar = sugar;
    }

    @Generated(hash = 1102998272)
    public NewBloodItem() {
    }

    public int getHistory_id() {
        return history_id;
    }

    public void setHistory_id(int history_id) {
        this.history_id = history_id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMouth() {
        return mouth;
    }

    public void setMouth(int mouth) {
        this.mouth = mouth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinus() {
        return minus;
    }

    public void setMinus(int minus) {
        this.minus = minus;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
