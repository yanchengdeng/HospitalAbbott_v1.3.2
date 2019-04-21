package com.comvee.hospitalabbott.ui.history;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.BaseFragmentActivity;
import com.comvee.hospitalabbott.bean.HistoryCountModel;
import com.comvee.hospitalabbott.tool.ThreadHandler;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by F011512088 on 2018/1/30.
 */

public class HistoryActivity extends BaseFragmentActivity<HistoryPresenter, HistoryViewController>
        implements HistoryViewController {

    @BindView(R.id.top_title)
    TextView topTitle;
    @BindView(R.id.top_history)
    TextView topHistory;

    @OnClick({R.id.historyFragment_rd_list, R.id.historyFragment_rd_curve})
    public void replaceFragment(View view) {
        switch (view.getId()) {
            case R.id.historyFragment_rd_list:
                switchContent(listFragment);
                positionsFragment = 0;
                break;
            case R.id.historyFragment_rd_curve:
                switchContent(brokenLineFragment);
                positionsFragment = 1;
                break;
        }
    }

    @OnClick(R.id.top_back)
    void backActivity() {
        finish();
    }

    private ListFragment listFragment;
    private BrokenLineFragment brokenLineFragment;

    private int positionsFragment = 0;
    public static boolean isRefresh = true;

    @Override
    protected HistoryPresenter initPresenter() {
        return new HistoryPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        topHistory.setVisibility(View.GONE);

        mPresenter.setHospitalBed(getIntent());
        topTitle.setText(mPresenter.getTitle());

        listFragment = ListFragment.newInstantiate(null);
        brokenLineFragment = BrokenLineFragment.newInstantiate(null);

        addFragment(brokenLineFragment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(brokenLineFragment).commit();
        addFragment(listFragment);

//        String memberId = mPresenter.getMemberId();
//        if (!mPresenter.isOneLoad() && !TextUtils.isEmpty(memberId)) {
//            mPresenter.getAllListData(memberId);
//        }else {
//            showToast("患者信息错误,memberId不能为空!");
//            finish();
//        }

        ThreadHandler.postUiThread(new Runnable() {
            @Override
            public void run() {
                //自动刷新
                listFragment.autoRefresh();
            }
        }, 300);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public void refreshData() {
        if (isRefresh) {
            isRefresh = false;
            //判断是否为第一次获取血糖记录界面, 第一次请求全部接口， 之后都是请求刷新接口
            if (mPresenter.isOneLoad()) {
               // mPresenter.getAllData();
            } else {
               // mPresenter.refreshData();
            }
            mPresenter.getAllData();
        } else {
            refreshComplete();
        }
    }


    @Override
    public void setHistoryList(HistoryCountModel historyModel) {
        isRefresh = true;
//        refreshComplete();
        if (listFragment != null) {
            listFragment.update(historyModel);
        }
        if (brokenLineFragment != null)
            brokenLineFragment.update(historyModel.getHistoryModels());
    }

    /**
     * 停止刷新
     */
    private void refreshComplete() {
        if (listFragment != null)
            listFragment.refreshComplete();
        if (brokenLineFragment != null)
            brokenLineFragment.refreshComplete();
    }

    public void switchContent(Fragment to) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment from = listFragment;
        if (positionsFragment == 0) {
            from = listFragment;
        } else if (positionsFragment == 1) {
            from = brokenLineFragment;
        }
        if (!to.isAdded()) {    // 先判断是否被add过
            transaction.hide(from).add(R.id.historyFragment_content,
                    to, to.getClass().getName()).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
        }
    }

    private void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.historyFragment_content, fragment, fragment.getClass().getName());
//        transaction.commit(); //界面不存在退出 IllegalStateException: Can not perform this action after onSaveInstanceState
        transaction.commitAllowingStateLoss();
    }
}
