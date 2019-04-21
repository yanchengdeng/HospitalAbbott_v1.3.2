package com.comvee.hospitalabbott.ui.history;

import android.content.Intent;

import com.comvee.greendao.gen.RefreshHistoryModelDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.bean.HistoryCountModel;
import com.comvee.hospitalabbott.bean.HistoryModel;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.bean.RefreshHistoryModel;
import com.comvee.hospitalabbott.helper.HistoryHelper;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.tool.RxJavaUtil;
import com.comvee.hospitalabbott.ui.detection.TestBloodActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by F011512088 on 2018/1/30.
 */
public class HistoryPresenter extends BasePresenter<HistoryViewController> {


    private String refreshTime = "";//刷新时间
    private HospitalBed hospitalBed;

    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param viewModel
     */
    public HistoryPresenter(HistoryViewController viewModel) {
        super(viewModel);
    }

    @Override
    public void initData() {

    }

    public boolean isOneLoad() {
        if (hospitalBed == null)
            return false;
        RefreshHistoryModelDao modelDao = XTYApplication.getInstance().getDaoSession().getRefreshHistoryModelDao();
        RefreshHistoryModel historyModel = modelDao.queryBuilder()
                .where(RefreshHistoryModelDao.Properties.
                        MemberId.eq(hospitalBed.getMemberId())).build().unique();
        if (historyModel == null)
            return true;
        else {
            return false;
        }
    }

    public void setHospitalBed(Intent intent) {
        if (intent != null) {
            hospitalBed = (HospitalBed) intent.getSerializableExtra(TestBloodActivity.MEMBER_BEAN);
        }
    }

    public void getAllData() {
        ComveeLoader.getInstance().getMemberParamLog(hospitalBed.getMemberId(), "", "", "")
                .subscribe(new HttpCall<HistoryCountModel>(viewModel.getActivity()) {
                    @Override
                    public void onNext(HistoryCountModel historyModel) {
                        viewModel.setHistoryList(historyModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getAllListData(hospitalBed.getMemberId());
                    }
                });
    }

    public void getAllListData(final String memberId) {
        Observable.create(new ObservableOnSubscribe<HistoryCountModel>() {
            @Override
            public void subscribe(ObservableEmitter<HistoryCountModel> e) throws Exception {
                HistoryCountModel countModel = HistoryHelper.getLocalHistory(memberId);
                e.onNext(countModel);
                e.onComplete();
            }
        }).compose(RxJavaUtil.<HistoryCountModel>toComputation())
                .subscribe(new Consumer<HistoryCountModel>() {
                    @Override
                    public void accept(@NonNull HistoryCountModel countModel) throws Exception {
                        viewModel.setHistoryList(countModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        viewModel.setHistoryList(new HistoryCountModel());
                    }
                });
    }

    public String getTitle() {
        if (hospitalBed != null)
            return hospitalBed.getBedNo() + "床 " + hospitalBed.getMemberName();
        return "";
    }

    public String getMemberId() {
        if (hospitalBed != null)
            return hospitalBed.getMemberId();
        return "";
    }

    /*public void refreshData() {
        RefreshHistoryModel refreshModel = XTYApplication.getInstance().getDaoSession()
                .getRefreshHistoryModelDao().queryBuilder()
                .where(RefreshHistoryModelDao.Properties.MemberId.eq(getMemberId())).build().unique();
        if (refreshModel != null)
            refreshTime = refreshModel.getRefreshTime();
        else
            refreshTime = DateUtil.getTimeString();
//        LogUtils.e("refreshTime =" + refreshTime);
        Intent intent = new Intent();
        intent.putExtra("memberId", getMemberId());
        ComveeLoader.getInstance().refreshParamLogList(refreshTime, getMemberId(), "", "")
                .subscribe(new HttpCall<HistoryModel>(viewModel.getActivity(), intent) {
                    @Override
                    public void onNext(HistoryModel model) {
                        getAllListData(getIntent().getStringExtra("memberId"));
                    }

                    @Override
                    public void onError(Throwable e) {
                        getAllListData(getIntent().getStringExtra("memberId"));
                    }
                });
    }*/
}
