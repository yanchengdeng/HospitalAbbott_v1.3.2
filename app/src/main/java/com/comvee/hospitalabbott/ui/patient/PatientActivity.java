package com.comvee.hospitalabbott.ui.patient;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.adapter.PatientAdapter;
import com.comvee.hospitalabbott.base.BaseActivity;
import com.comvee.hospitalabbott.bean.SearchBean;
import com.comvee.hospitalabbott.tool.LogUtils;
import com.comvee.hospitalabbott.tool.NextSubscriber;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.ui.main.MainActivity;
import com.comvee.hospitalabbott.ui.record.ManualRecordActivity;
import com.comvee.hospitalabbott.ui.record.ManualRecordPresenter;
import com.comvee.hospitalabbott.ui.search.SearchActivity;
import com.comvee.hospitalabbott.widget.view.CleanEditTextWithIcon;
import com.comvee.hospitalabbott.widget.view.CustomTitleBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by F011512088 on 2018/1/30.
 */

public class PatientActivity extends BaseActivity<PatientPresenter, PatientViewController>
        implements PatientViewController {

    @BindView(R.id.titleBar)
    CustomTitleBar titleBar;
    @BindView(R.id.et_search)
    CleanEditTextWithIcon etSearch;
    @BindView(R.id.search_loading)
    RelativeLayout relativeLayout;
    @BindView(R.id.loading_iv)
    ImageView loadingIv;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    @OnClick(R.id.tv_search)
    void toSearch() {
        String searchStr = etSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(searchStr)) {
            etSearch.setCursorVisible(false);
            etSearch.removeClearButton();
            //关闭虚拟键盘
            Utils.closeKeyboard(getActivity());
            startAnimation();
            mPresenter.searchStr(searchStr);

        } else {
            if (adapter.getData().size() == 0) {
                mPresenter.getLocalMemberAll();
            } else
                showToast("请输入搜索内容...");
        }
    }

    private PatientAdapter adapter;
    private List<SearchBean> mData = new ArrayList<>();
    private SearchBean searchBean;

    @Override
    protected PatientPresenter initPresenter() {
        return new PatientPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        titleBar.setTitleBarTitleText("选择病人")
                .bindActivity(this)
                .setTitleBarRightBtn("确定", new NextSubscriber() {
                    @Override
                    public void call(Object o) {
//                        LogUtils.e("---确定---");
                        if (searchBean != null) {
                            //退出当前界面
                            Utils.closeKeyboard(getActivity());
                            Intent intent = new Intent();
                            intent.putExtra(ManualRecordPresenter.SEARCH_BEAN, searchBean);
                            setResult(MainActivity.SEARCH_CODE, intent);
                            finish();

                        } else {
                            ToastUtil.showToast(getContext(), "请选择患者!");
                        }

                    }
                });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PatientAdapter(mData);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                adapter.setSelectPosition(position);
                adapter.notifyDataSetChanged();
                searchBean = adapter.getSelectSearchBean();
            }
        });
        recyclerView.setAdapter(adapter);

        ((ImageView) emptyLayout.findViewById(R.id.no_data_iv)).setImageResource(R.drawable.empty_icon);
        ((TextView) emptyLayout.findViewById(R.id.no_data_tv)).setText(getString(R.string.empty_no_data));

        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSearch.getText().toString().length() == 0) {
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });
        startAnimation();
        mPresenter.getLocalMemberAll();

    }

    private void onRefresh() {
        adapter.setNewData(mData);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_patient;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * 开始搜索动画
     */
    public void startAnimation() {
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);

        relativeLayout.setVisibility(View.VISIBLE);
        ObjectAnimator oaAnimator = ObjectAnimator.ofFloat(loadingIv, "rotation", 0, 359);
        oaAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        oaAnimator.setDuration(2000);
        oaAnimator.start();
    }

    /**
     * 结束搜索动画
     */
    public void endAnimation() {
        relativeLayout.setVisibility(View.GONE);
        loadingIv.clearAnimation();
    }

    @Override
    public void setSearchList(List<SearchBean> searchList) {
        if (adapter != null) {
            if (searchList.size() == 0) {
                emptyLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                mData.clear();
                mData.addAll(searchList);
                adapter.setNewData(searchList);
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
            }
        }
        endAnimation();
    }
}
