package com.comvee.hospitalabbott.ui.search;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.adapter.SearchAdapter;
import com.comvee.hospitalabbott.base.BaseActivity;
import com.comvee.hospitalabbott.bean.SearchBean;
import com.comvee.hospitalabbott.tool.ToastUtil;
import com.comvee.hospitalabbott.helper.UserHelper;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.tool.recycler.DividerItemDecoration;
import com.comvee.hospitalabbott.widget.flowlayout.FlowLayout;
import com.comvee.hospitalabbott.widget.flowlayout.TagAdapter;
import com.comvee.hospitalabbott.widget.flowlayout.TagFlowLayout;
import com.comvee.hospitalabbott.widget.view.CleanEditTextWithIcon;
import com.comvee.hospitalabbott.widget.view.CustomTitleBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by F011512088 on 2018/1/29.
 */

public class SearchActivity extends BaseActivity<SearchPresenter, SearchViewController> implements SearchViewController {

    public static String TAG = "SearchFragment";

    @BindView(R.id.searchFragment_scrollView)
    ScrollView scrollView;
    @BindView(R.id.searchFragment_cleared)
    TextView searchFragmentCleared;
    @BindView(R.id.searchFragment_flowLayout)
    TagFlowLayout historyFlowLayout;
    @BindView(R.id.titleBar)
    CustomTitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_search)
    CleanEditTextWithIcon etSearch;
    @BindView(R.id.search_loading)
    RelativeLayout relativeLayout;
    @BindView(R.id.loading_iv)
    ImageView loadingIv;

    @OnClick(R.id.tv_search)
    void toSearch() {
        String searchStr = etSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(searchStr)) {
            etSearch.setCursorVisible(false);
            etSearch.removeClearButton();
            //关闭虚拟键盘
            Utils.closeKeyboard(SearchActivity.this);

            searchNetWork(searchStr);
            /**
             * 把搜索关键字保存在本地
             */
            Set<String> localHistory = UserHelper.getStringSet(TAG);
            if (localHistory == null)
                localHistory = new HashSet<>();
            localHistory.add(searchStr);
            UserHelper.putStringSet(TAG, localHistory);

        } else {
            ToastUtil.showToast(getContext(), "请输入搜索内容...");
        }

    }

    private SearchAdapter adapter;
    private List<SearchBean> mData = new ArrayList<>();

    private TagAdapter<String> mTagAdapter;
    private String[] historyData = new String[]{};

    private View notDataView;

    @Override
    protected SearchPresenter initPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        titleBar.setTitleBarTitleText(String.format(getString(R.string.nurse_name), UserHelper.getUserName()))
                .bindActivity(this);
        setHistory();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        adapter = new SearchAdapter(mData, this);
        recyclerView.setAdapter(adapter);

        notDataView = LayoutInflater.from(this).inflate(R.layout.empty_view, (ViewGroup) recyclerView.getParent(), false);
//        if (TextUtils.isEmpty(stypeStr)) {
//            ((ImageView) notDataView.findViewById(R.id.no_data_iv)).setImageResource(R.drawable.hospital_118);
//            ((TextView) notDataView.findViewById(R.id.no_data_tv)).setText("没有搜索到相关床位");
//        } else {
        ((ImageView) notDataView.findViewById(R.id.no_data_iv)).setImageResource(R.drawable.empty_icon);
        ((TextView) notDataView.findViewById(R.id.no_data_tv)).setText(getString(R.string.empty_no_data));
//        }
        notDataView.setOnClickListener(new View.OnClickListener() {
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
                if (mData != null && adapter != null) {
                    mData.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * 刷新
     */
    private void onRefresh() {
        adapter.setNewData(mData);
    }


    private void setHistory() {
        /**
         * 从本地获取历史查询关键字
         */
        Set<String> localHistory = UserHelper.getStringSet(TAG);
        if (localHistory != null) {
            historyData = new String[localHistory.size()];
            int i = 0;
            for (String str : localHistory) {
                historyData[i] = str;
                i++;
            }
        }

        if (historyData.length != 0) {
            searchFragmentCleared.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
        }
        searchFragmentCleared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                historyData = new String[]{};
                searchFragmentCleared.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);

                UserHelper.putStringSet(TAG, new HashSet<String>());
            }
        });
        mTagAdapter = new TagAdapter<String>(historyData) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_history_tv,
                        null, false);
                tv.setText(s);
                return tv;
            }
        };
        historyFlowLayout.setAdapter(mTagAdapter);
        historyFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (historyData.length > position) {
                    etSearch.setText(historyData[position]);
                    toSearch();
                }
                return true;
            }
        });
    }

    private void searchNetWork(String searchStr) {
        startAnimation();
        recyclerView.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);

        mData.clear();
        adapter.notifyDataSetChanged();

        if (Utils.isNetwork(getContext())) {
            mPresenter.postSearch(SearchActivity.this, searchStr);
        } else {
            mPresenter.localSearch(SearchActivity.this, searchStr);
        }

    }


    private void stopSearch() {
        endAnimation();
        recyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * 开始搜索动画
     */
    public void startAnimation() {
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
    public void onNetSearchList(List<SearchBean> searchBeen) {
        if (adapter == null) return;
        if (searchBeen == null || searchBeen.size() == 0) {
            adapter.setEmptyView(notDataView);
        } else {
            mData.addAll(searchBeen);
            adapter.notifyDataSetChanged();
        }
        stopSearch();
    }

    @Override
    public void onNetError(String errorStr) {
        if (getContext() == null) return;
        if (adapter == null) return;
        adapter.setEmptyView(notDataView);
        stopSearch();
    }

    @Override
    public void onLocalSearch(List<SearchBean> searchBeen) {
        mData.clear();
//                        if (searchBeen.size() == 0) {
//                            postSearch(searchStr);
//                        } else {
        mData.addAll(searchBeen);
        adapter.notifyDataSetChanged();
        stopSearch();
//                        }
    }

}
