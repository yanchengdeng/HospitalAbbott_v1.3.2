package com.comvee.hospitalabbott.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.MyBaseViewHolder;
import com.comvee.hospitalabbott.bean.BloodRangeBean;
import com.comvee.hospitalabbott.bean.TimeModel;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by comv098 on 2017/3/7.
 */

public class MemberResultTimeAdapter extends QuickAdapter<String> {

    private int selectedIndex = -1;
    private BloodRangeBean rangeBean = null;
    private float bloodConcentration;
    private ResultTimeAdapter.TimeInterface timeInterface;

    public MemberResultTimeAdapter(List data, float bloodConcentration, ResultTimeAdapter.TimeInterface timeInterface) {
        super(R.layout.adapter_member_result_time, data);
        this.bloodConcentration = bloodConcentration;
        this.timeInterface = timeInterface;
    }

    @Override
    protected void convert(MyBaseViewHolder helper, String item) {
        final int position = helper.getLayoutPosition() - getHeaderLayoutCount();
        helper.setText(R.id.time_tv, item);
        helper.getView(R.id.time_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = position;
                notifyDataSetChanged();
            }
        });
        if (position == 0) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
                    helper.getView(R.id.time_tv).getLayoutParams();
            lp.setMargins(lp.rightMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin);
            helper.getView(R.id.time_tv).setLayoutParams(lp);
        }

        if (selectedIndex == position) {
            helper.setBackgroundRes(R.id.time_tv, R.drawable.rounded_rectangle_blue)
                    .setTextColor(R.id.time_tv, mContext.getResources().getColor(R.color.white));
            GradientDrawable myGrad = (GradientDrawable) helper.getView(R.id.time_tv).getBackground();
            int bgColor = Color.parseColor("#02c968");
            String[] blood = new String[2];
            if (rangeBean != null) {
                blood = getLowAndHigh(selectedIndex, rangeBean);
            } else {
                blood[0] = "4";
                blood[1] = "10";
            }
            int type = TestResultDataUtil.getBloodType(bloodConcentration, blood[1], blood[0]);
            switch (type) {
                case -1:
                    bgColor = mContext.getResources().getColor(R.color.low_blood);
                    break;
                case 0:
                    bgColor = mContext.getResources().getColor(R.color.normal_blood);
                    break;
                case 1:
                    bgColor = mContext.getResources().getColor(R.color.high_blood);
                    break;
            }
            myGrad.setColor(bgColor);
            helper.getView(R.id.time_tv).setBackground(myGrad);
        } else
            helper.setBackgroundRes(R.id.time_tv, R.drawable.rounded_rectangle_no)
                    .setTextColor(R.id.time_tv, mContext.getResources().getColor(R.color.text_gray_66));

        if (selectedIndex == position) {
            TimeModel timeModel = new TimeModel(mData.get(selectedIndex), selectedIndex);
            if (timeInterface != null) {
                timeInterface.onTime(timeModel);
            }
        }

    }

    public String getSelectedTime() {
        if (selectedIndex == -1) return "";
        return mData.get(selectedIndex >= mData.size() ? mData.size() - 1 : selectedIndex);
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public String[] getLowAndHigh(int position, BloodRangeBean rangeBean) {
        String[] lowAndHigh = new String[2];
        if (position == 0) { //凌晨
//            lowAndHigh[0] = rangeBean.getLowBeforedawn();
//            lowAndHigh[1] = rangeBean.getHighBeforedawn();
            lowAndHigh = getRange(TestResultDataUtil.TIME_0AM);
        } else if (position == 1) { //3AM
            lowAndHigh = getRange(TestResultDataUtil.TIME_3AM);
        } else if (position == 2) { //空腹
//            lowAndHigh[0] = rangeBean.getLowBeforeBreakfast();
//            lowAndHigh[1] = rangeBean.getHighBeforeBreakfast();
            lowAndHigh = getRange(TestResultDataUtil.TIME_FAST);
        } else if (position == 3) { //早餐后
//            lowAndHigh[0] = rangeBean.getLowAfterMeal();
//            lowAndHigh[1] = rangeBean.getHighAfterMeal();
            lowAndHigh = getRange(TestResultDataUtil.TIME_AFTER_BREAKFAST);
        } else if (position == 4) { //午餐前
//            lowAndHigh[0] = rangeBean.getLowBeforeMeal();
//            lowAndHigh[1] = rangeBean.getHighBeforeMeal();
            lowAndHigh = getRange(TestResultDataUtil.TIME_BEFORE_LUNCH);
        } else if (position == 5) { //午餐后
//            lowAndHigh[0] = rangeBean.getLowAfterMeal();
//            lowAndHigh[1] = rangeBean.getHighAfterMeal();
            lowAndHigh = getRange(TestResultDataUtil.TIME_AFTER_LUNCH);
        } else if (position == 6) { //晚餐前
//            lowAndHigh[0] = rangeBean.getLowBeforeMeal();
//            lowAndHigh[1] = rangeBean.getHighBeforeMeal();
            lowAndHigh = getRange(TestResultDataUtil.TIME_BEFORE_DINNER);
        } else if (position == 7) {//晚餐后
//            lowAndHigh[0] = rangeBean.getLowAfterMeal();
//            lowAndHigh[1] = rangeBean.getHighAfterMeal();
            lowAndHigh = getRange(TestResultDataUtil.TIME_AFTER_DINNER);
        } else if (position == 8) { //睡觉前
//            lowAndHigh[0] = rangeBean.getLowBeforeSleep();
//            lowAndHigh[1] = rangeBean.getHighBeforeSleep();
            lowAndHigh = getRange(TestResultDataUtil.TIME_BEFORE_BED);
        } else if (position == 9) {//随机就是当前时间段
            String paramCode = TestResultDataUtil.toTimeSlot(new Date());
            lowAndHigh = getRange(paramCode);
        }
        if (TextUtils.isEmpty(lowAndHigh[0])) {
            lowAndHigh[0] = TestResultDataUtil.lowValues + "";
        }
        if (TextUtils.isEmpty(lowAndHigh[1])) {
            lowAndHigh[1] = TestResultDataUtil.highValues + "";
        }
        return lowAndHigh;
    }

    public String[] getRange(String paramCode) {
        String[] lowAndHigh = new String[2];
        if (paramCode.equals(TestResultDataUtil.TIME_0AM)) {//凌晨
            lowAndHigh[0] = rangeBean.getLowBeforedawn();
            lowAndHigh[1] = rangeBean.getHighBeforedawn();
        } else if (paramCode.equals(TestResultDataUtil.TIME_3AM)) {//3AM
            lowAndHigh[0] = rangeBean.getLowBeforedawn();
            lowAndHigh[1] = rangeBean.getHighBeforedawn();
        } else if (paramCode.equals(TestResultDataUtil.TIME_FAST)) {//空腹
            lowAndHigh[0] = rangeBean.getLowBeforeBreakfast();
            lowAndHigh[1] = rangeBean.getHighBeforeBreakfast();
        } else if (paramCode.equals(TestResultDataUtil.TIME_AFTER_BREAKFAST)) {//早餐后
            lowAndHigh[0] = rangeBean.getLowAfterMeal();
            lowAndHigh[1] = rangeBean.getHighAfterMeal();
        } else if (paramCode.equals(TestResultDataUtil.TIME_BEFORE_LUNCH)) {//午餐前
            lowAndHigh[0] = rangeBean.getLowBeforeMeal();
            lowAndHigh[1] = rangeBean.getHighBeforeMeal();
        } else if (paramCode.equals(TestResultDataUtil.TIME_AFTER_LUNCH)) {//午餐后
            lowAndHigh[0] = rangeBean.getLowAfterMeal();
            lowAndHigh[1] = rangeBean.getHighAfterMeal();
        } else if (paramCode.equals(TestResultDataUtil.TIME_BEFORE_DINNER)) {//晚餐前
            lowAndHigh[0] = rangeBean.getLowBeforeMeal();
            lowAndHigh[1] = rangeBean.getHighBeforeMeal();
        } else if (paramCode.equals(TestResultDataUtil.TIME_AFTER_DINNER)) {//晚餐后
            lowAndHigh[0] = rangeBean.getLowAfterMeal();
            lowAndHigh[1] = rangeBean.getHighAfterMeal();
        } else if (paramCode.equals(TestResultDataUtil.TIME_BEFORE_BED)) {//睡前
            lowAndHigh[0] = rangeBean.getLowBeforeSleep();
            lowAndHigh[1] = rangeBean.getHighBeforeSleep();
        }
        return lowAndHigh;
    }

    public void setRangeBean(BloodRangeBean rangeBean) {
        this.rangeBean = rangeBean;
    }

    public interface TimeInterface {
        void onTime(TimeModel timeModel);
    }
}
