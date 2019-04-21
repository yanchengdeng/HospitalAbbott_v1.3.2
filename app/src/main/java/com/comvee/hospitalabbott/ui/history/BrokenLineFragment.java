package com.comvee.hospitalabbott.ui.history;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.BaseMVPFragment;
import com.comvee.hospitalabbott.bean.MemberHistoryModel;
import com.comvee.hospitalabbott.tool.DateUtil;
import com.comvee.hospitalabbott.widget.view.BrokenLineView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by F011512088 on 2018/1/30.
 */

public class BrokenLineFragment extends BaseMVPFragment<BrokenLinePresenter,BrokenLineViewController>
        implements BrokenLineViewController {

    public static BrokenLineFragment newInstantiate(Bundle bundle) {
        BrokenLineFragment brokenLineFragment = new BrokenLineFragment();
        if (bundle != null)
            brokenLineFragment.setArguments(bundle);
        return brokenLineFragment;
    }
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.brokenLine_time)
    TextView brokenLineTime;
    @BindView(R.id.brokenLineView)
    BrokenLineView brokenLineView;

    @OnClick({R.id.brokenLine_left_icon, R.id.brokenLine_right_icon})
    void toDay(View view) {
        switch (view.getId()) {
            case R.id.brokenLine_left_icon:

                if (position > 0) {
                    position--;
                    brokenLineView.setData(mData.get(position));
                    String time = mData.get(position).getDate();

                    brokenLineTime.setText(time);
                }

                break;
            case R.id.brokenLine_right_icon:

                if (position < (mData.size() - 1)) {
                    position++;
                    brokenLineView.setData(mData.get(position));
                    String time = mData.get(position).getDate();

                    brokenLineTime.setText(time);
                }
                break;
        }
    }

    private List<MemberHistoryModel> mData = new ArrayList<>();
    private int position = 0;


    @Override
    protected BrokenLinePresenter initPresenter() {
        return new BrokenLinePresenter(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_brokenline;
    }

    @Override
    protected void initView() {
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                ((HistoryActivity) getActivity()).refreshData();
            }
        });
        brokenLineTime.setText(DateUtil.getCurDateStr(DateUtil.FORMAT_YMD));
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

    public void update(List<MemberHistoryModel> list) {
        mData.clear();
        mData.addAll(list);
        if (mData.size()>0) {
            MemberHistoryModel bean = mData.get(position);
            String time = bean.getDate();
            if (brokenLineTime != null) {
                brokenLineTime.setText(time);
                brokenLineView.setData(bean);
            }
        }else {
            String time = DateUtil.getCurDateStr(DateUtil.FORMAT_YMD);
            if (brokenLineTime != null) {
                brokenLineTime.setText(time);
                brokenLineView.setData(new MemberHistoryModel(time));
            }
        }
        refreshComplete();
    }

    public void autoRefresh() {
        if (refreshLayout != null)
            refreshLayout.autoRefresh();
    }

    public void refreshComplete() {
        if (refreshLayout != null)
            refreshLayout.finishRefresh();
    }


}
