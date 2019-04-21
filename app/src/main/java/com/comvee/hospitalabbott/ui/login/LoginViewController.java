package com.comvee.hospitalabbott.ui.login;

import com.comvee.hospitalabbott.base.BaseViewController;
import com.comvee.hospitalabbott.bean.LoginModel;

/**
 * Created by F011512088 on 2018/1/24.
 */
public interface LoginViewController extends BaseViewController {

    void onLoginSuccess(LoginModel bean);

    void onLoginError(String errorStr);
}
