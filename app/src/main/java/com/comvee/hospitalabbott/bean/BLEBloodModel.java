package com.comvee.hospitalabbott.bean;

import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by F011512088 on 2018/2/27.
 */

public class BLEBloodModel implements Serializable {

    private String titleStr;
    private String memberId;
    private String readTime;
    private float value;
    private String paramCode;

    public BLEBloodModel(String readTime, float value) {
        this.readTime = readTime;
        this.value = value;
    }

    public String getReadTime() {
        String tmpReadTime = this.readTime;
        //如果在23点和24点之间，记录时间加一天
        Date readDate = DateUtil.parse(readTime);
        if(DateUtil.isInDate(readDate,"23:00:00","23:59:59") && paramCode!=null && paramCode.equals(TestResultDataUtil.PARAM_CODE_0AM)) {
            readDate = DateUtil.addDay(readDate,1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tmpReadTime = sdf.format(readDate)+" 00:00:00";
        }
        return tmpReadTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getParamCode() {
        if (paramCode == null)
            return "";
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }
}
