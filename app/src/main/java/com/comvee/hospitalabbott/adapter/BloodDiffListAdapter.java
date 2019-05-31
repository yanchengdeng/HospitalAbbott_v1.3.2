package com.comvee.hospitalabbott.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.bean.BloodSugarChatDynmicInfo;

import java.util.List;

/**
 * 血糖平均差
 */

public class BloodDiffListAdapter extends BaseQuickAdapter<BloodSugarChatDynmicInfo.AvgDiff, BaseViewHolder> {


    public BloodDiffListAdapter(int layoutResId, @Nullable List<BloodSugarChatDynmicInfo.AvgDiff> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BloodSugarChatDynmicInfo.AvgDiff item) {

        ((TextView)  helper.getView(R.id.tv_diff_date)).setText(""+item.name);

        ((TextView)  helper.getView(R.id.tv_diff_value)).setText(String.valueOf(item.value));
        if (item.value>0.83){
            ((TextView)  helper.getView(R.id.tv_diff_value)).setTextColor(mContext.getResources().getColor(R.color.red));
            ((TextView) helper.getView(R.id.tv_diff_value)).setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,mContext.getResources().getDrawable(R.drawable.hospital_103),null);
        }else{
            ((TextView) helper.getView(R.id.tv_diff_value)).setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
            ((TextView)  helper.getView(R.id.tv_diff_value)).setTextColor(mContext.getResources().getColor(R.color.blue));

        }





    }
}
