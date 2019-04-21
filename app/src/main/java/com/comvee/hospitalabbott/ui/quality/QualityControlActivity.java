package com.comvee.hospitalabbott.ui.quality;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.BaseActivity;
import com.comvee.hospitalabbott.helper.AbbottHelper;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.widget.popwindow.DataPopWin;
import com.comvee.hospitalabbott.widget.view.CustomTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by F011512088 on 2018/2/24.
 */

public class QualityControlActivity extends BaseActivity<QualityPresenter, QualityViewController>
        implements QualityViewController {

    @BindView(R.id.empty_tv)
    TextView emptyView;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;

    @BindView(R.id.titleBar)
    CustomTitleBar customTitleBar;
    @BindView(R.id.qualityControlFragment_monitorTime_layout)
    LinearLayout monitorTimeLayout; //选择时间
    @BindView(R.id.qualityControlFragment_monitorTime)
    TextView qualityControlFragmentMonitorTime; //时间
    @BindView(R.id.qualityControlFragment_paperNumber)
    TextView paperNumberET; //试纸批号
    @BindView(R.id.qualityControlFragment_deviceCode_layout)
    LinearLayout deviceCodeLayout;
    @BindView(R.id.qualityControlFragment_deviceCode)
    TextView qualityControlFragmentDeviceCode; //设备编号
    @BindView(R.id.qualityControlFragment_QualityNumber)
    TextView qualityNumberET; //质控液编号
    //    @BindView(R.id.qualityControlFragment_range_layout)
//    LinearLayout rangeLayout; //范围
    @BindView(R.id.qualityControlFragment_range)
    TextView qualityControlFragmentRange; //显示质控范围
    @BindView(R.id.qualityControlFragment_btn_submit)
    Button qualityControlFragmentBtnSubmit;

    /**
     * 试纸编号
     */
    @OnClick({R.id.qualityControlFragment_paperNumber_layout,
            R.id.qualityControlFragment_QualityNumber_layout,
            R.id.qualityControlFragment_range_layout})
    void paperNumberPOpWindow(View view) {
        Utils.closeKeyboard(getActivity());
        switch (view.getId()) {
            case R.id.qualityControlFragment_paperNumber_layout: //试纸编号
                mPresenter.initPaperNumberWindow(QualityControlActivity.this, customTitleBar);
                break;
            case R.id.qualityControlFragment_QualityNumber_layout: //质控液编号
                mPresenter.initQualityNumberWindow(QualityControlActivity.this, customTitleBar);
                break;
            case R.id.qualityControlFragment_range_layout: //质控液范围
                String paperNumber = paperNumberET.getText().toString().trim();
                if (!paperNumber.equals("")) {
                    mPresenter.initRangWindow(paperNumber, QualityControlActivity.this, customTitleBar);
                } else {
                    showToast("请先选择试纸批号");
                    return;
                }
                break;
            default:
                setBtnBg();
                break;
        }
    }


    @Override
    protected QualityPresenter initPresenter() {
        return new QualityPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        customTitleBar.setTitleBarTitleText("质控模式")
                .bindActivity(this);
        //获取试纸信息
        mPresenter.netWorkData(QualityControlActivity.this);
        mPresenter.initListener();
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.setPager(1);
                mPresenter.netWorkData(QualityControlActivity.this);
            }
        });

        //设置机器编码
        String machineNo = UserHelper.getMachineNo();
        qualityControlFragmentDeviceCode.setText(machineNo);

        monitorTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showDataPopWin(QualityControlActivity.this, new DataPopWin.OnDataListener() {
                    //日期选择回调接口
                    @Override
                    public void onDataCompleted(String dateDesc) {
                        dateDesc = dateDesc.replace("年", "-");
                        dateDesc = dateDesc.replace("月", "-");
                        dateDesc = dateDesc.replace("日", "");
                        qualityControlFragmentMonitorTime.setText(dateDesc);
                        setBtnBg();
                    }
                }, monitorTimeLayout);
            }
        });

        qualityControlFragmentBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = qualityControlFragmentMonitorTime.getText().toString();
                String qcPagerNo = paperNumberET.getText().toString();
                String qcLiquidNo = qualityNumberET.getText().toString();
                String machineNo = UserHelper.getMachineNo();
                if (TextUtils.isEmpty(machineNo)) {
                    ToastUtil.showToast(getContext(), "用户信息不足， 请重新登录");
                    return;
                }
                if (TextUtils.isEmpty(date)) {
                    ToastUtil.showToast(getContext(), "请选择测试时间");
                    return;
                }
                if (TextUtils.isEmpty(qcPagerNo)) {
                    ToastUtil.showToast(getContext(), "请输入试纸批号");
                    return;
                }
                if (TextUtils.isEmpty(qcLiquidNo)) {
                    ToastUtil.showToast(getContext(), "请输入质控液编号");
                    return;
                }
                if (TextUtils.isEmpty(qualityControlFragmentRange.getText())) {
                    ToastUtil.showToast(getContext(), "请选择质控液范围");
                    return;
                }

                mPresenter.toBlood(QualityControlActivity.this, date, qcPagerNo,qcLiquidNo);
            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_quality_control;
    }

    @Override
    public void showProgress() {
        initProgressDialog(false);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    private void setBtnBg() {
        String date = qualityControlFragmentMonitorTime.getText().toString();
        String qcPagerNo = paperNumberET.getText().toString();
        String qcLiquidNo = qualityNumberET.getText().toString();
        String rangStr = qualityControlFragmentRange.getText().toString();
        if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(qcPagerNo) && !TextUtils.isEmpty(qcLiquidNo)
                && !TextUtils.isEmpty(rangStr)) {
            qualityControlFragmentBtnSubmit.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_btn));
        } else {
            qualityControlFragmentBtnSubmit.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_btn_80ccff));
        }
    }

    @Override
    public void onNetWorkSuccess() {
        dismissProgress();
        contentLayout.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onNetWorkError() {
        contentLayout.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPaperNumber(String paperNumber) {
        paperNumberET.setText(paperNumber);
    }

    @Override
    public void setQualityNumber(String qualityNumber) {
        qualityNumberET.setText(qualityNumber);
    }

    @Override
    public void setQualityControlRange(String rangeStr) {
        qualityControlFragmentRange.setText(rangeStr);
        setBtnBg();
    }

    @Override
    public void dismissProgress() {
        dismissProgressDialog();
    }
}
