package com.comvee.hospitalabbott.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comvee.hospitalabbott.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by F011512088 on 2018/1/24.
 */

public abstract class BaseMVPFragment<T extends BasePresenter, M extends BaseViewController> extends Fragment {

    public String TAG;  //当前Activity的标记

    protected T mPresenter;     //主持人角色

    protected abstract T initPresenter();    //获取到主持人

    private View rootView;
    private Unbinder unbinder;

    private boolean isLoad = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TAG = String.format("%s::%s", getContext().getPackageName(), getClass().getName());
        if (rootView == null) {
            rootView = inflater.inflate(setLayoutId(), container, false);
            rootView.setBackgroundColor(getResources().getColor(R.color.fragment_background_color));
            unbinder = ButterKnife.bind(this, rootView);
            initView();
            initEvent();
            doOther();
            isLoad = true;
        }
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();    //初始化Presenter，提供主持人，拥有主持人后才能提交界面数据给presenter
        mPresenter.initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        //如果presenter为空的时候，我们需要重新初始化presenter
        if (mPresenter == null) {
            mPresenter = initPresenter();
        }
    }

    /**
     * 恢复界面后,我们需要判断我们的presenter是不是存在,不存在则重置presenter
     *
     * @param savedInstanceState
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mPresenter == null)
            mPresenter = initPresenter();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /**
     * onDestroy中销毁presenter
     */
    @Override
    public void onDestroy() {
        if(mPresenter != null){
            mPresenter.unBind();
            mPresenter = null;
        }
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    public abstract int setLayoutId();

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract void doOther();

    public boolean isLoad() {
        return isLoad;
    }
}
