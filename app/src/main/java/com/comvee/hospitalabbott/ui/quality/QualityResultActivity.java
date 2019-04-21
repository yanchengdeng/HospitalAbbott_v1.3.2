package com.comvee.hospitalabbott.ui.quality;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.base.BaseActivity;
import com.comvee.hospitalabbott.bean.BLEBloodModel;
import com.comvee.hospitalabbott.bean.QualityModel;
import com.comvee.hospitalabbott.bean.QualityResultBean;
import com.comvee.hospitalabbott.network.config.HttpCall;
import com.comvee.hospitalabbott.network.loader.ComveeLoader;
import com.comvee.hospitalabbott.tool.NextSubscriber;
import com.comvee.hospitalabbott.tool.RxJavaUtil;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.ui.detection.MemberResultPresenter;
import com.comvee.hospitalabbott.ui.detection.TestBloodActivity;
import com.comvee.hospitalabbott.ui.login.LoginActivity;
import com.comvee.hospitalabbott.widget.view.CircleProgressBar;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by F011512088 on 2018/2/27.
 */

public class QualityResultActivity extends BaseActivity<QualityResultPresenter, QualityResultViewController>
        implements QualityResultViewController {

    @BindView(R.id.qualityResultFragment_circleProgress)
    CircleProgressBar qualityResultFragmentCircleProgress;
    @BindView(R.id.qualityResultFragment_tv_remind)
    TextView qualityResultFragmentTvRemind; //描述测试情况
    @BindView(R.id.qualityResultFragment_tv_remind1)
    TextView tvRemind1; //描述测试情况2
    @BindView(R.id.qualityResultFragment_tv_reference)
    TextView qualityResultFragmentTvReference; //参考值
    @BindView(R.id.qualityResultFragment_btn_submit)
    Button qualityResultFragmentBtnSubmit;

    private boolean isClick = true;

    @Override
    protected QualityResultPresenter initPresenter() {
        QualityResultPresenter presenter = new QualityResultPresenter(this);
        QualityModel qualityModel = (QualityModel) getIntent().getSerializableExtra(TestBloodActivity.QUALITY_BEAN);
        if (qualityModel == null) {
            ToastUtil.showToast(QualityResultActivity.this, "质控数据获取失败!");
            finish();
        }
        presenter.setQualityModel(qualityModel);
        return presenter;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        qualityResultFragmentTvReference.setText(String.format(getContext().getString(R.string.reference_value),
                mPresenter.getQualityLow() + "", mPresenter.getQualityHigh() + ""));

        GradientDrawable myGrad = (GradientDrawable) qualityResultFragmentBtnSubmit.getBackground();
        myGrad.setColor(getResources().getColor(R.color.blue));
        qualityResultFragmentBtnSubmit.setBackground(myGrad);

        String str1;
        final int type = mPresenter.getType();
        if (type == 0) {
            str1 = "血糖正常";
            SpannableString ss = new SpannableString(str1);
            ForegroundColorSpan textColor = new ForegroundColorSpan(getResources().getColor(R.color.green));
            int startPosition = str1.toString().indexOf("糖");
            ss.setSpan(textColor, startPosition + 1, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            qualityResultFragmentTvRemind.setText(ss);
            qualityResultFragmentBtnSubmit.setText("确定");
        } else {
            str1 = "血糖超出质控范围，";
            SpannableString ss = new SpannableString(str1);
            ForegroundColorSpan textColor = new ForegroundColorSpan(getResources().getColor(R.color.red));
            int startPosition = str1.toString().indexOf("糖");
            int endPosition = str1.toString().indexOf("，");
            ss.setSpan(textColor, startPosition + 1, endPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            qualityResultFragmentTvRemind.setText(ss);
            str1 = "仪器被锁定";
            SpannableString ss1 = new SpannableString(str1);
            int startPosition1 = str1.toString().indexOf("被");
            ss1.setSpan(textColor, startPosition1 + 1, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvRemind1.setText(ss1);
            qualityResultFragmentBtnSubmit.setText("联系解锁");
        }

        qualityResultFragmentCircleProgress.setProgress(mPresenter.getValue(), type);

        //添加质控
        RxJavaUtil.clicks(qualityResultFragmentBtnSubmit, new NextSubscriber() {
            @Override
            public void call(Object o) {
                if (isClick) {
                    isClick = false;
                    if (type == 0 && !Utils.isNetwork(QualityResultActivity.this)){//正常没网本地保存
                        mPresenter.setLocalQuality();
                        QualityResultActivity.this.finish();
                    }else {
                        mPresenter.submit()
                                .subscribe(new HttpCall<String>(QualityResultActivity.this) {
                                    @Override
                                    public void onNext(String s) {
                                        isClick = true;
                                        showToast("提交成功");
                                        if (type == 0) {
                                            QualityResultActivity.this.finish();
                                        } else {
                                            QualityResultActivity.this.sendBroadcast(new Intent(XTYApplication.EXIT));
                                            LoginActivity.startActivity(QualityResultActivity.this);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        isClick = true;
                                    }
                                });
                    }


                }
            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_quality_result;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }


}
