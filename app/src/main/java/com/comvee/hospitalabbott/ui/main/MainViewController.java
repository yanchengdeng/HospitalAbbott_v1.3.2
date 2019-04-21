package com.comvee.hospitalabbott.ui.main;

import android.support.v4.app.Fragment;

import com.comvee.hospitalabbott.base.BaseViewController;
import com.comvee.hospitalabbott.bean.MemberCountBean;

/**
 * Created by F011512088 on 2018/1/25.
 */
public interface MainViewController extends BaseViewController{

    void addFragment(Fragment fragment);

    void setCountView(MemberCountBean response);

    int getPositionsFragment();

    void dismissProgressDialog();

    void getAllData();

    void onRefreshData();//刷新成功更新数据

    void onRefreshError();//刷新失败

}
