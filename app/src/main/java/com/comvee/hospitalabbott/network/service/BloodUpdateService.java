package com.comvee.hospitalabbott.network.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.comvee.greendao.gen.TestInfoDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.bean.TestInfo;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.network.rxbus.BloodUpdateRxModel;
import com.comvee.hospitalabbott.network.rxbus.RxBus;
import com.comvee.hospitalabbott.tool.RxCountDown;
import com.comvee.hospitalabbott.tool.ThreadHandler;
import com.comvee.hospitalabbott.tool.ToastUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by xingkong on 2018/5/14.
 */

public class BloodUpdateService extends Service {
    public boolean isOnUpdating = false;
    public Disposable disposable;
    private Timer timer;
    @Override
    public void onCreate() {
        super.onCreate();
        disposable = RxBus.getDefault().getObservable(BloodUpdateRxModel.class).subscribe(new Consumer<BloodUpdateRxModel>() {
            @Override
            public void accept(BloodUpdateRxModel bloodUpdateRxModel) throws Exception {
                updateAllData(false);
            }
        });
        startRegularUpdateData();
    }

    public void startRegularUpdateData(){
        if (timer != null){
            return;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ThreadHandler.postUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateAllData(true);
                    }
                });
            }
        },60000,60000);
    }

    public void updateAllData(boolean regular){
        if (isOnUpdating){
            ToastUtil.showToast( XTYApplication.getInstance(), "数据正在同步...");
            return;
        }else {
            isOnUpdating = true;
        }
        TestInfoDao dao = XTYApplication.getInstance().getDaoSession().getTestInfoDao();
        final List<TestInfo> mList = dao.loadAll();
        if (mList == null || mList.isEmpty()) {
            if ( XTYApplication.getInstance() != null && !regular){
                ToastUtil.showToast( XTYApplication.getInstance(), "无数据需要同步");
            }
            isOnUpdating = false;
            return;
        }
        if ( XTYApplication.getInstance() != null && !regular){
            ToastUtil.showToast( XTYApplication.getInstance(), "数据开始同步");
        }
        if (mList.size() > 1){
            //间隔三秒上传一条数据
            RxCountDown.countdown(mList.size(),3).subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(Integer integer) {
                    updateData(mList.get(integer));
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void onComplete() {
                    isOnUpdating = false;
                }
            });
        }else {
            updateData(mList.get(0));
            isOnUpdating = false;
        }
    }

    public void updateData(final TestInfo info){
        Intent intent = new Intent();
        intent.putExtra("memberId", info.getMemberId());
        intent.putExtra("recordTime", info.getRecordTime());
        ComveeLoader.getInstance().addMemberParamLog(info)
                .subscribe(new HttpCall<String>(null, intent) {
                    @Override
                    public void onNext(String s) {
                        deleteLocalTestInfo(info);
                        TestInfoDao dao = XTYApplication.getInstance().getDaoSession().getTestInfoDao();
                        if (dao.count() <= 0){
                            ToastUtil.showToast( XTYApplication.getInstance(), "数据同步成功");
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        TestInfoDao dao = XTYApplication.getInstance().getDaoSession().getTestInfoDao();
                        if (dao.count() <= 0){
                            ToastUtil.showToast( XTYApplication.getInstance(), "数据同步成功");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                       e.printStackTrace();
                    }
                });
    }

    public void deleteLocalTestInfo(TestInfo info) {
        TestInfoDao testInfoDao = XTYApplication.getInstance().getDaoSession().getTestInfoDao();
        testInfoDao.deleteInTx(info);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    public class Binder extends android.os.Binder {
        public BloodUpdateService getMyService() {
            return BloodUpdateService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }

        if (timer != null){
            timer.cancel();
        }
    }
}
