package com.comvee.hospitalabbott.ui.search;

import com.comvee.hospitalabbott.base.BaseViewController;
import com.comvee.hospitalabbott.bean.SearchBean;

import java.util.List;

/**
 * Created by F011512088 on 2018/1/29.
 */
public interface SearchViewController extends BaseViewController{

   void onNetSearchList(List<SearchBean> searchBeen);

    void onNetError(String errorStr);

    void onLocalSearch(List<SearchBean> searchBeen);

}
