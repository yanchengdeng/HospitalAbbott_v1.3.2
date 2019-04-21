package com.comvee.hospitalabbott.ui.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.adapter.GroupBedAdapter;
import com.comvee.hospitalabbott.base.BaseMVPFragment;
import com.comvee.hospitalabbott.network.rxbus.IndexRefreshRxModel;
import com.comvee.hospitalabbott.network.rxbus.RxBus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by F011512088 on 2018/1/25.
 */

public class TaskParamCodeFragment extends BaseMVPFragment<TaskParamPresenter, TaskParamViewController>
        implements TaskParamViewController {

    public static String PARAMCODE = "PARAMCODE";

    public static TaskParamCodeFragment newInstantiate(Bundle bundle) {
        TaskParamCodeFragment fragment = new TaskParamCodeFragment();
        if (bundle != null)
            fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.no_data_ll)
    LinearLayout noDataLl;

    private GroupBedAdapter adapter;
    private List<MultiItemEntity> mData = new ArrayList<>();


    private String paramCode = ""; //服务器时间标识

    @Override
    protected TaskParamPresenter initPresenter() {
        return new TaskParamPresenter(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_task_paramcode;
    }

    @Override
    protected void initView() {
        if (getArguments() != null)
            paramCode = getArguments().getString(PARAMCODE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupBedAdapter(getActivity(), mData, paramCode);
        adapter.expandAll();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                refreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        initSmartRefreshLayout();
    }

    private void initSmartRefreshLayout() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
//                refreshLayout.finishRefresh(false);
              //  TaskIndexFragment taskIndexFragment = (TaskIndexFragment) getParentFragment();
              //  ((MainActivity) taskIndexFragment.getActivity()).getPresenter().isRefreshData();
                RxBus.getDefault().post(new IndexRefreshRxModel());
            }
        });
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

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
        if (adapter != null)
            adapter.setParamCode(paramCode);
    }

    public void updateView(List<MultiItemEntity> multiItemEntityList) {
        if (multiItemEntityList == null) {
            multiItemEntityList = new ArrayList<>();
        }
        mData.clear();
        if (refreshLayout != null)
            refreshLayout.finishRefresh();
        if (adapter != null && !multiItemEntityList.isEmpty()) {
            mData.addAll(multiItemEntityList);
            adapter.setParamCode(paramCode);
            adapter.setNewData(mData);
            adapter.expandAll();
        }
        isEmptyView(mData);
        adapter.notifyDataSetChanged();
    }

    private void isEmptyView(List<MultiItemEntity> rowsBeanList) {
        if (rowsBeanList.size() == 0) {
            showEmptyView();
        } else {
            closeEmptyView();
        }
    }

    public void showEmptyView() {
        if (noDataLl != null)
            noDataLl.setVisibility(View.VISIBLE);
        if (recyclerView != null)
            recyclerView.setVisibility(View.GONE);
    }

    public void closeEmptyView() {
        if (noDataLl != null)
            noDataLl.setVisibility(View.GONE);
        if (recyclerView != null)
            recyclerView.setVisibility(View.VISIBLE);
    }

    //强制刷新
    public void autoRefresh() {
        if (refreshLayout != null)
            refreshLayout.autoRefresh();
    }

    public void onRefreshError() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh(false);
        }
    }
}
