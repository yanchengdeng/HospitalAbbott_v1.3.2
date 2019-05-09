package com.comvee.hospitalabbott.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.bean.DateListBean;

import java.util.List;

/**
 * 日期
 */

public class DateListAdapter extends BaseQuickAdapter<DateListBean, BaseViewHolder> {

    LinearLayout.LayoutParams params;

    public DateListAdapter(int layoutResId, @Nullable List<DateListBean> data) {
        super(layoutResId, data);
        params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth() / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void convert(BaseViewHolder helper, DateListBean item) {
        ((TextView) helper.getView(R.id.tv_date)).setText(item.dateDay);

        ((TextView) helper.getView(R.id.tv_week_title)).setText(item.dateWeek);

        helper.getView(R.id.ll_layout).setLayoutParams(params);

        if (item.isSelected) {
            helper.getView(R.id.tv_date).setBackground(mContext.getResources().getDrawable(R.drawable.date_circle));
            ((TextView) helper.getView(R.id.tv_date)).setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            helper.getView(R.id.tv_date).setBackground(mContext.getResources().getDrawable(R.drawable.date_circle_empty));
            ((TextView) helper.getView(R.id.tv_date)).setTextColor(mContext.getResources().getColor(R.color.blue));
        }


    }
}
