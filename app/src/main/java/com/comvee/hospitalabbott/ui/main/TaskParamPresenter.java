package com.comvee.hospitalabbott.ui.main;

import com.comvee.hospitalabbott.base.BasePresenter;

/**
 * Created by F011512088 on 2018/1/25.
 */
public class TaskParamPresenter extends BasePresenter<TaskParamViewController> {
    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param model
     */
    public TaskParamPresenter(TaskParamViewController model) {
        super(model);
    }

    @Override
    public void initData() {

    }
}
