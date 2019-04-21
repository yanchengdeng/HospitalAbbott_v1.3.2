package com.comvee.hospitalabbott.bean;

import java.util.List;

/**
 * Created by F011512088 on 2017/11/8.
 */

public class TestPaperBean {

    /**
     * pageNum : 1
     * pageSize : 10
     * rows : [{"insertDt":"2018-01-12 09:45:36.0","isValid":"1","level1H":"7","level1L":"5","level2H":"13","level2L":"9","modifyDt":"2018-01-12 20:36:42.0","sid":"180112094500001","testNo":"0091","userId":null,"waterNo":"0091"},{"insertDt":"2017-11-13 17:14:28.0","isValid":"1","level1H":"7.8","level1L":"5.6","level2H":"11.2","level2L":"7.9","modifyDt":"2017-12-19 14:47:42.0","sid":"171113171400001","testNo":"007","userId":null,"waterNo":"007"},{"insertDt":"2017-11-09 10:42:50.0","isValid":"1","level1H":"5","level1L":"4","level2H":"7","level2L":"6","modifyDt":"2017-11-09 10:42:50.0","sid":"171109104300001","testNo":"NO0006","userId":null,"waterNo":"W0000"}]
     * totalPages : 1
     * totalRows : 3
     */

    private int pageNum;
    private int pageSize;
    private int totalPages;
    private int totalRows;
    private List<RowsBean> rows;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * insertDt : 2018-01-12 09:45:36.0
         * isValid : 1
         * level1H : 7
         * level1L : 5
         * level2H : 13
         * level2L : 9
         * modifyDt : 2018-01-12 20:36:42.0
         * sid : 180112094500001
         * testNo : 0091
         * userId : null
         * waterNo : 0091
         */

        private String insertDt;
        private String isValid;
        private String level1H;
        private String level1L;
        private String level2H;
        private String level2L;
        private String modifyDt;
        private String sid;
        private String testNo;
        private String userId;
        private String waterNo;

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

        public String getLevel1H() {
            return level1H;
        }

        public void setLevel1H(String level1H) {
            this.level1H = level1H;
        }

        public String getLevel1L() {
            return level1L;
        }

        public void setLevel1L(String level1L) {
            this.level1L = level1L;
        }

        public String getLevel2H() {
            return level2H;
        }

        public void setLevel2H(String level2H) {
            this.level2H = level2H;
        }

        public String getLevel2L() {
            return level2L;
        }

        public void setLevel2L(String level2L) {
            this.level2L = level2L;
        }

        public String getModifyDt() {
            return modifyDt;
        }

        public void setModifyDt(String modifyDt) {
            this.modifyDt = modifyDt;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getTestNo() {
            return testNo;
        }

        public void setTestNo(String testNo) {
            this.testNo = testNo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getWaterNo() {
            return waterNo;
        }

        public void setWaterNo(String waterNo) {
            this.waterNo = waterNo;
        }
    }
}
