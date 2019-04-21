package com.comvee.hospitalabbott.adapter;

import android.graphics.Color;
import android.text.TextUtils;


import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.MyBaseViewHolder;
import com.comvee.hospitalabbott.bean.ParamLogListBean;

import java.util.List;

/**
 * Created by F011512088 on 2017/4/5.
 */

public class DayAdapter extends QuickAdapter<ParamLogListBean> {

    public DayAdapter(List data) {
        super(R.layout.adapter_day_item, data);
    }

    @Override
    protected void convert(MyBaseViewHolder helper, ParamLogListBean item) {
        float value = Float.valueOf(item.getValue());
        String status = item.getStatus();
        if (TextUtils.isEmpty(status)) {
            status = "";
        }
        helper.setText(R.id.blood_tv, value + "")
                .setVisible(R.id.noBlood, status.equals("2") ? true : false)
                .setVisible(R.id.isblood, status.equals("2") ? false : true)
                .setText(R.id.time_tv_Minute,
                        item.getRecordTime().substring(item.getRecordTime().length() - 8, item.getRecordTime().length() - 3))
                .setVisible(R.id.day_tv_remark, TextUtils.isEmpty(item.getRemark()) ? false : true)
                .setText(R.id.day_tv_remark, "备注: " + item.getRemark());
        boolean isRemark = false;
        if (TextUtils.isEmpty(item.getRemark())) {
            isRemark = true;
        } else {
            isRemark = false;
        }

        int level = Integer.valueOf(item.getLevel());
        if (!status.equals("2")) {
            if (level < 3) { //低
                helper.setBackgroundRes(R.id.blood_values_bg, isRemark ? R.drawable.bloodtwo_36 : R.drawable.bloodtwo_42)
                        .setBackgroundRes(R.id.blood_iv_trend, R.drawable.bloodtwo_23);
            } else if (level > 3) {// 高
                helper.setBackgroundRes(R.id.blood_values_bg, isRemark ? R.drawable.bloodtwo_31 : R.drawable.bloodtwo_38)
                        .setBackgroundRes(R.id.blood_iv_trend, R.drawable.bloodtwo_21);
            } else {// 正常
                helper.setBackgroundRes(R.id.blood_values_bg, isRemark ? R.drawable.bloodtwo_34 : R.drawable.bloodtwo_40)
                        .setBackgroundRes(R.id.blood_iv_trend, 0);
            }
        } else {
            helper.setBackgroundColor(R.id.blood_values_bg, Color.parseColor("#ff6c6c"));
        }
    }
}
