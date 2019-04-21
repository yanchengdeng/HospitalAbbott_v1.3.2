package com.comvee.hospitalabbott.ui.detection;

import android.widget.EditText;

import com.comvee.hospitalabbott.base.BaseViewController;

/**
 * Created by F011512088 on 2018/2/27.
 */
public interface MemberResultViewController extends BaseViewController{

    String getRemarksStr();

    void dismissProgress();

    void finishActivity();

    void setReference(String paranCode);
}
