package com.comvee.hospitalabbott.ui.update;

import android.os.Bundle;

import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.base.BaseRxActivity;
import com.comvee.hospitalabbott.bean.VersionBean;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.tool.AppUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by F011512088 on 2018/1/25.
 */
public class UpDatePresenter extends BasePresenter<UpDateViewController> {
    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param viewModel
     */
    public UpDatePresenter(UpDateViewController viewModel) {
        super(viewModel);
    }

    @Override
    public void initData() {

    }

    public void getMachineVersionModel(BaseRxActivity activity) {
        ComveeLoader.getInstance().getMachineVersionModel()
                .compose(activity.<VersionBean>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new HttpCall<VersionBean>(viewModel.getActivity()) {
                    @Override
                    public void onNext(VersionBean versionBean) {
                        int versionNum = Integer.valueOf(versionBean.getVersionNum());
                        int oldVersion = AppUtils.APP_VERSION_CODE;
                        if (versionNum > oldVersion) {
                            viewModel.toDownload(versionBean);
                        } else {
                            viewModel.latest();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        viewModel.onError();
                    }
                });
    }
}
