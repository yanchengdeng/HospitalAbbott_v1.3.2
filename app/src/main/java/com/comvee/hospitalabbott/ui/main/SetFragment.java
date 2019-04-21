package com.comvee.hospitalabbott.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BaseMVPFragment;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.network.rxbus.BloodUpdateRxModel;
import com.comvee.hospitalabbott.network.rxbus.RxBus;
import com.comvee.hospitalabbott.tool.AppUtils;
import com.comvee.hospitalabbott.tool.NextSubscriber;
import com.comvee.hospitalabbott.tool.RxJavaUtil;
import com.comvee.hospitalabbott.tool.ThreadHandler;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.tool.device.CH34xDriverUtil;
import com.comvee.hospitalabbott.ui.login.LoginActivity;
import com.comvee.hospitalabbott.ui.update.UpDateActivity;
import com.comvee.hospitalabbott.widget.view.AboutItemView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by F011512088 on 2018/1/25.
 */

public class SetFragment extends BaseMVPFragment<SetPresenter, SetViewController>
        implements SetViewController {

    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.positionName_tv)
    TextView positionNameTv;
    @BindView(R.id.department_name_tv)
    TextView departmentNameTv;
    @BindView(R.id.setFragment_statistics)
    AboutItemView setFragmentStatistics;
    @BindView(R.id.setFragment_update_iv)
    ImageView setFragmentUpdateIv;
    @BindView(R.id.setFragment_update)
    AboutItemView updateItemView;
    @BindView(R.id.setFragment_signOut)
    Button setFragmentSignOut;
    private ProgressDialog pd;
    // private WiseFy mWiseFy = new WiseFy.withContext(XTYApplication.getInstance()).logging(false).getSmarts();

    @OnClick({R.id.setFragment_qualityControl, R.id.setFragment_dataSynchronization,
            R.id.setFragment_update})
    void onSetClick(View view) {
        switch (view.getId()) {
            case R.id.setFragment_qualityControl://清除E-8错误
                if (pd == null) {
                    pd = new ProgressDialog(getContext());
                    pd.setCancelable(false);
                }
                pd.setMessage("错误清除中...");
                pd.show();
                if (!XTYApplication.driver.isConnected())
                    XTYApplication.driver.ResumeUsbList();
                XTYApplication.driver.UartInit();
                int baudRate = 115200;
                byte dataBit = 8;
                byte stopBit = 1;
                byte parity = 0;
                byte flowControl = 0;
                XTYApplication.driver.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);

                final byte[] to_send = CH34xDriverUtil.toByteArray("68 00 68 01 00 89 18");
                XTYApplication.driver.WriteData(to_send, to_send.length);
                ThreadHandler.postUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pd != null) {
                            pd.cancel();
                        }
                    }
                }, 5000);

               /* Intent intent = new Intent(getActivity(), QualityControlActivity.class);
                getActivity().startActivity(intent);*/
              /*  mWiseFy.disableWifi();
                ThreadHandler.postUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWiseFy.enableWifi();
                    }
                }, 2000);*/
                break;
            case R.id.setFragment_dataSynchronization://数据同步
                if (AppUtils.isNetworkConnected(getContext())) {
                    RxBus.getDefault().post(new BloodUpdateRxModel()); //通知BloodUpdateService
                   /* try {
                        SubmitLocalUtils.postAllLocalTestInfo(getContext());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                } else
                    ToastUtil.showToast(getContext(), "网络断开，请链接网络！");
                break;
            case R.id.setFragment_update: //数据更新
                getActivity().startActivity(new Intent(getActivity(), UpDateActivity.class));
                break;
        }
    }

    public static SetFragment newInstantiate(Bundle bundle) {
        SetFragment setFragment = new SetFragment();
        if (bundle != null)
            setFragment.setArguments(bundle);
        return setFragment;
    }


    @Override
    protected SetPresenter initPresenter() {
        return new SetPresenter(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_set;
    }

    @Override
    protected void initView() {
        nameTv.setText(UserHelper.getUserName());
        departmentNameTv.setText(UserHelper.getDepartName());
        positionNameTv.setText(UserHelper.getPositionName());
        updateItemView.setRightTV(String.format(getResources().getString(R.string.current_version),
                AppUtils.getVerName(getContext())));

        RxJavaUtil.clicks(setFragmentSignOut, new NextSubscriber() {
            @Override
            public void call(Object o) {
                LoginActivity.startActivity(getActivity());
            }
        });
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void doOther() {

    }

    @Override
    public void showProgress() {

    }

}
