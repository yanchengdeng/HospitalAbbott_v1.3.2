package com.comvee.hospitalabbott.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by F011512088 on 2017/5/18.
 */

public class HistoryModel implements Serializable {
    /**
     * memberHistory : [{"date":"2017-05-17","paramLogList":[{"adviseContent":"","batchId":"","bedNo":"","departmentName":"","highEmpty":"","highFull":"","innerCode":"","innerId":"864214018356941","insertDt":"2017-05-17 10:43:44","isValid":"1","level":"1","lowEmpty":"","lowFull":"","memberId":"170414180300001","memberName":"","modifyDt":"2017-05-17 10:43:44","optionUserId":"","optionUserName":"","paramCode":"beforeBreakfast","paramLogId":"170517104300006","paramOption":"","processed":"","processedDt":"","processedMsg":"","recordDt":"2017-05-17","recordOrigin":"XTY_ORIGIN","recordTime":"2017-05-17 10:43:43","remark":"","status":"1","value":"4.0"}]}]
     * refreshTime : 2017-05-17 17:05:22
     */

    private String refreshTime;
    private List<MemberHistoryBean> memberHistory;

    public List<MemberHistoryBean> getMemberHistory() {
        return memberHistory;
    }

    public void setMemberHistory(List<MemberHistoryBean> memberHistory) {
        this.memberHistory = memberHistory;
    }

    public String getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime) {
        this.refreshTime = refreshTime;
    }
}
