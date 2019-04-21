package com.comvee.hospitalabbott.ui.download;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BaseActivity;
import com.comvee.hospitalabbott.bean.DownloadModel;
import com.comvee.hospitalabbott.tool.AppUtils;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.widget.view.CustomTitleBar;

import java.io.File;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by F011512088 on 2018/1/25.
 */

public class DownloadActivity extends BaseActivity<DownloadPresenter, DownloadViewController>
        implements DownloadViewController {

    @BindView(R.id.titleBar)
    CustomTitleBar titleBar;

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.number_progress_bar)
    ProgressBar progressBar;

    @OnClick(R.id.download_update)
    void initApk() {
        if (tvMsg.getText().equals(DownloadPresenter.DOWNLOAD_OVER)) {
            File response = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
            AppUtils.installApk(XTYApplication.getInstance(), response);
        }
    }

    public int newVersionNum = 0;
    private String apkUrl = "";
    public final static String fileName = "sugar_manage_plat.apk";

    @Override
    protected DownloadPresenter initPresenter() {
        return new DownloadPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        if (getIntent() != null) {
            newVersionNum = getIntent().getIntExtra(DownloadPresenter.APK_VERSION, 0);
            apkUrl = getIntent().getStringExtra(DownloadPresenter.APK_URL);
        }
        titleBar.bindActivity(this);
        titleBar.setTitleBarTitleText("下载");

        if (newVersionNum == UserHelper.getInt(DownloadPresenter.APK_VERSION)) {
            progressBar.setMax(100);
            progressBar.setProgress(100);
            tvMsg.setText(DownloadPresenter.DOWNLOAD_OVER);
            title_tv.setText("血糖仪-下载完成");
        } else {
            progressBar.setMax(UserHelper.getInt(DownloadPresenter.APK_TOTAL, 100));
            progressBar.setProgress(UserHelper.getInt(DownloadPresenter.APK_PROGRESS, 0));
            if (UserHelper.getInt(DownloadPresenter.APK_PROGRESS, 0) == 0) {
                UserHelper.putInt(DownloadPresenter.APK_PROGRESS, 0);
            }
            if (UserHelper.getInt(DownloadPresenter.APK_TOTAL, 0) == 0) {
                UserHelper.putInt(DownloadPresenter.APK_TOTAL, 100);
            }

            if (!UserHelper.getBoolean(DownloadPresenter.APK_ISDOWNLOAD)) {
                mPresenter.downloadAPK(apkUrl, fileName, myHandler);
            }
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
//            LogUtils.e("--handleMessage--");
            switch (msg.what) {
                case -1:
                    UserHelper.putInt(DownloadPresenter.APK_PROGRESS, 0);
                    if (progressBar != null) {
                        progressBar.setProgress(0);
                    }
                    if (tvMsg != null) {
                        tvMsg.setText("下载失败");
                        title_tv.setText("血糖仪-下载失败");
                    }
                    break;
                case 1:
                    DownloadModel model = (DownloadModel) msg.obj;
                    long total = model.getTotal();
                    int progress = model.getProgress();
//                    LogUtils.e("正在下载-----progress =" + progress + ", total =" + total);
                    if (tvMsg != null) {
                        float progressM = (float) (((float) total * progress) / (1024 * 1024 * 100));
                        float totalM = (float) total / (1024 * 1024);
                        tvMsg.setText(decimalFormat.format(progressM) + "M/" + decimalFormat.format(totalM) + "M");
                    }
                    if (progressBar != null) {
                        UserHelper.putInt(DownloadPresenter.APK_PROGRESS, progress);
                        progressBar.setProgress(progress);
                    }
                    break;
                case 2:
//                        showToast("下载完成");
                    if (progressBar != null) {
                        progressBar.setProgress(100);
                    }
                    if (tvMsg != null) {
                        tvMsg.setText(DownloadPresenter.DOWNLOAD_OVER);
                        title_tv.setText("血糖仪-下载完成");
                    }
                    UserHelper.putInt(DownloadPresenter.APK_VERSION, newVersionNum);
                    File file = (File) msg.obj;

                    AppUtils.installApk(XTYApplication.getInstance(), file);

                    break;
            }

        }
    };
}
