package com.comvee.hospitalabbott.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.receiver.ExitAppReceiver;
import com.comvee.hospitalabbott.tool.ActivityMrg;
import com.comvee.hospitalabbott.tool.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public abstract class BaseActivity<T extends BasePresenter, M extends BaseViewController> extends BaseRxActivity {

    public String TAG;  //当前Activity的标记

    protected T mPresenter;     //主持人角色

    protected abstract T initPresenter();    //获取到主持人

    private Unbinder unbinder;
    /*加载框可自己定义*/
    private ProgressDialog pd;

    private ExitAppReceiver mExitAppReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = String.format("%s::%s", getPackageName(), getLocalClassName());

        mPresenter = initPresenter();    //初始化Presenter，提供主持人，拥有主持人后才能提交界面数据给presenter

        setContentView(setLayoutId());
        ActivityMrg.getInstance().addActivity(this);
        unbinder = ButterKnife.bind(this);

        mExitAppReceiver = new ExitAppReceiver(this);
        registerReceiver(mExitAppReceiver,new IntentFilter(XTYApplication.EXIT));

        initView();

        mPresenter.initData();

        initEvent();

        doOther();
    }

    protected void doOther() {

    }

    public Context getContext() {
        return this;
    }

    protected abstract void initEvent();


    protected abstract void initView();

    protected abstract int setLayoutId();

    @Override
    protected void onResume() {
        super.onResume();
        //如果presenter为空的时候，我们需要重新初始化presenter
        if (mPresenter == null) {
            mPresenter = initPresenter();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {   //返回按钮点击事件
        //当Activity中的 进度对话框正在旋转的时候（数据正在加载，网络延迟高，数据难以加载）,关闭 进度对话框 ， 然后可以手动执行重新加载

        super.onBackPressed();
    }

    /**
     * 恢复界面后,我们需要判断我们的presenter是不是存在,不存在则重置presenter
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mPresenter == null)
            mPresenter = initPresenter();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * onDestroy中销毁presenter
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unBind();
        mPresenter = null;
        unbinder.unbind();
        unregisterReceiver(mExitAppReceiver);
        ActivityMrg.getInstance().removeActivity(this);
    }

    /**
     * 初始化加载框
     */
    public void initProgressDialog(boolean cancel) {
        if (pd == null) {
            pd = new ProgressDialog(this);
            pd.setMessage("加载中...");
            pd.setCancelable(cancel);
            if (cancel) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        onCancelProgress();
                    }
                });
            }
        } else {
            pd.setCancelable(cancel);
            if (cancel) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        onCancelProgress();
                    }
                });
            }
        }
       // showProgressDialog();
    }

    /**
     * 显示加载框
     */
    public void showProgressDialog() {
        if (pd == null) return;
        if (!pd.isShowing()) {
            pd.setMessage("加载中...");
            pd.show();
        }
    }

    public void showProgressDialog(String messages) {
        if (pd == null) return;
        if (!pd.isShowing()) {
            pd.setMessage(messages);
            pd.show();
        }
    }

    /**
     * 隐藏
     */
    public void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    public void onCancelProgress() {

    }

    public void showToast(String toast){
        ToastUtil.showToast(this, toast);
    }
}