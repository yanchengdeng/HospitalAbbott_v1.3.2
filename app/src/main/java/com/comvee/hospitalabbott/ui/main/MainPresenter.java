package com.comvee.hospitalabbott.ui.main;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.comvee.greendao.gen.MemberModelDao;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.bean.MemberCountBean;
import com.comvee.hospitalabbott.bean.MemberModel;
import com.comvee.hospitalabbott.network.rxbus.IndexUiRxModel;
import com.comvee.hospitalabbott.network.rxbus.RxBus;
import com.comvee.hospitalabbott.receiver.NetWorkStateReceiver;
import com.comvee.hospitalabbott.tool.RxJavaUtil;
import com.vise.baseble.core.DeviceMirror;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by F011512088 on 2018/1/25.
 */
public class MainPresenter extends BasePresenter<MainViewController> {
    private DeviceMirror mDeviceMirror;
    private NetWorkStateReceiver netWorkStateReceiver;
    public Disposable uiDisposable;

    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param viewModel
     */
    public MainPresenter(MainViewController viewModel) {
        super(viewModel);
        uiDisposable = RxBus.getDefault().getObservable(IndexUiRxModel.class).subscribe(new Consumer<IndexUiRxModel>() {
            @Override
            public void accept(IndexUiRxModel indexUiRxModel) throws Exception {
                parseCommand(indexUiRxModel.command,indexUiRxModel.data);
            }
        });
    }

    @Override
    public void initData() {
        if (viewModel != null)
            registerReceiver(viewModel.getActivity());
    }

    @Override
    public void unBind() {
        if (viewModel != null)
            unregisterReceiver(viewModel.getActivity());

    }

    /**
     * 扫描界面返回住院号,匹配患者
     *
     * @param patPatientId
     * @return
     */
    public Observable<HospitalBed> matchPatients(final String patPatientId) {
        return Observable.create(new ObservableOnSubscribe<HospitalBed>() {
            @Override
            public void subscribe(ObservableEmitter<HospitalBed> e) throws Exception {
                //获取到扫描界面发来的登记号，搜索本地住院患者数据，匹配到患者，跳转测血糖界面
                MemberModelDao dao = XTYApplication.getInstance().getDaoSession().getMemberModelDao();
                MemberModel localModel = dao.queryBuilder()
                        .where(MemberModelDao.Properties.PatPatientId.eq(patPatientId))
                        .unique();
                if (localModel == null) {
                    e.onError(new Throwable("未找到改患者,请手动选择!"));
                } else {
                    e.onNext(new HospitalBed(localModel));
                }
                e.onComplete();
            }
        }).compose(RxJavaUtil.<HospitalBed>toComputation());
    }



    public void parseCommand(String command,Object data) {
        switch (command) {
            case "showProgress":
                viewModel.showProgress();
                break;
            case "dismissProgressDialog":
                viewModel.dismissProgressDialog();
                break;
            case "getAllData":
                viewModel.getAllData();
                break;
            case "onRefreshData":
                viewModel.onRefreshData();
                break;
            case "onRefreshError":
                viewModel.onRefreshError();
                break;
            case "setCountView":
                viewModel.setCountView((MemberCountBean) data);
                break;
        }
    }


    public void registerReceiver(Context context) {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(netWorkStateReceiver, filter);
    }

    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(netWorkStateReceiver);
    }
}


