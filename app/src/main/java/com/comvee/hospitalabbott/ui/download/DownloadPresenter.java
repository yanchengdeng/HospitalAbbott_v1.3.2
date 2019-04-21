package com.comvee.hospitalabbott.ui.download;

import android.os.Handler;
import android.os.Message;

import com.comvee.hospitalabbott.base.BasePresenter;
import com.comvee.hospitalabbott.bean.DownloadModel;
import com.comvee.hospitalabbott.network.download.DownloadUtil;
import com.comvee.hospitalabbott.helper.UserHelper;

import java.io.File;

/**
 * Created by F011512088 on 2018/1/25.
 */
public class DownloadPresenter extends BasePresenter<DownloadViewController> {

    public final static String APK_URL = "APK_URL";
    public final static String APK_VERSION = "APK_VERSION";
    public final static String APK_PROGRESS = "APK_PROGRESS";
    public final static String APK_TOTAL = "APK_TOTAL";
    public final static String APK_ISDOWNLOAD = "APK_ISDOWNLOAD";
    public final static String DOWNLOAD_OVER = "下载完成";

    private int pg;
    /**
     * 在子类的构造函数中，设定参数为model，这时候可以presenter调用接口来实现对界面的操作。
     *
     * @param viewModel
     */
    public DownloadPresenter(DownloadViewController viewModel) {
        super(viewModel);
    }

    @Override
    public void initData() {

    }

    public void downloadAPK(String apkUrl, String fileName, final Handler myHandler) {
        UserHelper.putBoolean(APK_ISDOWNLOAD, true);
        DownloadUtil.get().download(apkUrl, fileName, new DownloadUtil.OnDownloadListener() {

            @Override
            public void onDownloadSuccess(File file) {
                UserHelper.putBoolean(APK_ISDOWNLOAD, false);
//                showToast("下载完成");
                Message message = new Message();
                message.what = 2;
                message.obj = file;
                myHandler.sendMessage(message);
            }

            @Override
            public void onDownloading(int progress, long total) {
//                progressBar.setMax((int) total);
//                progressBar.setProgress(progress);
                if (progress - 5 > pg) {
                    pg = progress;
                    Message message = new Message();
                    message.what = 1;
                    DownloadModel model = new DownloadModel(total, progress);
                    message.obj = model;
                    myHandler.sendMessage(message);
                }
            }

            @Override
            public void onDownloadFailed() {
//                showToast("下载失败");
                UserHelper.putBoolean(APK_ISDOWNLOAD, false);
                Message message = new Message();
                message.what = -1;
                myHandler.sendMessage(message);
            }
        });
    }

}
