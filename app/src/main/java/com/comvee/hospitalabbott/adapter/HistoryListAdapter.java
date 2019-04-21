package com.comvee.hospitalabbott.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;


import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.MyBaseViewHolder;
import com.comvee.hospitalabbott.bean.DayBean;
import com.comvee.hospitalabbott.bean.MemberHistoryModel;
import com.comvee.hospitalabbott.bean.ParamLogListBean;
import com.comvee.hospitalabbott.tool.NextSubscriber;
import com.comvee.hospitalabbott.tool.RxJavaUtil;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.ui.history.DayHistoryActivity;

import java.lang.ref.WeakReference;
import java.util.List;


/**
 * Created by comv098 on 2017/1/18.
 */

public class HistoryListAdapter extends QuickAdapter<MemberHistoryModel> {

    private WeakReference<Activity> weakReference;

    public HistoryListAdapter(List<MemberHistoryModel> data, Activity activity) {
        super(R.layout.fragment_history_list_item, data);
        this.weakReference = new WeakReference<>(activity);
    }

    @Override
    protected void convert(MyBaseViewHolder helper, MemberHistoryModel item) {
        toFragment(helper, item);

        helper.setText(R.id.history_fasting, valueToString(item.getBeforeDawnList()),
                getTextColor(item.getBeforeDawnList()))

                .setText(R.id.history_three, valueToString(item.getThreeclockList()),
                        getTextColor(item.getThreeclockList()))

                .setText(R.id.history_breakfast_before, valueToString(item.getBeforeBreakfast()),
                        getTextColor(item.getBeforeBreakfast()))

                .setText(R.id.history_breakfast_rear, valueToString(item.getAfterBreakfast()),
                        getTextColor(item.getAfterBreakfast()))

                .setText(R.id.history_lunch_before, valueToString(item.getBeforeLunch()),
                        getTextColor(item.getBeforeLunch()))

                .setText(R.id.history_lunch_rear, valueToString(item.getAfterLunch()),
                        getTextColor(item.getAfterLunch()))

                .setText(R.id.history_dinner_before, valueToString(item.getBeforeDinner()),
                        getTextColor(item.getBeforeDinner()))

                .setText(R.id.history_dinner_rear, valueToString(item.getAfterDinner()),
                        getTextColor(item.getAfterDinner()))

                .setText(R.id.history_beforeBed, valueToString(item.getBeforeSleep()),
                        getTextColor(item.getBeforeSleep()))

                .setText(R.id.history_random, valueToString(item.getRandomtime()),
                        getTextColor(item.getRandomtime()))
                .setText(R.id.history_date, item.getDate().substring(5, item.getDate().length()))
                //item.getDate().substring(5, 10 > item.getDate().length() ? item.getDate().length() : 10)
                .setVisible(R.id.history_fasting_iv, item.getBeforeDawnList().size() > 1)
                .setVisible(R.id.history_three_iv, item.getThreeclockList().size() > 1)
                .setVisible(R.id.history_breakfast_before_iv, item.getBeforeBreakfast().size() > 1)
                .setVisible(R.id.history_breakfast_rear_iv, item.getAfterBreakfast().size() > 1)
                .setVisible(R.id.history_lunch_before_iv, item.getBeforeLunch().size() > 1)
                .setVisible(R.id.history_lunch_rear_iv, item.getAfterLunch().size() > 1)
                .setVisible(R.id.history_dinner_before_iv, item.getBeforeDinner().size() > 1)
                .setVisible(R.id.history_dinner_rear_iv, item.getAfterDinner().size() > 1)
                .setVisible(R.id.history_beforeBed_iv, item.getBeforeSleep().size() > 1)
                .setVisible(R.id.history_random_iv, item.getRandomtime().size() > 1);


        int position = helper.getLayoutPosition() - getHeaderLayoutCount();

        if ((position + 1) == getData().size()) {
            View view = helper.getView(R.id.history_last);
            view.setBackgroundColor(mContext.getResources().getColor(R.color.divider_blue));
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.divider_width);
            view.setLayoutParams(layoutParams);
        } else {
            View view = helper.getView(R.id.history_last);
            view.setBackgroundColor(mContext.getResources().getColor(R.color.gray_divider));
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = 1;
            view.setLayoutParams(layoutParams);
        }

    }

    private int getTextColor(List<ParamLogListBean> item) {
        if (item.isEmpty()) return mContext.getResources().getColor(R.color.green);
//        LogUtils.e("values =" + values);
//        if (values == 0) return mContext.getResources().getColor(R.color.red); //值为零的情况下一般都是拒绝状态
        ParamLogListBean bean = item.get(0);
        if (item != null) {
            if (bean.getStatus() != null)
                if (bean.getStatus().equals("2"))
                    return mContext.getResources().getColor(R.color.red); //拒绝状态
        } else
            return mContext.getResources().getColor(R.color.red); //拒绝状态

        int level = Integer.valueOf(bean.getLevel());
        if (level < 3) {
            return mContext.getResources().getColor(R.color.blue);
        } else if (level > 3) {
            return mContext.getResources().getColor(R.color.red);
        } else {
            return mContext.getResources().getColor(R.color.green);
        }
    }

    private String valueToString(List<ParamLogListBean> list) {
        if (list.isEmpty()) return "";
        ParamLogListBean bean = list.get(0);
        if (bean == null || TextUtils.isEmpty(bean.getStatus())) return "";
        if (!bean.getStatus().equals("2")) { // 2 拒绝监测
            float value = Float.valueOf(bean.getValue());
            if (value != 0)
                return value == (int) value ? (int) value + "" : value + "";
            else
                return "";
        }
        return "拒";
    }

    /**
     * TIME_BEFORE_DAWN = "凌晨";
     * TIME_3AM = "3AM";
     * TIME_FAST = "空腹";
     * TIME_AFTER_BREAKFAST = "早餐后";
     * TIME_BEFORE_LUNCH = "午餐前";
     * TIME_AFTER_LUNCH = "午餐后";
     * TIME_BEFORE_DINNER = "晚餐前";
     * TIME_AFTER_DINNER = "晚餐后";
     * TIME_BEFORE_BED = "睡前";
     * TIME_RANDOM = "随机";
     */
    private void toFragment(MyBaseViewHolder helper, MemberHistoryModel item) {
        setTextClick(helper.getView(R.id.history_fasting_rl), item.getDate(), TestResultDataUtil.TIME_0AM, item.getBeforeDawnList());

        setTextClick(helper.getView(R.id.history_three_rl), item.getDate(), TestResultDataUtil.TIME_3AM, item.getBeforeDawnList());

        setTextClick(helper.getView(R.id.history_breakfast_before_rl), item.getDate(), TestResultDataUtil.TIME_FAST, item.getBeforeBreakfast());

        setTextClick(helper.getView(R.id.history_breakfast_rear_rl), item.getDate(), TestResultDataUtil.TIME_AFTER_BREAKFAST, item.getAfterBreakfast());

        setTextClick(helper.getView(R.id.history_lunch_before_rl), item.getDate(), TestResultDataUtil.TIME_BEFORE_LUNCH, item.getBeforeLunch());

        setTextClick(helper.getView(R.id.history_lunch_rear_rl), item.getDate(), TestResultDataUtil.TIME_AFTER_LUNCH, item.getAfterLunch());

        setTextClick(helper.getView(R.id.history_dinner_before_rl), item.getDate(), TestResultDataUtil.TIME_BEFORE_DINNER, item.getBeforeDinner());

        setTextClick(helper.getView(R.id.history_dinner_rear_rl), item.getDate(), TestResultDataUtil.TIME_AFTER_DINNER, item.getAfterDinner());

        setTextClick(helper.getView(R.id.history_beforeBed_rl), item.getDate(), TestResultDataUtil.TIME_BEFORE_BED, item.getBeforeSleep());

        setTextClick(helper.getView(R.id.history_random_rl), item.getDate(), TestResultDataUtil.TIME_RANDOM, item.getRandomtime());
    }

    private void setTextClick(View view, final String recordTime, final String paramCode,
                              final List<ParamLogListBean> mList) {
        if (mList.size() > 1) {
            RxJavaUtil.clicks(view, new NextSubscriber() {
                @Override
                public void call(Object o) {
                    Intent intent = new Intent(weakReference.get(), DayHistoryActivity.class);
                    DayBean dayBean = new DayBean(recordTime, paramCode, mList);
                    intent.putExtra(DayHistoryActivity.RECORD_DAY_DATA, dayBean);
                    weakReference.get().startActivity(intent);
                }
            });
        }
    }

}
