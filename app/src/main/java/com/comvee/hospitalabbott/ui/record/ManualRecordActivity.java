package com.comvee.hospitalabbott.ui.record;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.adapter.ResultTimeAdapter;
import com.comvee.hospitalabbott.base.BaseActivity;
import com.comvee.hospitalabbott.bean.BloodRangeBean;
import com.comvee.hospitalabbott.bean.SearchBean;
import com.comvee.hospitalabbott.bean.TimeModel;
import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.tool.NextSubscriber;
import com.comvee.hospitalabbott.tool.RxJavaUtil;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.ui.main.MainActivity;
import com.comvee.hospitalabbott.ui.patient.PatientActivity;
import com.comvee.hospitalabbott.widget.calendar.MyCalendarView;
import com.comvee.hospitalabbott.widget.calendar.TimeUtil;
import com.comvee.hospitalabbott.widget.popwindow.basepopwind.BloodPopWin;
import com.comvee.hospitalabbott.widget.popwindow.basepopwind.CalendarPopWin;
import com.comvee.hospitalabbott.widget.popwindow.basepopwind.EasyPopup;
import com.comvee.hospitalabbott.widget.view.CircleProgressBar;
import com.comvee.hospitalabbott.widget.view.RecordProgressBar;
import com.comvee.hospitalabbott.widget.view.StopAutoScrollView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

/**
 * 手动记录
 * Created by F011512088 on 2018/1/29.
 */

public class ManualRecordActivity extends BaseActivity<ManualRecordPresenter, ManualRecordViewController>
        implements ManualRecordViewController, ResultTimeAdapter.TimeInterface, BloodPopWin.OnBloodListener {

    @BindView(R.id.record_layout)
    RelativeLayout layout;
    @BindView(R.id.title_rl_left)
    RelativeLayout titleRlLeft;
    @BindView(R.id.horizontal_recyclerView)
    RecyclerView horizontalRecyclerView;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.recordFragment_circleProgress)
    RecordProgressBar recordFragmentCircleProgress;
    @BindView(R.id.reference_value)
    TextView referenceValue;
    @BindView(R.id.select_layout)
    RelativeLayout selectLayout;
    @BindView(R.id.tv_patient_name)
    TextView tvPatientName;
    @BindView(R.id.remarks_tv)
    EditText remarksEdt;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private ResultTimeAdapter timeAdapter;

    private Drawable arrowDrawable;

    private BloodPopWin bloodPopWin;
    private CalendarPopWin calendarPopWin;

    @Override
    protected ManualRecordPresenter initPresenter() {
        return new ManualRecordPresenter(this);
    }

    @Override
    protected void initEvent() {
        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendarPopWin == null)
                    initCalendarPop();
                showCalendarPop(timeTv);
            }
        });
    }

    @Override
    protected void initView() {
        mPresenter.setSearchBean(getIntent());
        timeTv.setText(DateUtil.getCurDateStr(DateUtil.FORMAT_YMD));

        arrowDrawable = getResources().getDrawable(R.drawable.ms__arrow).mutate();
        timeTv.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDrawable, null);

        horizontalRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        timeAdapter = new ResultTimeAdapter(TestResultDataUtil.getTimeStr(), this);
        horizontalRecyclerView.setAdapter(timeAdapter);

        int position = mPresenter.getParamCodePosition();
        if (position < timeAdapter.getItemCount()) {
            timeAdapter.setSelectedIndex(position);
            timeAdapter.notifyDataSetChanged();
            horizontalRecyclerView.scrollToPosition(position);
        }

        referenceValue.setText(String.format(getContext().getString(R.string.reference_value),
                4.0 + "", 10.0 + ""));

        initCalendarPop();
        initBloodPop();

        //退出当前界面
        RxJavaUtil.clicks(titleRlLeft, new NextSubscriber() {
            @Override
            public void call(Object o) {
                Utils.closeKeyboard(ManualRecordActivity.this);
                finish();
            }
        });

        //选择患者
        RxJavaUtil.clicks(selectLayout, new NextSubscriber() {
            @Override
            public void call(Object o) {
                Utils.closeKeyboard(ManualRecordActivity.this);
                startActivityForResult(new Intent(ManualRecordActivity.this, PatientActivity.class), 0);
            }
        });

        //保存血糖值
        RxJavaUtil.clicks(btnSubmit, new NextSubscriber() {
            @Override
            public void call(Object o) {
                mPresenter.postRecord(remarksEdt.getText().toString(), timeTv.getText().toString());
            }
        });

        RxJavaUtil.clicks(recordFragmentCircleProgress, new NextSubscriber() {
            @Override
            public void call(Object o) {
                Utils.closeKeyboard(ManualRecordActivity.this);
                if (bloodPopWin == null) {
                    initBloodPop();
                }
                showBloodPop();
            }
        });
        remarksEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (remarksEdt.getText().toString().length() == 0) {
                    btnSubmit.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_btn_80ccff));
                }
            }
        });

        if (mPresenter.getSearchBean() != null) {
            selectLayout.setVisibility(View.GONE);
        }

        upDataBean();

        Utils.addLayoutListener(layout, remarksEdt);
    }

    private void upDataBean() {
        if (mPresenter.getSearchBean() != null) {
            String memberId = mPresenter.getSearchBean().getMemberId();
            calendarPopWin.updateData(memberId);

            tvPatientName.setText(mPresenter.getSearchBean().getMemberName());
            btnSubmit.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rectangle_09));
            mPresenter.getRangeBean(memberId);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initBloodPop() {
        bloodPopWin = new BloodPopWin.Builder(ManualRecordActivity.this, this)
                .setSelectInteger(10).setSelectDecimal(0).build();
    }

    @Override
    public void showBloodPop() {
        bloodPopWin.showPopWin(ManualRecordActivity.this);
    }

    @Override
    public void initCalendarPop() {
        calendarPopWin = new CalendarPopWin.Builder(ManualRecordActivity.this, null).build();
        calendarPopWin.setOnChoiceCalendarListener(new MyCalendarView.OnChoiceCalendarListener() {
            @Override
            public void onItemChoice(Calendar calendarSelected, int position, boolean isRecord) {
                timeTv.setText(TimeUtil.fomateTime(calendarSelected.getTimeInMillis(), "yyyy-MM-dd"));
                calendarPopWin.dismiss();
            }

            @Override
            public void onRightClick(Calendar calendar, boolean isRecord) {
                timeTv.setText(TimeUtil.fomateTime(calendar.getTimeInMillis(), "yyyy-MM-dd"));
                calendarPopWin.dismiss();
            }
        });
    }

    @Override
    public void showCalendarPop(View view) {
//        calendarPopWin.showAsDropDown(timeTv);
        calendarPopWin.showPopWin(this);
    }

    /**
     * @param referenceStr
     */
    @Override
    public void upDataReference(String referenceStr) {
        referenceValue.setText(referenceStr);
    }

    /**
     * 更新血糖值
     *
     * @param bloodValues
     * @param type
     */
    @Override
    public void upDataCircleProgress(float bloodValues, int type) {
        recordFragmentCircleProgress.setProgress(bloodValues, type);
    }

    /**
     * 时间段选择回调
     *
     * @param timeModel
     */
    @Override
    public void onTime(TimeModel timeModel) {
        mPresenter.setTimeModel(timeModel);
    }

    @Override
    public void onBloodCompleted(String dateDesc) {
        if (!TextUtils.isEmpty(dateDesc)) {
            mPresenter.setBlood(dateDesc);
        }
    }

    /**
     * 获取选择患者回调数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case MainActivity.SEARCH_CODE:
                mPresenter.setSearchBean(data);
                if (data != null)
                    upDataBean();
                break;
        }
    }
}
