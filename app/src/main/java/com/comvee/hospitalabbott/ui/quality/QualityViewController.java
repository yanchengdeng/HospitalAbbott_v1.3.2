package com.comvee.hospitalabbott.ui.quality;

import com.comvee.hospitalabbott.base.BaseViewController;

/**
 * Created by F011512088 on 2018/2/24.
 */
public interface QualityViewController extends BaseViewController {

    void onNetWorkSuccess();

    void onNetWorkError();

    void setPaperNumber(String paperNumber);

    void setQualityNumber(String qualityNumber);

    void setQualityControlRange(String rangeStr);

    void dismissProgress();
}
