package com.comvee.hospitalabbott.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.bean.DiffBloodValue;

import java.util.List;

/**
 * 血糖平均差
 */

public class BloodDiffListAdapter extends BaseQuickAdapter<DiffBloodValue, BaseViewHolder> {


    public BloodDiffListAdapter(int layoutResId, @Nullable List<DiffBloodValue> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiffBloodValue item) {

        ((TextView)  helper.getView(R.id.tv_diff_date)).setText(item.getDateString());

        ((TextView)  helper.getView(R.id.tv_diff_value)).setText(String.valueOf(item.getValueData()));
        if (item.getValueData()>0.9){
            ((TextView) helper.getView(R.id.tv_diff_value)).setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,mContext.getResources().getDrawable(R.drawable.hospital_103),null);
        }else{
            ((TextView) helper.getView(R.id.tv_diff_value)).setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
        }





    }
}
