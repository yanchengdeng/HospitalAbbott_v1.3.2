package com.comvee.hospitalabbott.ui.history;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.adapter.DayAdapter;
import com.comvee.hospitalabbott.base.BaseActivity;
import com.comvee.hospitalabbott.bean.DayBean;
import com.comvee.hospitalabbott.bean.ParamLogListBean;
import com.comvee.hospitalabbott.widget.view.CustomTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 单个时间段的血糖记录列表
 * Created by F011512088 on 2017/4/5.
 */

public class DayHistoryActivity extends BaseActivity<DayHistoryPresenter, DayHistoryViewController>
        implements DayHistoryViewController {

    public static final String RECORD_DAY_DATA = "RECORD_DAY_DATA";


    @BindView(R.id.titleBar)
    CustomTitleBar titleBar;
    @BindView(R.id.dayFragment_paramCode)
    TextView dayFragmentParamCode;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private DayAdapter dayAdapter;
    private List<ParamLogListBean> mData;
    private DayBean dayBean;

    @Override
    protected DayHistoryPresenter initPresenter() {
        if (getIntent() != null) {
            dayBean = (DayBean) getIntent().getParcelableExtra(RECORD_DAY_DATA);
        }
        return new DayHistoryPresenter(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_history_day;
    }

    @Override
    protected void initView() {
        if (dayBean == null)
           finish();
        if (dayBean.getRecordTime() != null)
            titleBar.setTitleBarTitleText(dayBean.getRecordTime());
        titleBar.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dayFragmentParamCode.setText(
                String.format(getResources().getString(R.string.day_paramcode), dayBean.getParamCode()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mData = dayBean.getmData();
        if (mData == null)
            mData = new ArrayList<>();
        //反转集合对象顺序
//        Collections.reverse(mData);
        dayAdapter = new DayAdapter(mData);
        recyclerView.setAdapter(dayAdapter);

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void doOther() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
