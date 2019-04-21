package com.xys.libzxing.zxing.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xys.libzxing.R;
import com.xys.libzxing.zxing.utils.BeepManager;
import com.xys.libzxing.zxing.utils.DateUtil;

import java.util.Date;

import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class TestScanActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final String TAG = TestScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    public static final String TITLE_NAME = "title";
    public static final int MANUAL = 1999;
    private ZBarView mZBarView;
    private TextView topTitle;
    private TextView newTimeTV;
    private TextView manualControlTV;
    private BeepManager beepManager;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mZBarView = (ZBarView) findViewById(R.id.zbarview);
        mZBarView.setDelegate(this);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        findViewById(R.id.capture_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestScanActivity.this.finish();
            }
        });
        topTitle = (TextView) findViewById(R.id.capture_title);
        Intent intent = getIntent();
        if (intent != null) {
            topTitle.setText(intent.getStringExtra(TITLE_NAME));
        }

        newTimeTV = (TextView) findViewById(R.id.capture_tv_time);
        manualControlTV = (TextView) findViewById(R.id.capture_manual_control);
        String text = manualControlTV.getText().toString().trim();
        if (!TextUtils.isEmpty(text) && text.length()> 6) {
            SpannableString spannableString = new SpannableString(text);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
            spannableString.setSpan(colorSpan, spannableString.length() - 6, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            manualControlTV.setText(spannableString);
        }
        manualControlTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                TestScanActivity.this.setResult(MANUAL, resultIntent);
                TestScanActivity.this.finish();
            }
        });
        beepManager = new BeepManager(this);
    }



    @Override
    protected void onStart() {
        super.onStart();
        mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZBarView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

    //    mZBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
        mZBarView.getScanBoxView().setOnlyDecodeScanBoxArea(false); // 识别整个屏幕中的码
        mZBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        mZBarView.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
        mZBarView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.5秒后开始识别
    }

    @Override
    protected void onResume() {
        super.onResume();
        String ymd = DateUtil.getCurDateStr(DateUtil.FORMAT_YMD);
        String hm = DateUtil.getCurDateStr(DateUtil.FORMAT_HM);
        String week = DateUtil.getWeekStrOfDate(new Date());
        String time = ymd.replace("-", "/") + "\t" + week + "\t" + hm;
        newTimeTV.setText(time);
    }

    @Override
    protected void onStop() {
        mZBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        beepManager.close();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        beepManager.playBeepSoundAndVibrate();
       // Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        //mZBarView.startSpot(); // 延迟0.5秒后开始识别
        Bundle bundle = new Bundle();
        Intent resultIntent = new Intent();
        bundle.putString("result", result);
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        TestScanActivity.this.finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

}