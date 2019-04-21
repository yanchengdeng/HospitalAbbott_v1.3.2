package com.comvee.hospitalabbott.base;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by comv098 on 2017/1/23.
 */

public class MyBaseViewHolder extends BaseViewHolder {

    public MyBaseViewHolder(View view) {
        super(view);
    }

    public MyBaseViewHolder setText(int viewId, CharSequence value, @ColorInt int colors) {
        TextView view = getView(viewId);
        view.setText(value);
        view.setTextColor(colors);
        return this;
    }

    public MyBaseViewHolder setTextAndColor(int viewId, CharSequence value, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setText(value);
        view.setTextColor(textColor);
        return this;
    }

    public MyBaseViewHolder setGone(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public MyBaseViewHolder setBackground(int viewId, Drawable background) {
        View view = getView(viewId);
        view.setBackground(background);
        return this;
    }

}
