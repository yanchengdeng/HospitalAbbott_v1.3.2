package com.comvee.hospitalabbott.bean;

import java.util.List;

/**
 * Created by F011512088 on 2017/6/13.
 */

public class UncheckinBean {

    /**
     * pager : {"currentPage":1,"endRow":2,"firstPage":false,"getCount":true,"lastPage":true,"pageSize":2,"startRow":0,"totalPages":10,"totalRows":20}
     * ROWS : [{"departmentId":"170417095600001","insertDt":"2017-06-13 15:56:06","isValid":"1","modifyDt":"2017-06-13 15:56:06","optionUserId":"170417095200001","paramLogId":"170613155600001","recordOrigin":"XTY_ORIGIN","recordTime":"2017-06-13 15:56:05","remark":"距离线哦推荐","status":"1","value":"12.0"},{"departmentId":"170417095600001","insertDt":"2017-06-13 15:56:06","isValid":"1","modifyDt":"2017-06-13 15:56:06","optionUserId":"170417095200001","paramLogId":"170613155600002","recordOrigin":"XTY_ORIGIN","recordTime":"2017-06-13 15:56:04","remark":"距离线哦推荐","status":"1","value":"12.0"}]
     */

    private PagerBean pager;
    private List<RowsBean> rows;

    public PagerBean getPager() {
        return pager;
    }

    public void setPager(PagerBean pager) {
        this.pager = pager;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

}
