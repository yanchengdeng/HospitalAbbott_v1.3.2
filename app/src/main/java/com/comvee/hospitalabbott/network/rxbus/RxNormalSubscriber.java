package com.comvee.hospitalabbott.network.rxbus;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * des:订阅封装
 * Created by xsf
 * on 2016.09.10:16
 */

public abstract class RxNormalSubscriber<T> implements Observer<T> {
    Disposable disposable;
    public RxNormalSubscriber() {

    }
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.disposable = d;
    }

    @Override
    public void onComplete() {
        disposable.dispose();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
}
