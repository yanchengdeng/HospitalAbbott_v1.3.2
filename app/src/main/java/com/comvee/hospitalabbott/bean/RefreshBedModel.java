package com.comvee.hospitalabbott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by F011512088 on 2017/4/26.
 */

public class RefreshBedModel implements Serializable {

    /**
     * departmentList : [{"addBed":null,"bed":null,"cityName":null,"departId":null,"departName":null,"departPName":null,"departPid":null,"departType":null,"departmentDesc":"科室简介","departmentId":"1","departmentName":"内分泌科","districtName":null,"hospitalId":null,"hospitalName":null,"insertDt":null,"isValid":null,"modifyDt":null,"optionType":null,"provinceName":null,"userId":null}]
     * outHospitalList : [{"memberId":2},{"memberId":2}]
     * returnTime : 2018-01-12 19:58:03
     * rows : [{{"bi": "1","bn": "01","cs": "1","departId": "1","di": "1","dn": "内分泌科","mi": "1","mn": "沈蔚",
                "newSugarMap": {
                "ab": {"level": "3","mi": "1","pc": "afterBreakfast","pli": "180117101200120","remark": "","ro": null,"rt": "2018-01-17 03:03:0","status": "1","value": "7"},
                "ad": {"level": "3","mi": "1","pc": "afterDinner","pli": "180117101300128","remark": "","ro": null,"rt": "2018-01-17 10:15:0","status": "1","value": "6"},
                "al": {"level": "1","mi": "1","pc": "afterLunch","pli": "180117144400026","remark": "","ro": null,"rt": "2018-01-17 14:41:51","status": "2","value": "0"},
                "bb": {"level": "3","mi": "1","pc": "beforeBreakfast","pli": "180117095900015","remark": "","ro": null,"rt": "2018-01-17 12:01:0","status": "1","value": "5.6"},
                "bd": {"level": "5","mi": "1","pc": "beforeDinner","pli": "180117101200126","remark": "","ro": null,"rt": "2018-01-17 20:14:0","status": "1","value": "34"},
                "beforedawn": {"level": "1","mi": "1","pc": "beforedawn","pli": "180117140100005","remark": "3","ro": "web","rt": "2018-01-17 02:03","status": "1","value": "3"},
                "bl": {"level": "1","mi": "1","pc": "beforeLunch","pli": "180117112500041","remark": "","ro": null,"rt": "2018-01-17 11:24:56","status": "2","value": "0"},
                "bs": {"level": "3","mi": "1","pc": "beforeSleep","pli": "180117101300130","remark": "","ro": null,"rt": "2018-01-17 10:14:0","status": "1","value": "4"},
                "randomtime": {"level": "5","mi": "1","pc": "randomtime","pli": "180117101300134","remark": "","ro": null,"rt": "2018-01-17 20:16:0","status": "1","value": "12"}
                },
                "sex": "1",
                "ss": ""
                }}]
     * totalPages : 0
     * totalRows : 0
     */

    private String returnTime;
    private int pageNum;
    private int totalPages;
    private int totalRows;
    private List<DepartmentListBean> departmentList;
    private List<OutHospitalListBean> outHospitalList;
    private List<Rows> rows;

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public List<DepartmentListBean> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<DepartmentListBean> departmentList) {
        this.departmentList = departmentList;
    }

    public List<OutHospitalListBean> getOutHospitalList() {
        return outHospitalList;
    }

    public void setOutHospitalList(List<OutHospitalListBean> outHospitalList) {
        this.outHospitalList = outHospitalList;
    }

    public List<Rows> getRows() {
        return rows;
    }

    public void setRows(List<Rows> rows) {
        this.rows = rows;
    }

    public static class DepartmentListBean {
        /**
         * addBed : null
         * bed : null
         * cityName : null
         * departId : null
         * departName : null
         * departPName : null
         * departPid : null
         * departType : null
         * departmentDesc : 科室简介
         * departmentId : 1
         * departmentName : 内分泌科
         * districtName : null
         * hospitalId : null
         * hospitalName : null
         * insertDt : null
         * isValid : null
         * modifyDt : null
         * optionType : null
         * provinceName : null
         * userId : null
         */

        private Object addBed;
        private Object bed;
        private Object cityName;
        private Object departId;
        private Object departName;
        private Object departPName;
        private Object departPid;
        private Object departType;
        private String departmentDesc;
        private String departmentId;
        private String departmentName;
        private Object districtName;
        private Object hospitalId;
        private Object hospitalName;
        private Object insertDt;
        private Object isValid;
        private Object modifyDt;
        private Object optionType;
        private Object provinceName;
        private Object userId;

        public Object getAddBed() {
            return addBed;
        }

        public void setAddBed(Object addBed) {
            this.addBed = addBed;
        }

        public Object getBed() {
            return bed;
        }

        public void setBed(Object bed) {
            this.bed = bed;
        }

        public Object getCityName() {
            return cityName;
        }

        public void setCityName(Object cityName) {
            this.cityName = cityName;
        }

        public Object getDepartId() {
            return departId;
        }

        public void setDepartId(Object departId) {
            this.departId = departId;
        }

        public Object getDepartName() {
            return departName;
        }

        public void setDepartName(Object departName) {
            this.departName = departName;
        }

        public Object getDepartPName() {
            return departPName;
        }

        public void setDepartPName(Object departPName) {
            this.departPName = departPName;
        }

        public Object getDepartPid() {
            return departPid;
        }

        public void setDepartPid(Object departPid) {
            this.departPid = departPid;
        }

        public Object getDepartType() {
            return departType;
        }

        public void setDepartType(Object departType) {
            this.departType = departType;
        }

        public String getDepartmentDesc() {
            return departmentDesc;
        }

        public void setDepartmentDesc(String departmentDesc) {
            this.departmentDesc = departmentDesc;
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

        public Object getDistrictName() {
            return districtName;
        }

        public void setDistrictName(Object districtName) {
            this.districtName = districtName;
        }

        public Object getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(Object hospitalId) {
            this.hospitalId = hospitalId;
        }

        public Object getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(Object hospitalName) {
            this.hospitalName = hospitalName;
        }

        public Object getInsertDt() {
            return insertDt;
        }

        public void setInsertDt(Object insertDt) {
            this.insertDt = insertDt;
        }

        public Object getIsValid() {
            return isValid;
        }

        public void setIsValid(Object isValid) {
            this.isValid = isValid;
        }

        public Object getModifyDt() {
            return modifyDt;
        }

        public void setModifyDt(Object modifyDt) {
            this.modifyDt = modifyDt;
        }

        public Object getOptionType() {
            return optionType;
        }

        public void setOptionType(Object optionType) {
            this.optionType = optionType;
        }

        public Object getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(Object provinceName) {
            this.provinceName = provinceName;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }
    }

    public static class OutHospitalListBean {
        /**
         * memberId : "2"
         */

        private String memberId;


        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }
    }
}
