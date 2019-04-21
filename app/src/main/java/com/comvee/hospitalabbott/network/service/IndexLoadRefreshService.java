package com.comvee.hospitalabbott.network.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.bean.MemberCountBean;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.network.rxbus.IndexRefreshRxModel;
import com.comvee.hospitalabbott.network.rxbus.IndexUiRxModel;
import com.comvee.hospitalabbott.network.rxbus.RxBus;
import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.tool.RxCountDown;
import com.comvee.hospitalabbott.tool.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by xingkong on 2018/5/14.
 * 首页病床加载与刷新
 */

public class IndexLoadRefreshService extends Service {
    public boolean isOnLoading = false;
    public Disposable disposable, timerDisposable;

    @Override
    public void onCreate() {
        super.onCreate();
        disposable = RxBus.getDefault().getObservable(IndexRefreshRxModel.class).subscribe(new Consumer<IndexRefreshRxModel>() {
            @Override
            public void accept(IndexRefreshRxModel bloodUpdateRxModel) throws Exception {
                refreshData(bloodUpdateRxModel.tag,bloodUpdateRxModel.concernStatus);
            }
        });
        refreshData("ALL", "");
    }

    public void refreshData(String tag, String concernStatus) {
        Log.d("lololo", "refreshData() returned:请求数据 " );
        if (isOnLoading) {
            Log.d("lololo", "refreshData() returned:请求驳回 " );
            return;
        } else {
            isOnLoading = true;
            timer();
        }
        if (UserHelper.isInitBed()) {
            Log.d("lololo", "refreshData() returned:请求刷新 " );
            refreshBedList(1, concernStatus);
        } else {
            Log.d("lololo", "refreshData() returned:请求加载数据 " );
            getNewAllData(1, tag, concernStatus);
        }
        getMemberSmbgCount();
    }

    public void timer(){
        RxCountDown.countdown(20).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                timerDisposable = disposable;
            }

            @Override
            public void onNext(Integer integer) {
                Log.d("lololo", "refreshData() returned:倒计时" + integer );
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                isOnLoading = false;
                Log.d("lololo", "refreshData() returned:倒计时完成 " );
            }
        });
    }

    /**
     * 第一次获取全部的数据
     *
     * @param page
     * @param tag
     * @param concernStatus
     */
    public void getNewAllData(final int page, final String tag, final String concernStatus) {
        RxBus.getDefault().post(new IndexUiRxModel("showProgress"));
        ComveeLoader.getInstance().getBedListByPage(page, ComveeLoader.ROWS)
                .subscribe(new HttpCall<Boolean>(null){
                    @Override
                    public void onNext(Boolean isComplete) {
                        ToastUtil.showToast("加载全部，成功回调");
                        if (isComplete) { //加载完成
                            RxBus.getDefault().post(new IndexUiRxModel("dismissProgressDialog"));
                            RxBus.getDefault().post(new IndexUiRxModel("getAllData"));
                            refreshComplete();
                        } else {
                            int newPage = page + 1;
                            getNewAllData(newPage, tag, concernStatus);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtil.showToast("加载全部，失败: " +  e.getMessage());
                        RxBus.getDefault().post(new IndexUiRxModel("dismissProgressDialog"));
                        RxBus.getDefault().post(new IndexUiRxModel("onRefreshError"));
                        refreshComplete();
                    }
                });
    }

    /**
     * 刷新接口
     *
     * @param page
     * @param concernStatus
     */
    private void refreshBedList(final int page, final String concernStatus) {
        String refreshTime = UserHelper.getString(UserHelper.refreshBedTime);
        ComveeLoader.getInstance().refreshBedList(TextUtils.isEmpty(refreshTime) ? DateUtil.getTimeString() : refreshTime,
                "", UserHelper.getMachineNo(), page, ComveeLoader.ROWS)
                .subscribe(new HttpCall<Boolean>(null) {
                    @Override
                    public void onNext(Boolean isAll) {
                       // ToastUtil.showToast("刷新成功");
                        if (isAll) {
                            RxBus.getDefault().post(new IndexUiRxModel("onRefreshData"));
                            refreshComplete();
                        } else {
                            int i = page + 1;
                            refreshBedList(i, concernStatus);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtil.showToast("刷新失败：" + e.getMessage());
                        RxBus.getDefault().post(new IndexUiRxModel("onRefreshError"));
                        refreshComplete();
                    }
                });
    }

    /**
     * 获取今日任务未测试数量
     */
    public void getMemberSmbgCount() {
        ComveeLoader.getInstance().getMemberSmbgCount()
                .subscribe(new HttpCall<MemberCountBean>(null) {
                    @Override
                    public void onNext(MemberCountBean response) {
                        RxBus.getDefault().post(new IndexUiRxModel("setCountView",response));
                    }
                });
    }

    public void refreshComplete(){
        Log.d("lololo", "refreshData() returned:请求完成 " );
        isOnLoading = false;
        if (timerDisposable != null && !timerDisposable.isDisposed()){
            timerDisposable.dispose();
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    public class Binder extends android.os.Binder {
        public IndexLoadRefreshService getMyService() {
            return IndexLoadRefreshService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
