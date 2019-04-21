package com.comvee.hospitalabbott.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.adapter.BedGridAdapter;
import com.comvee.hospitalabbott.adapter.GroupBedAdapter;
import com.comvee.hospitalabbott.adapter.SpacesItemDecoration;
import com.comvee.hospitalabbott.base.BaseMVPFragment;
import com.comvee.hospitalabbott.bean.DepartmentModel;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.helper.AbbottHelper;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.network.rxbus.IndexRefreshRxModel;
import com.comvee.hospitalabbott.network.rxbus.RxBus;
import com.comvee.hospitalabbott.tool.LogUtils;
import com.comvee.hospitalabbott.ui.record.ManualRecordActivity;
import com.comvee.hospitalabbott.ui.search.SearchActivity;
import com.comvee.hospitalabbott.widget.DropDownPopWindow;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.UIUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.activity.TestScanActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by F011512088 on 2018/1/24.
 */

public class BedFragment extends BaseMVPFragment<BedPresenter, BedViewController> implements BedViewController {

    @BindView(R.id.fragment_top_tv)
    TextView fragmentTopTv;
    /* @BindView(R.id.fragmentBed_spinner_left)
     MaterialSpinner fragmentBedSpinnerLeft;*/
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.no_data_ll)
    LinearLayout noDataView;
    @BindView(R.id.item_department)
    LinearLayout itemDepartment;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.iv_select)
    ImageView ivSelect;
    @BindView(R.id.iv_department)
    ImageView ivDepartment;
    @BindView(R.id.item_select)
    LinearLayout itemSelect;
    @BindView(R.id.wardFragment_rl_top)
    LinearLayout wardFragmentRlTop;
    private List<DepartmentModel> departmentList;

    @OnClick({R.id.fragmentBed_tv_toRecord, R.id.fragmentBed_iv_search, R.id.fragmentBed_tv_reuse_data,R.id.item_department,R.id.item_select})
    void onBedClick(View view) {
        switch (view.getId()) {
            case R.id.fragmentBed_tv_toRecord://手动记录
                getActivity().startActivityForResult(
                        new Intent(getActivity(), ManualRecordActivity.class), 0);
                break;
            case R.id.fragmentBed_iv_search://搜索患者信息
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.fragmentBed_tv_reuse_data:
               // AbbottHelper.putAbbottBlood("", 0, "");
                break;
            case R.id.item_department:
                showPopWindow(0);
                break;
            case R.id.item_select:
                showPopWindow(1);
                break;
        }
    }

    public static BedFragment newInstantiate(Bundle bundle) {
        BedFragment fragment = new BedFragment();
        if (bundle != null)
            fragment.setArguments(bundle);
        return fragment;
    }

    public int posSpinner = 0;
    public int departmentPosition = 0;
    private static final String[] SPINNER_LEFT = {
            "全部", "关注", "已监测", "未监测"
    };
    private String concernStatus = "";

    private List<HospitalBed> mData = new ArrayList<>();
    //  private GroupBedAdapter adapter;
    private BedGridAdapter adapter;
    private DepartmentModel currentDepartmentModel;


    @Override
    protected BedPresenter initPresenter() {
        return new BedPresenter(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_bed;
    }

    @Override
    protected void initView() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                RxBus.getDefault().post(new IndexRefreshRxModel());
            }
        });
        GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.addItemDecoration(new SpacesItemDecoration(UIUtil.dip2px(getContext(),10),0,0,0));
        adapter = new BedGridAdapter(mData,getActivity());
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
        mPresenter.loadDepartmentList();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void doOther() {
        if (UserHelper.isInitBed()) {
            onRefreshBedList(); //获取本地数据
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.d("BedFragment ----- onHiddenChanged  :" + hidden);
        if (!hidden && isLoad()) {
            if (fragmentTopTv != null)
                fragmentTopTv.setText(String.format(getString(R.string.nurse_name), UserHelper.getUserName()));
            RxBus.getDefault().post(new IndexRefreshRxModel());
        }
    }


    @Override
    public void showProgress() {
        ((MainActivity) getActivity()).showProgressDialog();
    }

    public void onRefreshBedList() {
        if (departmentList != null && departmentList.size() > 0){
            mPresenter.getLocalFilterRowBean(departmentList.get(departmentPosition).getDepartmentId(),posSpinner);
        }else {
            mPresenter.loadDepartmentList();
        }
    }

    public void onRefreshError() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh(false);
        }
    }

    @Override
    public void setAdapterList(List<HospitalBed> MultiList) {
        if (MultiList.size() == 0) {
            if (noDataView != null)
                noDataView.setVisibility(View.VISIBLE);
            if (recyclerView != null)
                recyclerView.setVisibility(View.GONE);
        } else {
            mData.clear();
            mData.addAll(MultiList);
            if (adapter != null) {
                adapter.setNewData(mData);
                adapter.expandAll();
            }
            if (noDataView != null)
                noDataView.setVisibility(View.GONE);
            if (recyclerView != null)
                recyclerView.setVisibility(View.VISIBLE);
        }
        if (refreshLayout != null)
            refreshLayout.finishRefresh();
        dismissProgressDialog();
    }

    @Override
    public void refreshComplete() {
        if (refreshLayout != null)
            refreshLayout.finishRefresh();
    }

    @Override
    public void loadedDepartmentList(List<DepartmentModel> list) {
        departmentList = list;
        if (departmentList != null && departmentList.size() > 0){
            if (currentDepartmentModel == null) {
                currentDepartmentModel = departmentList.get(0);
                tvDepartment.setText(currentDepartmentModel.getDepartmentName());
                tvSelect.setText(SPINNER_LEFT[posSpinner]);
            }
            mPresenter.getLocalFilterRowBean(currentDepartmentModel.getDepartmentId(),posSpinner);
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).dismissProgressDialog();
        }
    }

    public void autoRefresh() {
        if (refreshLayout != null)
            refreshLayout.autoRefresh();
    }

    public void showPopWindow(int type){
        LinearLayout rootView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.pop_window_root_layout,null);
        if (type == 0){
            for (int i = 0; i < departmentList.size(); i++) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.pop_window_item_layout,null);
                ((TextView)itemView.findViewById(R.id.tv_name)).setText(departmentList.get(i).getDepartmentName());
                itemView.findViewById(R.id.iv_check).setVisibility(View.GONE);
                if (departmentList.get(i).getDepartmentId().equals(currentDepartmentModel.getDepartmentId())){
                    itemView.findViewById(R.id.iv_check).setVisibility(View.VISIBLE);
                }
                final int currentPosition = i;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        departmentPosition = currentPosition;
                        currentDepartmentModel = departmentList.get(currentPosition);
                        tvDepartment.setText(currentDepartmentModel.getDepartmentName());
                        mPresenter.getLocalFilterRowBean(currentDepartmentModel.getDepartmentId(),posSpinner);
                        if (popWindow !=null){
                            popWindow.dismiss();
                        }

                    }
                });
                rootView.addView(itemView);
            }
            popWindow = new DropDownPopWindow(getActivity(),rootView,wardFragmentRlTop,ivDepartment,SPINNER_LEFT.length);
        }else {
            for (int i = 0; i < SPINNER_LEFT.length; i++) {
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.pop_window_item_layout,null);
                ((TextView)itemView.findViewById(R.id.tv_name)).setText(SPINNER_LEFT[i]);
                itemView.findViewById(R.id.iv_check).setVisibility(View.GONE);
                if (posSpinner == i){
                    itemView.findViewById(R.id.iv_check).setVisibility(View.VISIBLE);
                }
                final int currentPosition = i;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        posSpinner = currentPosition;
                        mPresenter.getLocalFilterRowBean(currentDepartmentModel.getDepartmentId(),posSpinner);
                        tvSelect.setText(SPINNER_LEFT[posSpinner]);
                        if (popWindow !=null){
                            popWindow.dismiss();
                        }
                    }
                });
                rootView.addView(itemView);
            }
            popWindow = new DropDownPopWindow(getActivity(),rootView,wardFragmentRlTop,ivSelect,SPINNER_LEFT.length);
        }
    }
    DropDownPopWindow popWindow;
}
