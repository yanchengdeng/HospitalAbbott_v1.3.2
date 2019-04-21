package com.comvee.hospitalabbott.ui.history;

import com.comvee.hospitalabbott.base.BaseViewController;
import com.comvee.hospitalabbott.bean.HistoryCountModel;
import com.comvee.hospitalabbott.bean.HistoryModel;

/**
 * Created by F011512088 on 2018/1/30.
 */
public interface HistoryViewController extends BaseViewController {

    void setHistoryList(HistoryCountModel historyModel);

}
