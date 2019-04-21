package com.comvee.hospitalabbott.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by F011512088 on 2017/4/13.
 */
@Entity
public class BloodRangeBean  {//implements Parcelable

    @Id
    private Long id;
    /**
     * highAfterMeal : 100
     * highBeforeBreakfast : 3
     * highBeforeMeal : 100
     * highBeforeSleep : 100
     * highBeforedawn : 100
     * insertDt : 2017-02-17 09:40:41
     * isValid : 1
     * lowAfterMeal : 55
     * lowBeforeBreakfast : 2
     * lowBeforeMeal : 55
     * lowBeforeSleep : 55
     * lowBeforedawn : 55
     * memberId : 1
     * modifyDt : 2017-04-23 15:51:39
     * rangeId : 170217094000001
     */

    private String highAfterMeal;
    private String highBeforeBreakfast;
    private String highBeforeMeal;
    private String highBeforeSleep;
    private String highBeforedawn;
    private String insertDt;
    private String isValid;
    private String lowAfterMeal;
    private String lowBeforeBreakfast;
    private String lowBeforeMeal;
    private String lowBeforeSleep;
    private String lowBeforedawn;
    private String memberId;
    private String modifyDt;
    private String rangeId;

    public BloodRangeBean(String lowBeforedawn, String highAfterMeal,
                          String highBeforeBreakfast, String highBeforeMeal,
                          String highBeforeSleep, String highBeforedawn,
                          String insertDt, String isValid, String lowAfterMeal,
                          String lowBeforeBreakfast, String lowBeforeMeal,
                          String lowBeforeSleep) {
        this.lowBeforedawn = lowBeforedawn;
        this.highAfterMeal = highAfterMeal;
        this.highBeforeBreakfast = highBeforeBreakfast;
        this.highBeforeMeal = highBeforeMeal;
        this.highBeforeSleep = highBeforeSleep;
        this.highBeforedawn = highBeforedawn;
        this.insertDt = insertDt;
        this.isValid = isValid;
        this.lowAfterMeal = lowAfterMeal;
        this.lowBeforeBreakfast = lowBeforeBreakfast;
        this.lowBeforeMeal = lowBeforeMeal;
        this.lowBeforeSleep = lowBeforeSleep;
    }

    @Generated(hash = 376845502)
    public BloodRangeBean(Long id, String highAfterMeal, String highBeforeBreakfast,
            String highBeforeMeal, String highBeforeSleep, String highBeforedawn,
            String insertDt, String isValid, String lowAfterMeal,
            String lowBeforeBreakfast, String lowBeforeMeal, String lowBeforeSleep,
            String lowBeforedawn, String memberId, String modifyDt,
            String rangeId) {
        this.id = id;
        this.highAfterMeal = highAfterMeal;
        this.highBeforeBreakfast = highBeforeBreakfast;
        this.highBeforeMeal = highBeforeMeal;
        this.highBeforeSleep = highBeforeSleep;
        this.highBeforedawn = highBeforedawn;
        this.insertDt = insertDt;
        this.isValid = isValid;
        this.lowAfterMeal = lowAfterMeal;
        this.lowBeforeBreakfast = lowBeforeBreakfast;
        this.lowBeforeMeal = lowBeforeMeal;
        this.lowBeforeSleep = lowBeforeSleep;
        this.lowBeforedawn = lowBeforedawn;
        this.memberId = memberId;
        this.modifyDt = modifyDt;
        this.rangeId = rangeId;
    }

    @Generated(hash = 158546298)
    public BloodRangeBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getHighAfterMeal() {
        return highAfterMeal;
    }

    public void setHighAfterMeal(String highAfterMeal) {
        this.highAfterMeal = highAfterMeal;
    }

    public String getHighBeforeBreakfast() {
        return highBeforeBreakfast;
    }

    public void setHighBeforeBreakfast(String highBeforeBreakfast) {
        this.highBeforeBreakfast = highBeforeBreakfast;
    }

    public String getHighBeforeMeal() {
        return highBeforeMeal;
    }

    public void setHighBeforeMeal(String highBeforeMeal) {
        this.highBeforeMeal = highBeforeMeal;
    }

    public String getHighBeforeSleep() {
        return highBeforeSleep;
    }

    public void setHighBeforeSleep(String highBeforeSleep) {
        this.highBeforeSleep = highBeforeSleep;
    }

    public String getHighBeforedawn() {
        return highBeforedawn;
    }

    public void setHighBeforedawn(String highBeforedawn) {
        this.highBeforedawn = highBeforedawn;
    }

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

    public String getLowAfterMeal() {
        return lowAfterMeal;
    }

    public void setLowAfterMeal(String lowAfterMeal) {
        this.lowAfterMeal = lowAfterMeal;
    }

    public String getLowBeforeBreakfast() {
        return lowBeforeBreakfast;
    }

    public void setLowBeforeBreakfast(String lowBeforeBreakfast) {
        this.lowBeforeBreakfast = lowBeforeBreakfast;
    }

    public String getLowBeforeMeal() {
        return lowBeforeMeal;
    }

    public void setLowBeforeMeal(String lowBeforeMeal) {
        this.lowBeforeMeal = lowBeforeMeal;
    }

    public String getLowBeforeSleep() {
        return lowBeforeSleep;
    }

    public void setLowBeforeSleep(String lowBeforeSleep) {
        this.lowBeforeSleep = lowBeforeSleep;
    }

    public String getLowBeforedawn() {
        return lowBeforedawn;
    }

    public void setLowBeforedawn(String lowBeforedawn) {
        this.lowBeforedawn = lowBeforedawn;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getModifyDt() {
        return modifyDt;
    }

    public void setModifyDt(String modifyDt) {
        this.modifyDt = modifyDt;
    }

    public String getRangeId() {
        return rangeId;
    }

    public void setRangeId(String rangeId) {
        this.rangeId = rangeId;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(highAfterMeal);
//        dest.writeString(highBeforeBreakfast);
//        dest.writeString(highBeforeMeal);
//        dest.writeString(highBeforeSleep);
//        dest.writeString(highBeforedawn);
//        dest.writeString(insertDt);
//        dest.writeString(isValid);
//        dest.writeString(lowAfterMeal);
//        dest.writeString(lowBeforeBreakfast);
//        dest.writeString(lowBeforeMeal);
//        dest.writeString(lowBeforeSleep);
//        dest.writeString(lowBeforedawn);
//        dest.writeString(memberId);
//        dest.writeString(modifyDt);
//        dest.writeString(rangeId);
//    }

}
