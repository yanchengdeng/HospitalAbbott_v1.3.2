package com.comvee.hospitalabbott.ui.main;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.base.CallObserver;
import com.comvee.hospitalabbott.bean.DepartmentModel;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.helper.BedHelper;
import com.comvee.hospitalabbott.network.rxbus.RxNormalSubscriber;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by F011512088 on 2018/1/24.
 */

public class BedPresenter extends BasePresenter<BedViewController> {

    private CallObserver observer;

    private BedHelper bedHelper;

    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param model
     */
    public BedPresenter(BedViewController model) {
        super(model);
    }

    @Override
    public void initData() {
        bedHelper = new BedHelper();
    }

    public void getLocalFilterRowBean(String departmentId,int position) {
        if (observer != null) {
            observer.disposable();
            observer = null;
        }
        observer = new CallObserver<List<HospitalBed>>() {

            @Override
            public void onNext(List<HospitalBed> multiItemEntities) {
                viewModel.setAdapterList(multiItemEntities);
            }

            @Override
            public void onError(Throwable e) {
                viewModel.refreshComplete();
                viewModel.dismissProgressDialog();
            }
        };
        if (bedHelper == null)
            bedHelper = new BedHelper();
        bedHelper.getBedListByDepartmentId(departmentId, position)
                .subscribe(observer);
    }

    public void loadDepartmentList(){
        Observable.create(new ObservableOnSubscribe<List<DepartmentModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DepartmentModel>> observableEmitter) throws Exception {
                if (bedHelper == null)
                    bedHelper = new BedHelper();
                observableEmitter.onNext(bedHelper.getDepartmentList());
                observableEmitter.onComplete();
            }
        }).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxNormalSubscriber<List<DepartmentModel>>() {
            @Override
            public void onNext(List<DepartmentModel> list) {
                viewModel.loadedDepartmentList(list);
            }
        });

    }
}
