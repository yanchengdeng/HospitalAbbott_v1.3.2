package com.comvee.hospitalabbott.ui.main;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BaseFragmentActivity;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.bean.MemberCountBean;
import com.comvee.hospitalabbott.bean.TestInfo;
import com.comvee.hospitalabbott.network.service.BloodUpdateService;
import com.comvee.hospitalabbott.network.service.IndexLoadRefreshService;
import com.comvee.hospitalabbott.tool.ActivityCommunicationUtil;
import com.comvee.hospitalabbott.tool.LogUtils;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.tool.ThreadHandler;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.ui.detection.TestBloodActivity;
import com.comvee.hospitalabbott.ui.scan.CaptureActivity;
import com.comvee.hospitalabbott.widget.view.BottomView;
import com.xys.libzxing.zxing.activity.TestScanActivity;


import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


public class MainActivity extends BaseFragmentActivity<MainPresenter, MainViewController>
        implements MainViewController, BottomView.BottomOnClick, ActivityCommunicationUtil.BaseCallback<TestInfo> {

    public final static int RECORD_REQUEST_CODE = 1005; //手动记录requestCode
    public final static int SEARCH_CODE = 1006; //手动记录requestCode

    @BindView(R.id.main_bottom)
    BottomView mainBottom;

    private int positionsFragment;
    private BedFragment bedFragment;
    private TaskIndexFragment taskIndexFragment;
    private SetFragment setFragment;
    private Intent updateServiceIntent, indexLoadRefreshService;


    @Override
    protected MainPresenter initPresenter() {
        MainPresenter presenter = new MainPresenter(this);
//        presenter.connectBLE();
        return presenter;
    }

    @Override
    protected void initEvent() {
        updateServiceIntent = new Intent(this, BloodUpdateService.class);
        startService(updateServiceIntent);

        indexLoadRefreshService = new Intent(this, IndexLoadRefreshService.class);
        startService(indexLoadRefreshService);
    }

    @Override
    protected void initView() {

        LogUtils.d("---initView---");
        initProgressDialog(false);//false点击不取消

        bedFragment = BedFragment.newInstantiate(null);
        taskIndexFragment = TaskIndexFragment.newInstantiate(null);
        setFragment = SetFragment.newInstantiate(null);
        positionsFragment = 0;
        addFragment(bedFragment);
        positionsFragment = 1;
        addFragment(taskIndexFragment);
        positionsFragment = 3;
        addFragment(setFragment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(bedFragment).hide(taskIndexFragment)
                .commit();
        showFragment(bedFragment);
        mainBottom.setCountView(0);
        positionsFragment = 0;
        mainBottom.selectIndex(positionsFragment);
        mainBottom.setBottomOnClick(this);

        //血糖值返回接口
        ActivityCommunicationUtil.getSingle().setCallback(MainActivity.class.getName(), this);
        if (UserHelper.isInitBed())
            autoRefresh();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TestScanActivity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            final String patPatientId = bundle.getString("result");
            showProgressDialog("患者匹配中，请稍后...");
            mPresenter.matchPatients(patPatientId)
                    .subscribe(new Consumer<HospitalBed>() {
                        @Override
                        public void accept(@NonNull HospitalBed hospitalBed) throws Exception {
                            dismissProgressDialog();
                            if (hospitalBed != null) {
                                String paramCode = TestResultDataUtil.getParamCode(new Date());
                                TestBloodActivity.startBloodActivity(MainActivity.this, hospitalBed, paramCode);
                            } else {
                                ToastUtil.showToast(MainActivity.this, "患者匹配失败!");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            dismissProgressDialog();
                            ToastUtil.showToast(MainActivity.this, throwable.getMessage());
                        }
                    });
        } else if (resultCode == TestScanActivity.MANUAL) {//手动记录
            ThreadHandler.postUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isTopActivity())
                        setBottomSwitch(0);
                }
            }, 500);
        }
    }

    //判断界面是否在可见
    private boolean isTopActivity() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(this.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return false;
                } else {
                    Log.i("前台", appProcess.processName);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void showProgress() {
//        showProgressDialog();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void setCountView(MemberCountBean response) {
        if (mainBottom != null)
            mainBottom.setCountView(response != null ? response.getTotal() : 0);
        if (taskIndexFragment != null)
            taskIndexFragment.onMemberSmbgCount(response);
    }

    @Override
    public int getPositionsFragment() {
        return positionsFragment;
    }

    @Override
    public void getAllData() {
        switch (positionsFragment) {
            case 0:
                if (bedFragment != null) {
                    bedFragment.onRefreshBedList();
                }
                break;
            case 1:
                if (taskIndexFragment != null) {
                    taskIndexFragment.onBedListByPage();
                }
                break;
        }
    }

    @Override
    public void onRefreshData() {
        switch (positionsFragment) {
            case 0:
                if (bedFragment != null) {
                    bedFragment.onRefreshBedList();
                }
                break;
            case 1:
                if (taskIndexFragment != null) {
                    taskIndexFragment.onRefreshBedList();
                }
                break;
        }
    }

    @Override
    public void onRefreshError() {
        switch (positionsFragment) {
            case 0:
                if (bedFragment != null) {
                    bedFragment.onRefreshError();
                }
                break;
            case 1:
                if (taskIndexFragment != null) {
                    taskIndexFragment.onRefreshError();
                }
                break;
        }
    }

    @Override
    public void onBedClick() {
        setBottomSwitch(0);
    }

    @Override
    public void onWardClick() {
        setBottomSwitch(1);
    }

    @Override
    public void onZxingClick() {
        /*Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
        openCameraIntent.putExtra(CaptureActivity.TITLE_NAME, "护士：" + UserHelper.getUserName());
        startActivityForResult(openCameraIntent, 1);*/
        Intent intent = new Intent(getActivity(), TestScanActivity.class);
        intent.putExtra(TestScanActivity.TITLE_NAME, UserHelper.getUserName());
        startActivityForResult(intent, 0);
    }

    @Override
    public void onSetClick() {
        setBottomSwitch(3);
    }

    private void setBottomSwitch(int position) {
        if (mainBottom == null) return;
        switch (position) {
            case 0:
                showFragment(bedFragment);
                positionsFragment = 0;
                mainBottom.selectIndex(positionsFragment);
                break;
            case 1:
                showFragment(taskIndexFragment);
                positionsFragment = 1;
                mainBottom.selectIndex(positionsFragment);
                break;
            case 3:
                showFragment(setFragment);
                positionsFragment = 3;
                mainBottom.selectIndex(positionsFragment);
                break;
        }

    }

    private void showFragment(Fragment fragment2) {
        Fragment fragment1 = null;
        if (positionsFragment == 0) {
            fragment1 = bedFragment;
        } else if (positionsFragment == 1) {
            fragment1 = taskIndexFragment;
        } else if (positionsFragment == 3) {
            fragment1 = setFragment;
        }
        if (fragment1 == null) return;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(fragment1).show(fragment2);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d("---onStart---");
        //   mPresenter.isRefreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("---onResume---");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("---onPause---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XTYApplication.driver.CloseDevice();
        if (updateServiceIntent != null) {
            stopService(updateServiceIntent);
        }
        if (indexLoadRefreshService != null) {
            stopService(indexLoadRefreshService);
        }
    }

    /**
     * 血糖记录返回值
     *
     * @param model
     */
    @Override
    public void onCall(TestInfo model) {
        if (bedFragment != null) {
            bedFragment.onRefreshBedList();
        }
        if (taskIndexFragment != null) {
            taskIndexFragment.onBedListByPage();
        }
    }

    private void autoRefresh() {
        switch (positionsFragment) {
            case 0:
                if (bedFragment != null) {
                    bedFragment.autoRefresh();
                }
                break;
            case 1:
                if (taskIndexFragment != null) {
                    taskIndexFragment.autoRefresh();
                }
                break;
        }
    }


}
