package com.comvee.hospitalabbott.base;

import android.app.Activity;

public interface BaseViewController {
    //这里面添加实现类需要实现的方法即可
    void showProgress();

    Activity getActivity();
}