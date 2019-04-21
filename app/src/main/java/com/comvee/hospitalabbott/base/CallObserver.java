package com.comvee.hospitalabbott.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class CallObserver<T> implements Observer<T> {

    private Disposable disposable;

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }


    @Override
    public void onComplete() {
        disposable = null;
    }

    public void disposable() {
        if (disposable != null && !disposable.isDisposed())
                disposable.dispose();

    }
}