package com.comvee.hospitalabbott.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by F011512088 on 2018/1/9.
 */

public abstract class BaseAdapter<T> extends BaseQuickAdapter<T, ComveeViewHolder> {

    public BaseAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

}
