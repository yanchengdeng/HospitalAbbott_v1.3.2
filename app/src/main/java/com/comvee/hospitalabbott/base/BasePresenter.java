package com.comvee.hospitalabbott.base;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<D extends BaseViewController> {

    private List<Disposable> mDisposableList = new ArrayList<>();

    public D viewModel;

    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     */
    public BasePresenter(D viewModel) {
        this.viewModel = viewModel;
    }

    public abstract void initData();

    /**
     * 解除网络数据绑定
     */
    public void unBind() {
        for (Disposable d : mDisposableList) {
            if (d != null)
                d.dispose();
        }
    }

    public void addDisposable(Disposable d) {
        if (mDisposableList == null)
            mDisposableList = new ArrayList<>();
        mDisposableList.add(d);

    }

    public void removeAllDisposable() {
        if (mDisposableList == null)
            mDisposableList.clear();
    }

}