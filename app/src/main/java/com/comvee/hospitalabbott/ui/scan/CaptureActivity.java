package com.comvee.hospitalabbott.ui.scan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.BaseFragmentActivity;
import com.xys.libzxing.zxing.utils.DateUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaptureActivity extends BaseFragmentActivity<ScanDevicePresenter, ScanDeviceViewController> implements ScanDeviceViewController {

    public static final String TITLE_NAME = "title";
    public static final int READ_SUCESS = 2000;
    public static final int MANUAL = 1999;
    @BindView(R.id.capture_back)
    ImageView captureBack;
    @BindView(R.id.capture_title)
    TextView captureTitle;
    @BindView(R.id.capture_tv_time)
    TextView captureTvTime;
    @BindView(R.id.capture_manual_control)
    TextView manualControlTV;
    @BindView(R.id.scan_fail)
    TextView scanFail;
    @BindView(R.id.capture_scan_line_fail)
    TextView captureScanLineFail;
    @BindView(R.id.ll_operating_btn)
    LinearLayout llOperatingBtn;
    private CountDownTimer openSucessCountDownTimer;


    @Override
    protected ScanDevicePresenter initPresenter() {
        return new ScanDevicePresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void doOther() {
        super.doOther();

        //血糖仪设备是否连接
        mPresenter.isConnect();

    }

    @Override
    protected void initView() {
        //返回
        captureBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null)
                    mPresenter.closeDevice();
                finish();
            }
        });

        //手动跳转
        captureScanLineFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                CaptureActivity.this.setResult(MANUAL, resultIntent);
                CaptureActivity.this.finish();
            }
        });
        //重新扫描
        llOperatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.requestDataConnect();
                manualControlTV.setVisibility(View.VISIBLE);
                scanFail.setVisibility(View.GONE);
                captureScanLineFail.setVisibility(View.GONE);
                llOperatingBtn.setVisibility(View.GONE);
            }
        });

        //标题
        Intent intent = getIntent();
        if (intent != null) {
            captureTitle.setText(intent.getStringExtra(TITLE_NAME));
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //时间显示
        onDisplayTime();

    }

    public void onDisplayTime() {

        String ymd = DateUtil.getCurDateStr(DateUtil.FORMAT_YMD);
        String hm = DateUtil.getCurDateStr(DateUtil.FORMAT_HM);
        String week = DateUtil.getWeekStrOfDate(new Date());
        String time = ymd.replace("-", "/") + "\t" + week + "\t" + hm;
        captureTvTime.setText(time);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_capture;
    }

    @Override
    public void onUsbAttached() {
        //设备连接成功，打开设备
        mPresenter.openBloodDevice();
    }

    @Override
    public void onUsbDetached() {
        showToast("设备未连接,请退出重试!");

    }

    @Override
    public void onOpenFail() {

        showToast("设备打开失败,请退出重试!");
    }

    @Override
    public void onOpenSucess() {
        mPresenter.requestDataConnect();
    }

    @Override
    public void onReadSucess(String asciiString, String recv) {
        if (openSucessCountDownTimer != null)
            openSucessCountDownTimer.cancel();
        if (mPresenter != null)
            mPresenter.closeDevice();
        final Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("result", asciiString);
        resultIntent.putExtras(bundle);
        setResult(READ_SUCESS, resultIntent);
        finish();

    }

    @Override
    public void onReadFail() {
        if (mPresenter != null)
            mPresenter.closeDevice();
        showToast("打开摄像头失败,请退出重试!");
    }

    @Override
    public void onReadDoing() {


    }

    @Override
    public void onOpenCameraSucess() {


        openSucessCountDownTimer = new CountDownTimer(5200, 5200) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                if (openSucessCountDownTimer != null)
                    openSucessCountDownTimer.cancel();
                if (manualControlTV != null) {
                    manualControlTV.setVisibility(View.GONE);
                    scanFail.setVisibility(View.VISIBLE);
                    captureScanLineFail.setVisibility(View.VISIBLE);
                    llOperatingBtn.setVisibility(View.VISIBLE);
                }
            }
        }.start();

    }

    @Override
    public void showProgress() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }


}
