package com.comvee.hospitalabbott.ui.login;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.bean.LoginModel;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.tool.LogUtils;
import com.comvee.hospitalabbott.tool.MD5Util;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.widget.AddressDialog;
import com.comvee.hospitalabbott.widget.view.CleanEditTextWithIcon;
import com.comvee.hospitalabbott.widget.view.ProgressButton;

/**
 * Created by F011512088 on 2018/1/24.
 */
public class LoginPresenter extends BasePresenter<LoginViewController> {

    private boolean isClick = true;
    private AddressDialog addressDialog;

    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param model
     */
    public LoginPresenter(LoginViewController model) {
        super(model);
    }

    @Override
    public void initData() {
        if (addressDialog == null) {
            addressDialog = AddressDialog.newInstantiate();
        }
    }

    public void showAddressDialog(FragmentManager manager) {
        addressDialog.show(manager, "address");
    }

    public void toLogin(Context context, CleanEditTextWithIcon accountEd, CleanEditTextWithIcon pwdEd, ProgressButton button,
                        boolean isMD5PWD) {
        if (!isClick) return;
        isClick = false;
        String account = accountEd.getText().toString().trim();
        String pwd = pwdEd.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtil.showToast(context, "请输入账号");
            isClick = true;
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showToast(context, "请输入密码");
            isClick = true;
            return;
        }

        accountEd.setCursorVisible(false);
        accountEd.removeClearButton();
        pwdEd.setCursorVisible(false);
        pwdEd.removeClearButton();

        //这两个顺序不可变化， 会导致动画结束了设置可点击无效果
        button.setClickable(false);
        button.startAnim();

        if (!UserHelper.getBaseUrl().equals("")) {
            LogUtils.d(UserHelper.getBaseUrl());
            login(account, pwd, isMD5PWD);
        } else {
            ToastUtil.showToast(context, "请输入地址");
        }
    }

    private void login(final String account, String pwd, boolean isMD5PWD) {
        final String pwdStr = isMD5PWD ? pwd : MD5Util.getMD5String(pwd);
        ComveeLoader.getInstance().login(account, pwdStr)
                .subscribe(new HttpCall<LoginModel>(viewModel.getActivity()) {
                    @Override
                    public void onNext(LoginModel loginModel) {
                        if (loginModel.getMachine() == null || loginModel.getMachine().getMachineStatus() == 1) { //解锁状态
                            UserHelper.putString(UserHelper.LastUserNo, account);
                            UserHelper.putString(UserHelper.LastUserPasswordMD5, pwdStr);
                            viewModel.onLoginSuccess(loginModel);
                        } else if (loginModel.getMachine().getMachineStatus() == 2) {//锁住状态
                            viewModel.onLoginError("机子已锁,请联系客服人员进行解锁!");
                        }
                        isClick = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        isClick = true;
                        viewModel.onLoginError(e.getMessage());
                    }
                });
    }
}
