package com.comvee.hospitalabbott.ui.main;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.comvee.hospitalabbott.base.BaseViewController;

import java.util.List;

/**
 * Created by F011512088 on 2018/1/25.
 */
public interface TaskIndexViewController extends BaseViewController{

    void setAdapterList(List<MultiItemEntity> MultiList);

    void upDataMagicIndicator(int position);

    void autoRefresh();

    void onRefreshError();
}
