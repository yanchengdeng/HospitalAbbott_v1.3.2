package com.comvee.hospitalabbott.ui.record;

import android.content.Intent;
import android.view.View;

import com.comvee.hospitalabbott.base.BaseViewController;

/**
 * Created by F011512088 on 2018/1/29.
 */
public interface ManualRecordViewController extends BaseViewController {

    void initBloodPop();

    void showBloodPop();

    void initCalendarPop();

    void showCalendarPop(View view);


    void upDataReference(String referenceStr);

    void upDataCircleProgress(float bloodValues, int type);

}
