package com.comvee.hospitalabbott.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 绑定科室、病床数据
 * Created by F011512088 on 2017/4/12.
 */

public class RowsBean implements Serializable {
    /**
     *  久版本              新版本 （命名规则：两个单词以上为每个单词首字母）
     * departmentId : 1    di": "3"
     * departmentList : [{"bedId":"1","bedNo":"01","departmentId":"1","departmentName":"内分泌科","memberId":"1","memberName":"沈蔚","newSugarMap":{"afterBreakfast":"","afterDinner":"","afterLunch":"","beforeBreakfast":"","beforeDinner":"","beforeLunch":"","beforeSleep":"","beforedawn":"","randomtime":""}},{"bedId":"19","bedNo":"01","departmentId":"1","departmentName":"内分泌科","memberId":"19","memberName":"阮莞","newSugarMap":{"afterBreakfast":"","afterDinner":"","afterLunch":"","beforeBreakfast":"","beforeDinner":"","beforeLunch":"","beforeSleep":"","beforedawn":"","randomtime":""}},{"bedId":"31","bedNo":"01","departmentId":"1","departmentName":"内分泌科","memberId":"31","memberName":"琴青","newSugarMap":{"afterBreakfast":"","afterDinner":"","afterLunch":"","beforeBreakfast":"","beforeDinner":"","beforeLunch":"","beforeSleep":"","beforedawn":"","randomtime":""}},{"bedId":"3","bedNo":"01","departmentId":"1","departmentName":"内分泌科","memberId":"3","memberName":"容勉","newSugarMap":{"afterBreakfast":"","afterDinner":"","afterLunch":"","beforeBreakfast":"","beforeDinner":"","beforeLunch":"","beforeSleep":"","beforedawn":"","randomtime":""}},{"bedId":"13","bedNo":"01","departmentId":"1","departmentName":"内分泌科","memberId":"13","memberName":"顾韶","newSugarMap":{"afterBreakfast":"","afterDinner":"","afterLunch":"","beforeBreakfast":"","beforeDinner":"","beforeLunch":"","beforeSleep":"","beforedawn":"","randomtime":""}},{"bedId":"32","bedNo":"02","departmentId":"1","departmentName":"内分泌科","memberId":"32","memberName":"寇仲","newSugarMap":{"afterBreakfast":"","afterDinner":"","afterLunch":"","beforeBreakfast":"","beforeDinner":"","beforeLunch":"","beforeSleep":"","beforedawn":"","randomtime":""}},{"bedId":"38","bedNo":"02","departmentId":"1","departmentName":"内分泌科","memberId":"170323165600010","memberName":"水月华","newSugarMap":{"afterBreakfast":"","afterDinner":"","afterLunch":"","beforeBreakfast":"","beforeDinner":"","beforeLunch":"","beforeSleep":"","beforedawn":"","randomtime":""}},{"bedId":"14","bedNo":"02","departmentId":"1","departmentName":"内分泌科","memberId":"14","memberName":"容钰","newSugarMap":{"afterBreakfast":"","afterDinner":"","afterLunch":"","beforeBreakfast":"","beforeDinner":"","beforeLunch":"","beforeSleep":"","beforedawn":"","randomtime":""}},{"bedId":"2","bedNo":"02","departmentId":"1","departmentName":"内分泌科","memberId":"2","memberName":"顾迟方","newSugarMap":{"afterBreakfast":"","afterDinner":"","afterLunch":"","beforeBreakfast":"","beforeDinner":"","beforeLunch":"","beforeSleep":"","beforedawn":"","randomtime":""}},{"bedId":"26","bedNo":"02","departmentId":"1","departmentName":"内分泌科","memberId":"26","memberName":"风亦乐","newSugarMap":{"afterBreakfast":"","afterDinner":"","afterLunch":"","beforeBreakfast":"","beforeDinner":"","beforeLunch":"","beforeSleep":"","beforedawn":"","randomtime":"", sex:"", }}]
     * departmentName : 内分泌科  "dn": "内二科"
     */

//    private String departmentId;
//    private String departmentName;
    private String di;
    private String dn;
    private List<Rows> departmentList;

    public String getDepartmentId() {
        return di;
    }

    public void setDepartmentId(String departmentId) {
        this.di = departmentId;
    }

    public String getDepartmentName() {
        return dn;
    }

    public void setDepartmentName(String departmentName) {
        this.dn = departmentName;
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

    public List<Rows> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Rows> departmentList) {
        this.departmentList = departmentList;
    }

    @Override
    public String toString() {
        return "RowsBean{" +
                ", departmentId='" + di + '\'' +
                ", departmentName='" + dn + '\'' +
                '}';
    }

}
