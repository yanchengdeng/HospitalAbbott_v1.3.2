package com.comvee.hospitalabbott.widget.magicindicator.buildins.commonnavigator.model;

import com.comvee.hospitalabbott.tool.TestResultDataUtil;

/**
 * Created by F011512088 on 2017/12/14.
 */

public class TitleData {
    private String title;
    private String paramCode;
    private String num;
    private int position;

    public TitleData(String title, int num,int position) {
        this.title = title;
        this.paramCode = TestResultDataUtil.getParamCode(title);
        this.num = num+"";
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position){
        this.position = position;
    }
}
