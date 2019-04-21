package com.comvee.hospitalabbott.ui.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BaseActivity;
import com.comvee.hospitalabbott.bean.LoginModel;
import com.comvee.hospitalabbott.helper.OutHelper;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.network.NetWorkManger;
import com.comvee.hospitalabbott.network.config.ApiConfig;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.tool.WiseFy;
import com.comvee.hospitalabbott.ui.main.MainActivity;
import com.comvee.hospitalabbott.widget.comveealert.ComveeAlertDialog;
import com.comvee.hospitalabbott.widget.view.CleanEditTextWithIcon;
import com.comvee.hospitalabbott.widget.view.ProgressButton;

import butterknife.BindView;

/**
 * Created by F011512088 on 2018/1/24.
 */

public class LoginActivity extends BaseActivity<LoginPresenter, LoginViewController> implements LoginViewController {

    @BindView(R.id.login_iv_log)
    ImageView loginIvLog;
    @BindView(R.id.login_ed_account)
    CleanEditTextWithIcon loginEdAccount;
    @BindView(R.id.login_ed_pwd)
    CleanEditTextWithIcon loginEdPwd;
    @BindView(R.id.login_tv_forget)
    TextView loginTvForget;
    @BindView(R.id.login_ed_ll)
    LinearLayout loginEdLl;
    @BindView(R.id.pb_btn)
    ProgressButton progressButton;
    @BindView(R.id.toWifi_tv)
    TextView toWifiTv;
    @BindView(R.id.deviceCode)
    TextView deviceCode;
    @BindView(R.id.address_tv)
    EditText addressTv;
    @BindView(R.id.rel_content)
    RelativeLayout relContent;
    @BindView(R.id.ll_login_root)
    LinearLayout llLoginRoot;
    private WiseFy mWiseFy;
    private String[] addressList;
    private String[] addressName;
    public static final String ACCOUNT_NAME = "accountName";

    private String accountStr;
    private boolean isMD5PWD = false; //判断密码框中的字符串是否是MD5码

    public static void startActivity(Activity activity) {
        OutHelper.outLogin();
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void initEvent() {
        progressButton.setBgColor(getResources().getColor(R.color.blue_80ccff));
        progressButton.setTextColor(Color.WHITE);
        progressButton.setProColor(Color.WHITE);
        progressButton.setButtonText("登录");
        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isNetwork(LoginActivity.this)) {
                    Utils.closeKeyboard(LoginActivity.this);
                    mPresenter.toLogin(getContext(),
                            loginEdAccount,
                            loginEdPwd,
                            progressButton, isMD5PWD);
                } else {
                    showToast("请检查网络连接...");
                }


            }
        });

        loginTvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (!TextUtils.isEmpty(accountStr))
            loginEdAccount.setText(accountStr);

        Utils.keepLoginBtnNotOver(llLoginRoot, relContent, 180);
    }

    //测试账号是15880050290  123456

    @Override
    protected void initView() {
        loginEdAccount.setText("15880050290");
        loginEdPwd.setText("123456");
//        if (TextUtils.isEmpty(UserHelper.getBaseUrl())){
        //   UserHelper.setBaseUrl(ApiConfig.BASE_URL_OUT);
        //  UserHelper.setBaseUrl(ApiConfig.BASE_URL_TEST);
        //   UserHelper.setBaseUrl(ApiConfig.BASE_URL_ZHI_GE);
        //   UserHelper.setBaseUrl(ApiConfig.BASE_URL_ZUO_HE);

        UserHelper.setBaseUrl(ApiConfig.BASE_URL);
        //    UserHelper.setBaseUrl(ApiConfig.BASE_URL_OUT);
        //UserHelper.setBaseUrl(ApiConfig.TEST_COMV_URL);

        //   ComveeLoader.reset();
        //   UserHelper.setBaseUrl(ApiConfig.TEST_2_4_zh);
        //   UserHelper.setBaseUrl("http://117.158.201.79:8888/");
//        UserHelper.setBaseUrl(ApiConfig.TEST_2_4_y1);
//        }
        mWiseFy = new WiseFy.withContext(XTYApplication.getInstance()).logging(false).getSmarts();
        addressList = new String[]{ApiConfig.BASE_URL, ApiConfig.BASE_URL, ApiConfig.BASE_URL};
        addressName = new String[]{"外网", "内网", "测试地址"};
        if (UserHelper.isLogin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        if (getIntent() != null)
            if (!TextUtils.isEmpty(getIntent().getStringExtra(ACCOUNT_NAME)))
                accountStr = getIntent().getStringExtra(ACCOUNT_NAME);
        deviceCode.setText("设备编号：" + UserHelper.getMachineNo());

        if (!TextUtils.isEmpty(UserHelper.getString(UserHelper.LastUserNo)))
            loginEdAccount.setText(UserHelper.getString(UserHelper.LastUserNo));
        if (!TextUtils.isEmpty(UserHelper.getString(UserHelper.LastUserPasswordMD5))) {
            loginEdPwd.setText(UserHelper.getString(UserHelper.LastUserPasswordMD5));
            isMD5PWD = true;
        }
        loginEdAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    loginEdAccount.setCursorVisible(true);
                    // 此处为得到焦点时的处理内容
                    if (loginEdAccount.getText().toString().length() > 0)
                        loginEdAccount.addClearButton();
                } else {
                    // 此处为失去焦点时的处理内容
                    if (loginEdAccount.getText().toString().length() > 0)
                        loginEdAccount.removeClearButton();
                }
            }
        });


        loginEdPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                LogUtils.e("onFocusChange");
                if (hasFocus) {
                    loginEdPwd.setCursorVisible(true);
                    // 此处为得到焦点时的处理内容
                    if (loginEdPwd.getText().toString().length() > 0)
                        loginEdPwd.addClearButton();
                } else {
                    // 此处为失去焦点时的处理内容
                    if (loginEdPwd.getText().toString().length() > 0)
                        loginEdPwd.removeClearButton();
                }
            }
        });
        loginEdAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (loginEdPwd.getText().toString().length() > 0 && loginEdAccount.getText().toString().length() > 0) {
                    progressButton.setEnabled(true);
                    progressButton.setBgColor(getResources().getColor(R.color.blue));
                } else {
                    progressButton.setEnabled(false);
                    progressButton.setBgColor(getResources().getColor(R.color.blue_80ccff));
                }
            }
        });
        loginEdPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isMD5PWD = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (loginEdPwd.getText().toString().length() > 0 && loginEdAccount.getText().toString().length() > 0) {
                    progressButton.setEnabled(true);
                    progressButton.setBgColor(getResources().getColor(R.color.blue));
                } else {
                    progressButton.setEnabled(false);
                    progressButton.setBgColor(getResources().getColor(R.color.blue_80ccff));
                }
            }
        });
        loginIvLog.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ComveeAlertDialog.Builder groupDialog = new ComveeAlertDialog.Builder(getActivity());
                groupDialog.setCancelable(true);
                Dialog dialog = groupDialog.setSingleChoiceItems(addressName, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserHelper.setBaseUrl(addressList[which]);
                        ComveeLoader.reset();
                        dialog.dismiss();
                    }
                }).create();
                dialog.show();
                return true;
            }
        });

        findViewById(R.id.tv_connect_test).setVisibility(View.GONE);
        findViewById(R.id.tv_connect_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, NetworkTestActivity.class);
                startActivity(intent);
            }
        });

        initProgressDialog(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onLoginSuccess(LoginModel loginModel) {
        UserHelper.setSessionToken(loginModel.getSessionToken());
        UserHelper.setMachineId(loginModel.getMachine() == null ? "" : loginModel.getMachine().getMachineId() + "");
        NetWorkManger.refreshNetWorkManger();
        UserHelper.setIsLogin(true);
        UserHelper.setDoctorInfo(loginModel);

        progressButton.stopAnim(new ProgressButton.OnStopAnim() {
            @Override
            public void Stop() {
                myHand.sendEmptyMessage(0);
            }
        });

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onLoginError(String errorStr) {
        progressButton.stopAnim(new ProgressButton.OnStopAnim() {
            @Override
            public void Stop() {
                myHand.sendEmptyMessage(0);
            }
        });
        /*if (!TextUtils.isEmpty(errorStr) && errorStr.contains("Failed to connect")) {
            mWiseFy.disableWifi();
            LoadingDialog.showDialogForLoading(ActivityMrg.getInstance().getCurrentActivity(), "网络优化中...", false);
            ThreadHandler.postUiThread(new Runnable() {
                @Override
                public void run() {
                    mWiseFy.enableWifi();
                }
            }, 3000);
            ThreadHandler.postUiThread(new Runnable() {
                @Override
                public void run() {
                    LoadingDialog.cancelDialogForLoading();
                    if (!Utils.isNetwork(LoginActivity.this)) {
                        ToastUtil.showToast(LoginActivity.this, "请检查网络连接...");
                    }
                }
            }, 5000);
        }else {

        }*/

        ToastUtil.showToast(LoginActivity.this, errorStr);
    }

    private Handler myHand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            LogUtils.e(" -- handleMessage --");
            if (progressButton != null)
                progressButton.setClickable(true);
        }
    };
}
