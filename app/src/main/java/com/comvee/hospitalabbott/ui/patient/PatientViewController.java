package com.comvee.hospitalabbott.ui.patient;

import com.comvee.hospitalabbott.base.BaseViewController;
import com.comvee.hospitalabbott.bean.SearchBean;

import java.util.List;

/**
 * Created by F011512088 on 2018/1/30.
 */
public interface PatientViewController extends BaseViewController{

    void setSearchList(List<SearchBean> searchList);
}
