package com.comvee.hospitalabbott.ui.history;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.adapter.HistoryListAdapter;
import com.comvee.hospitalabbott.base.BaseMVPFragment;
import com.comvee.hospitalabbott.bean.HistoryCountModel;
import com.comvee.hospitalabbott.bean.MemberHistoryModel;
import com.comvee.hospitalabbott.widget.view.CircleProgressBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by F011512088 on 2018/1/30.
 */

public class ListFragment extends BaseMVPFragment<ListPresenter, ListViewController>
        implements ListViewController {

    public static ListFragment newInstantiate(Bundle bundle) {
        ListFragment listFragment = new ListFragment();
        if (bundle != null)
            listFragment.setArguments(bundle);
        return listFragment;
    }

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.listFragment_recyclerView)
    RecyclerView mRecyclerView;


    private List<MemberHistoryModel> mData;
    private HistoryListAdapter historyAdapter;

    @Override
    protected ListPresenter initPresenter() {
        return new ListPresenter(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_history_list;
    }

    @Override
    protected void initView() {
        initRefreshLayout();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mData = new ArrayList<>();
        historyAdapter = new HistoryListAdapter(mData, (HistoryActivity) getActivity());
        View headerView = getHeaderView(null);
        if (headerView != null)
            historyAdapter.addHeaderView(headerView);
        historyAdapter.bindToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(historyAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private CircleProgressBar highProgressBar;
    private CircleProgressBar normalProgressBar;
    private CircleProgressBar lowProgressBar;

    private View getHeaderView(View.OnClickListener listener) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_history_list_header,
                (ViewGroup) mRecyclerView.getParent(), false);

        highProgressBar = (CircleProgressBar) view.findViewById(R.id.circleProgress_high);
        normalProgressBar = (CircleProgressBar) view.findViewById(R.id.circleProgress_normal);
        lowProgressBar = (CircleProgressBar) view.findViewById(R.id.circleProgress_low);
        highProgressBar.setTextValue("0");
        normalProgressBar.setTextValue("0");
        lowProgressBar.setTextValue("0");
        if (listener != null)
            view.setOnClickListener(listener);
        return view;
    }

    public void update(HistoryCountModel countModel) {
        mData.clear();
        mData.addAll(countModel.getHistoryModels());
        boolean isNull = historyAdapter != null;
        if (countModel.getHistoryModels().isEmpty() && isNull) {
            historyAdapter.removeAllHeaderView();
            historyAdapter.setEmptyView(R.layout.adapter_history_emptyview, (ViewGroup) mRecyclerView.getParent());
        } else {
            if (historyAdapter.getHeaderLayoutCount() != 0) {
                historyAdapter.notifyDataSetChanged();
            } else {
                View headerView = getHeaderView(null);
                if (headerView != null)
                    historyAdapter.addHeaderView(headerView);
            }

        }
        if (highProgressBar != null)
            highProgressBar.setTextValue("" + countModel.getHigh());
        if (lowProgressBar != null)
            normalProgressBar.setTextValue("" + countModel.getNormal());
        if (lowProgressBar != null)
            lowProgressBar.setTextValue("" + countModel.getLow());

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
