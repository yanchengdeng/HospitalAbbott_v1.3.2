package com.comvee.hospitalabbott.ui.detection;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.adapter.MemberResultTimeAdapter;
import com.comvee.hospitalabbott.adapter.ResultTimeAdapter;
import com.comvee.hospitalabbott.base.BaseActivity;
import com.comvee.hospitalabbott.bean.BLEBloodModel;
import com.comvee.hospitalabbott.bean.BloodRangeBean;
import com.comvee.hospitalabbott.bean.TimeModel;
import com.comvee.hospitalabbott.tool.NextSubscriber;
import com.comvee.hospitalabbott.tool.RxJavaUtil;
import com.comvee.hospitalabbott.tool.SoftKeyInputHidWidget;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.widget.view.CustomTitleBar;
import com.comvee.hospitalabbott.widget.view.KeyboardLayout;
import com.comvee.hospitalabbott.widget.view.NewCircleProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by F011512088 on 2018/2/27.
 */

public class MemberResultActivity extends BaseActivity<MemberResultPresenter, MemberResultViewController>
        implements MemberResultViewController, ResultTimeAdapter.TimeInterface {

    @BindView(R.id.titleBar)
    CustomTitleBar titleBar;
    @BindView(R.id.blood_cardView_ll)
    RelativeLayout bloodCardViewLl;
    @BindView(R.id.resultFragment_circleProgress)
    NewCircleProgressBar resultFragmentCircleProgress;
    @BindView(R.id.resultFragment_tv_remind)
    TextView resultFragmentTvRemind; //血糖状态
    @BindView(R.id.point_of_time)
    RecyclerView pointOfTime; //时间段
    @BindView(R.id.add_remarks)
    EditText remarksEd; //备注
    @BindView(R.id.reference_value)
    TextView referenceValueTV; //参考值
    @BindView(R.id.new_layout)
    KeyboardLayout newLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.resultFragment_complete)
    Button completeBTN;


    List<String> dayList = new ArrayList();
    private MemberResultTimeAdapter timeAdapter;
    private String oldParamCode;
    private String timeParamCode;
    private String bloodStr = TestResultDataUtil.bloodStateNormal;
    private BloodRangeBean rangeBean;
    //血糖值等级 默认正常=3  高>3 低<3
    private int level = 3;
    private String lowStr;
    private String highStr;


    @Override
    protected MemberResultPresenter initPresenter() {
        MemberResultPresenter presenter = new MemberResultPresenter(this);
        BLEBloodModel bleBloodModel = (BLEBloodModel) getIntent().getSerializableExtra(TestBloodActivity.MEMBER_BEAN);
        if (bleBloodModel == null)
            finishActivity();
//        BLEBloodModel bleBloodModel = new BLEBloodModel("2018-02-27 12:00:00", 4.9f);
//        bleBloodModel.setMemberId("23123123213");
//        bleBloodModel.setTitleStr("01床 xxA");
        presenter.setBleBloodModel(bleBloodModel);
        return presenter;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        //获取控件大小
        bloodCardViewLl.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = bloodCardViewLl.getWidth();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) bloodCardViewLl.getLayoutParams();
                float scale = 448f / 380f;
                layoutParams.width = width;
                layoutParams.height = (int) (width / scale);
                bloodCardViewLl.setLayoutParams(layoutParams);
                return true;
            }
        });
        resultFragmentCircleProgress.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                float scale = 448f / 180f;
                RelativeLayout.LayoutParams progressLayoutParams = (RelativeLayout.LayoutParams) resultFragmentCircleProgress.getLayoutParams();
                DisplayMetrics dm = new DisplayMetrics();
                // 获取屏幕信息
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int width = (int) (dm.widthPixels - (getActivity().getResources().getDimension(R.dimen.top_left) - getActivity().getResources().getDimension(R.dimen.top_right)));
                progressLayoutParams.width = (int) (width / scale);
                progressLayoutParams.height = (int) (width / scale);
                progressLayoutParams.topMargin = (int) (width / (448f / 380f) * (80f / 380f));
                resultFragmentCircleProgress.setLayoutParams(progressLayoutParams);
                return true;
            }
        });


        titleBar.setTitleBarTitleText(mPresenter.getTitlerStr())
                .setTitleRlLeftVisibility(false)
                .setRightVisibility(false)
                .bindActivity(this);

        pointOfTime.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        dayList = TestResultDataUtil.getTimeStr();
        timeAdapter = new MemberResultTimeAdapter(dayList, mPresenter.getValue(), this);
        pointOfTime.setAdapter(timeAdapter);
        //默认进入的时段
        timeParamCode = TestResultDataUtil.getPeriod(mPresenter.getBleBloodModel().getParamCode());
        if (TextUtils.isEmpty(timeParamCode)) {
            timeParamCode = TestResultDataUtil.toTimeSlot(new Date());
        }
        int position = TestResultDataUtil.getPosition(timeParamCode);
        if (position > 0 && position < timeAdapter.getItemCount()) {
            timeAdapter.setSelectedIndex(position);
            timeAdapter.notifyDataSetChanged();
            pointOfTime.scrollToPosition((position > 0 && position < 8) ? position - 1 : position); //默认选择的时段处于中间位置
        }
        mPresenter.getRange().subscribe(new Consumer<BloodRangeBean>() {
            @Override
            public void accept(@NonNull BloodRangeBean bean) throws Exception {
                rangeBean = bean;
                setReference(timeParamCode);
                timeAdapter.setRangeBean(rangeBean);

                //默认保存本地 上传成功删除本地数据 上传失败更新本地数据 TestInfoDao库
                mPresenter.initDataTestInfo(timeAdapter == null ? "" : timeAdapter.getSelectedTime());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                //默认保存本地 上传成功删除本地数据 上传失败更新本地数据
                mPresenter.initDataTestInfo(timeAdapter == null ? "" : timeAdapter.getSelectedTime());
            }
        });

        RxJavaUtil.clicks(completeBTN, new NextSubscriber() {
            @Override
            public void call(Object o) {
                timeParamCode = timeAdapter.getSelectedTime();
                if (TextUtils.isEmpty(timeParamCode)) {
                    showToast("请选择血糖时间点");
                    return;
                }
                mPresenter.completeBlood(timeAdapter.getSelectedTime());
            }
        });

//        addLayoutListener();

    }

    /**
     * 监听键盘状态，布局有变化时，靠scrollView去滚动界面
     */
    public void addLayoutListener() {
        newLayout.setKeyboardListener(new KeyboardLayout.KeyboardLayoutListener() {
            @Override
            public void onKeyboardStateChanged(boolean isActive, int keyboardHeight) {
                Log.e("onKeyboardStateChanged", "isActive:" + isActive + " keyboardHeight:" + keyboardHeight);
                if (isActive) {
                    scrollToBottom();
                }
            }
        });
    }

    /**
     * 弹出软键盘时将SVContainer滑到底
     */
    private void scrollToBottom() {
        newLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, scrollView.getBottom() + SoftKeyInputHidWidget.getStatusBarHeight(MemberResultActivity.this));
            }
        }, 100);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_member_result;
    }

    @Override
    public void showProgress() {
        initProgressDialog(false);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public String getRemarksStr() {
        if (remarksEd == null)
            return "";
        return remarksEd.getText().toString();
    }

    @Override
    public void dismissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void setReference(String paranCode) {
        upDataHighLow(timeParamCode);
        bloodStr = TestResultDataUtil.bloodGlucoseToString(mPresenter.getValue(), Float.valueOf(highStr), Float.valueOf(lowStr));
        if (!bloodStr.equals(TestResultDataUtil.bloodStateNormal)) {
            String str1 = "血糖" + (bloodStr.equals(TestResultDataUtil.bloodStateHigh) ? "偏高" : "偏低") + " , 请注意";
            SpannableString ss = new SpannableString(str1);
            int startPosition = str1.toString().indexOf("偏");
            int endPosition = str1.toString().indexOf(" ,");
            ss.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.text_17), true), startPosition, endPosition,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            resultFragmentTvRemind.setText(ss);
        } else if (bloodStr.equals(TestResultDataUtil.bloodStateNormal)) {
            String str1 = "血糖" + bloodStr + " , 请注意";

            resultFragmentTvRemind.setText(str1);
        }

        referenceValueTV.setText(String.format(getContext().getString(R.string.reference_value),
                lowStr, highStr));
        //正常 0: 高 1: 低 -1
        int type = TestResultDataUtil.getBloodType(mPresenter.getValue(), highStr, lowStr);
        int bgColor = 0;
        switch (type) {
            case -1:
                level = 1;
                bloodCardViewLl.setBackground(getResources().getDrawable(R.drawable.bloodthree_28));
                bgColor = getContext().getResources().getColor(R.color.low_blood);
                break;
            case 0:
                level = 3;
                bloodCardViewLl.setBackground(getResources().getDrawable(R.drawable.bloodthree_33));
                bgColor = getContext().getResources().getColor(R.color.normal_blood);
                break;
            case 1:
                level = 5;
                bloodCardViewLl.setBackground(getResources().getDrawable(R.drawable.bloodthree_31));
                bgColor = getContext().getResources().getColor(R.color.high_blood);
                break;
        }
        mPresenter.setLevel(level);
        if (titleBar != null)
            titleBar.setBackgroundColor(bgColor);
        GradientDrawable myGrad = (GradientDrawable) completeBTN.getBackground();
        myGrad.setColor(bgColor);
        completeBTN.setBackground(myGrad);

        resultFragmentCircleProgress.setProgress(mPresenter.getValue(), type);
    }

    /**
     * 按照时间段进行不同血糖范围的监测
     *
     * @param paramCode
     */
    private void upDataHighLow(String paramCode) {
//        LogUtils.e("rangeBean == null :" + (rangeBean == null));
        if (paramCode.equals(TestResultDataUtil.TIME_RANDOM)) {//随机就是当前时段
            paramCode = TestResultDataUtil.toTimeSlot(new Date());
        }
        if (rangeBean != null) {
            String[] lowAndHigh = TestResultDataUtil.getLowAndHigh(paramCode, rangeBean);
            lowStr = lowAndHigh[0];
            highStr = lowAndHigh[1];
        } else {
            lowStr = "4";
            highStr = "10";
        }
    }

    @Override
    public void onTime(TimeModel timeModel) {
        if (!timeParamCode.equals(timeModel.getTimeParamCode())) {
            timeParamCode = timeModel.getTimeParamCode();
            setReference(timeParamCode);
        }
    }
}
