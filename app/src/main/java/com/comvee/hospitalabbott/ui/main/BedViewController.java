package com.comvee.hospitalabbott.ui.main;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.comvee.hospitalabbott.base.BaseViewController;
import com.comvee.hospitalabbott.bean.DepartmentModel;
import com.comvee.hospitalabbott.bean.HospitalBed;

import java.util.List;

/**
 * Created by F011512088 on 2018/1/24.
 */

public interface BedViewController extends BaseViewController {

    void setAdapterList(List<HospitalBed> MultiList);

    void refreshComplete();

    void loadedDepartmentList(List<DepartmentModel> list);

    void dismissProgressDialog();
}
