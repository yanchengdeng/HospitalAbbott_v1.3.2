package com.comvee.hospitalabbott.ui.update;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.BaseActivity;
import com.comvee.hospitalabbott.bean.VersionBean;
import com.comvee.hospitalabbott.tool.AppUtils;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.ui.download.DownloadActivity;
import com.comvee.hospitalabbott.ui.download.DownloadPresenter;
import com.comvee.hospitalabbott.widget.view.CustomTitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by F011512088 on 2018/1/25.
 */

public class UpDateActivity extends BaseActivity<UpDatePresenter, UpDateViewController>
        implements UpDateViewController {

    @BindView(R.id.updateFragment_main_rl)
    RelativeLayout relativeLayout;
    @BindView(R.id.titleBar)
    CustomTitleBar titleBar;
    /**
     * 是否最新版本
     */
    @BindView(R.id.updateFragment_tv_title)
    TextView updateFragmentTvTitle;
    /**
     * 最新版本号
     */
    @BindView(R.id.updateFragment_tv_values)
    TextView updateFragmentTvValues;
    @BindView(R.id.updateFragment_update)
    Button updateBtn;

    /**
     * 下载更新
     */
    @OnClick(R.id.updateFragment_update)
    public void upDateAPK() {

        initProgressDialog(false);
        mPresenter.getMachineVersionModel(UpDateActivity.this);

    }


    @Override
    protected UpDatePresenter initPresenter() {
        return new UpDatePresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        titleBar.setTitleBarTitleText("升级检测")
                .bindActivity(UpDateActivity.this);

        updateFragmentTvValues.setText(String.format(getResources().getString(R.string.current_version),
                AppUtils.getVerName(getContext())));

        GradientDrawable myGrad = (GradientDrawable) updateBtn.getBackground();
        myGrad.setColor(getResources().getColor(R.color.blue));
        updateBtn.setBackground(myGrad);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_update;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void toDownload(VersionBean bean) {
        dismissProgressDialog();
        String updateUrl = bean.getUpdateUrl();
        int versionNum = 0;
        try {
            versionNum = Integer.valueOf(bean.getVersionNum());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, DownloadActivity.class);
        intent.putExtra(DownloadPresenter.APK_VERSION, versionNum);
        intent.putExtra(DownloadPresenter.APK_URL, updateUrl);
        startActivity(intent);
    }

    @Override
    public void latest() {
        dismissProgressDialog();
        ToastUtil.showToast(getContext(), "当前已经是最新版本了");
    }

    @Override
    public void onError() {
        dismissProgressDialog();
    }
}
