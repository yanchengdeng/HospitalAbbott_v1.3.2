package com.comvee.hospitalabbott.tool;

import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by comv098 on 2017/2/17.
 */

public class RxJavaUtil {

    /**
     * Schedulers.computation( )	用于计算任务，如事件循环或和回调处理，不要用于IO操作(IO操作请使用Schedulers.io())；默认线程数等于处理器的数量
     * Schedulers.from(executor)	使用指定的Executor作为调度器
     * Schedulers.immediate( )	在当前线程立即开始执行任务
     * Schedulers.io( )	用于IO密集型任务，如异步阻塞IO操作，这个调度器的线程池会根据需要增长；
     * 对于普通的计算任务，请使用Schedulers.computation()；Schedulers.io( )默认是一个CachedThreadScheduler，很像一个有线程缓存的新线程调度器
     * Schedulers.newThread( )	为每个任务创建一个新线程
     * Schedulers.trampoline( )	当其它排队的任务完成后，在当前线程排队开始执行
     * <p>
     * 使用compose不要打断链式结构 线程切换
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 计算
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> toComputation() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.computation())
                        .unsubscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 500毫秒防重复点击
     *
     * @param view
     * @param action1
     */
    public static void clicks(View view, NextSubscriber action1) {
        RxView.clicks(view)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

}
