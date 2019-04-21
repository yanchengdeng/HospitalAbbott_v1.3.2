package com.comvee.hospitalabbott.ui.main;

import android.os.Bundle;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.adapter.TitleAdapter;
import com.comvee.hospitalabbott.base.BaseMVPFragment;
import com.comvee.hospitalabbott.bean.MemberCountBean;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.network.rxbus.IndexRefreshRxModel;
import com.comvee.hospitalabbott.network.rxbus.RxBus;
import com.comvee.hospitalabbott.tool.LogUtils;
import com.comvee.hospitalabbott.widget.magicindicator.FragmentContainerHelper;
import com.comvee.hospitalabbott.widget.magicindicator.MagicIndicator;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.CommonNavigator;

import java.util.List;

import butterknife.BindView;

/**
 * Created by F011512088 on 2018/1/25.
 */

public class TaskIndexFragment extends BaseMVPFragment<TaskIndexPresenter, TaskIndexViewController>
        implements TaskIndexViewController, TitleAdapter.TitleAdapterClickListener {

    public static TaskIndexFragment newInstantiate(Bundle bundle) {
        TaskIndexFragment bedIndexFragment = new TaskIndexFragment();
        if (bundle != null)
            bedIndexFragment.setArguments(bundle);
        return bedIndexFragment;
    }

    @BindView(R.id.fragment_top_tv)
    TextView fragmentTopTv;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;


    private TitleAdapter titleAdapter;
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();

    private TaskParamCodeFragment paramCodeFragment;

    private MemberCountBean smbgCount;

    @Override
    protected TaskIndexPresenter initPresenter() {
        return new TaskIndexPresenter(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void initView() {
        if (fragmentTopTv != null)
            fragmentTopTv.setText(String.format(getString(R.string.nurse_name), UserHelper.getUserName()));

        initMagicIndicator();
        mFragmentContainerHelper.handlePageSelected(1, false);

        paramCodeFragment = (TaskParamCodeFragment) getChildFragmentManager().findFragmentById(R.id.fragment);
        if (paramCodeFragment == null) {
            Bundle bundle = new Bundle();
            bundle.putString(TaskParamCodeFragment.PARAMCODE, mPresenter.getShowParamCode());
            paramCodeFragment = TaskParamCodeFragment.newInstantiate(bundle);
        }

    }

    private void initMagicIndicator() {
        if (smbgCount != null)
            mPresenter.upDataSmbgCount(smbgCount);
        magicIndicator.setBackgroundColor(getResources().getColor(R.color.white));
        titleAdapter = new TitleAdapter(mPresenter.getScreeList(), this);
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setTrisection(true);
        commonNavigator.setAdapter(titleAdapter);
        magicIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator);
    }


    @Override
    protected void initEvent() {

    }

    @Override
    protected void doOther() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && isLoad()) {
            mPresenter.refreshParam();
            RxBus.getDefault().post(new IndexRefreshRxModel());
        }
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void onTitleClick(int index) {
        mFragmentContainerHelper.handlePageSelected(index);
        mPresenter.switchPages(index);
    }

    @Override
    public void setAdapterList(List<MultiItemEntity> multiList) {
        paramCodeFragment.setParamCode(mPresenter.getShowParamCode());
        paramCodeFragment.updateView(multiList);
    }

    @Override
    public void upDataMagicIndicator(int position) {
        if (magicIndicator != null) {
            mFragmentContainerHelper.handlePageSelected(position);
            mPresenter.switchPages(position);
        }
    }

    /**
     * 强制刷新
     */
    @Override
    public void autoRefresh() {
        if (paramCodeFragment != null)
            paramCodeFragment.autoRefresh();
    }

    @Override
    public void onRefreshError() {
        if (paramCodeFragment != null)
            paramCodeFragment.onRefreshError();
    }

    /**
     * 更新未测量数
     *
     * @param memberCountBean
     */
    public void onMemberSmbgCount(MemberCountBean memberCountBean) {
        if (mPresenter == null) {
            smbgCount = memberCountBean;
        } else {
            mPresenter.upDataSmbgCount(memberCountBean);
            if (titleAdapter != null)
                titleAdapter.setData(mPresenter.getScreeList());
        }
    }

    public void onRefreshBedList() {
        mPresenter.getLocalFilterRowBean(mPresenter.getShowParamCode(), 0);
    }


    public void onBedListByPage() {
        autoRefresh();
        onRefreshBedList();
    }

}
