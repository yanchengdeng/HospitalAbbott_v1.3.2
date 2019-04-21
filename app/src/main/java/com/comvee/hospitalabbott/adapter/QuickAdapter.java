package com.comvee.hospitalabbott.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.comvee.hospitalabbott.base.MyBaseViewHolder;

import java.util.List;

/**
 * Created by comv098 on 2017/1/23.
 */

public abstract class QuickAdapter<T> extends BaseQuickAdapter<T, MyBaseViewHolder> {


    public QuickAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    public QuickAdapter(List data) {
        super(data);
    }

    // 当使用自定义的BaseViewHolder时，需要重写此函数以创建ViewHolder
    @Override
    protected MyBaseViewHolder createBaseViewHolder(View view) {
        return new MyBaseViewHolder(view);
    }
}
