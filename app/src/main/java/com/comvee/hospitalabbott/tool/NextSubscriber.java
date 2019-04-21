package com.comvee.hospitalabbott.tool;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by F011512088 on 2017/8/17.
 */

public abstract class NextSubscriber<T> implements Observer<T> {



    public NextSubscriber() {
        super();
    }

    @Override
    public void onComplete() {

    }
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        LogUtils.e(e.getMessage());
    }

    @Override
    public void onNext(T t) {
        call(t);
    }

    public abstract void call(T t);
}
