package com.comvee.hospitalabbott.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.comvee.greendao.gen.TestInfoDao;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.XTYApplication;
import com.comvee.hospitalabbott.bean.BloodRangeBean;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.bean.ParamCodeBean;
import com.comvee.hospitalabbott.bean.TestInfo;
import com.comvee.hospitalabbott.helper.RangeHelper;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.ui.detection.TestBloodActivity;
import com.comvee.hospitalabbott.widget.calendar.TimeUtil;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import okhttp3.internal.Util;

public class BedGridAdapter extends BaseQuickAdapter<HospitalBed, BaseViewHolder> {
    private Activity activity;
    TestInfoDao dao = XTYApplication.getInstance().getDaoSession().getTestInfoDao();

    public BedGridAdapter(@Nullable List<HospitalBed> data, Activity activity) {
        super(R.layout.bed_item, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final HospitalBed bedModel) {
        helper.setText(R.id.tv_name, bedModel.getMemberName());
        if (bedModel.getBedNo().contains("加")) {
            helper.setVisible(R.id.tv_add, true);
            helper.setText(R.id.tv_bed_num, bedModel.getBedNo().substring(1));
        } else {
            helper.setGone(R.id.tv_add, false);
            helper.setText(R.id.tv_bed_num, bedModel.getBedNo());
        }


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //测血糖
                TestBloodActivity.startBloodActivity(activity, bedModel, "");
            }
        });
        ParamCodeBean paramCodeBean = bedModel.getParamCodeModel();
        List<TestInfo> list = dao.queryBuilder().where(TestInfoDao.Properties.MemberId.eq(bedModel.getMemberId())).orderDesc(TestInfoDao.Properties.RecordTime).build().list();
        if (list != null && list.size() > 0) {
            TestInfo info = list.get(0);
            if (paramCodeBean == null) {
                initParam(helper, info);
            } else {
                long l1 = TimeUtil.stringToLong(paramCodeBean.getRecordTime(), "yyyy-MM-dd HH:mm:ss");
                long l2 = TimeUtil.stringToLong(info.getRecordTime(), "yyyy-MM-dd HH:mm:ss");
                if (l1 >= l2){
                    if (paramCodeBean.getStatus().equals("2")) {
                        helper.setText(R.id.tv_param_code, TestResultDataUtil.getPeriod(paramCodeBean.getParamCode()));
                    } else {
                        initParam(helper, paramCodeBean);
                    }
                }else {
                    if (info.getStateInte() == 2) {
                        helper.setText(R.id.tv_param_code, TestResultDataUtil.getPeriod(info.getParamCode()));
                    } else {
                        initParam(helper, info);
                    }
                }

            }

        } else {
            if (paramCodeBean == null) return;
            if (paramCodeBean.getStatus().equals("2")) {
                helper.setText(R.id.tv_param_code, TestResultDataUtil.getPeriod(paramCodeBean.getParamCode()));
            } else {
                initParam(helper, paramCodeBean);
            }
        }
    }


    /**
     * 根据时间段血糖数据设置UI
     *
     * @param helper
     * @param paramCodeBean
     */
    private void initParam(BaseViewHolder helper, ParamCodeBean paramCodeBean) {
        String valueStr;
        int level = -1;
        if (!TextUtils.isEmpty(paramCodeBean.getLevel()))
            level = Integer.valueOf(paramCodeBean.getLevel());
        if (TextUtils.isEmpty(paramCodeBean.getValue())) {
            valueStr = "--";
        } else {
            valueStr = paramCodeBean.getValue();
        }
        //设置高低剪头 左间距
        try {
            int value = Integer.valueOf(paramCodeBean.getValue());
            ImageView iv = helper.getView(R.id.iv_arrow_icon);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
            float leftMargin;
            if (value < 10) {
                leftMargin = DensityUtil.px2dp(15);

            } else {
                leftMargin = DensityUtil.px2dp(10);
            }
            layoutParams.setMargins((int) leftMargin, layoutParams.topMargin, layoutParams.rightMargin,
                    layoutParams.bottomMargin);
        } catch (NumberFormatException e) {
//            e.printStackTrace();
        }

        int valuesImageId;
        int biasedImageId = 0;
        int textColors;
        if (level < 0) {
            textColors = R.color.text_null;
            valuesImageId = 0;
            biasedImageId = 0;
        } else if (level < 3) {
            valuesImageId = R.drawable.hospital_50;
            biasedImageId = R.drawable.blood_33;
            textColors = R.color.blue;
        } else if (level > 3) {
            valuesImageId = R.drawable.hospital_46;
            biasedImageId = R.drawable.blood_31;
            textColors = R.color.red;
        } else {
            valuesImageId = R.drawable.hospital_48;
            textColors = R.color.green;
        }
        if (!TextUtils.isEmpty(paramCodeBean.getParamCode())) { //设置时间段
            helper.setGone(R.id.tv_param_code, true)
                    .setText(R.id.tv_param_code, TestResultDataUtil.getPeriod(paramCodeBean.getParamCode()));
        } else {
            helper.setGone(R.id.tv_param_code, false);
        }
        helper.setText(R.id.tv_values, valueStr)
                .setTextColor(R.id.tv_values, mContext.getResources().getColor(textColors))
                .setVisible(R.id.iv_values, level > 0 ? true : false)
                .setVisible(R.id.iv_arrow_icon, level > 0 ? true : false)
                .setBackgroundRes(R.id.iv_values, valuesImageId)
                .setBackgroundRes(R.id.iv_arrow_icon, biasedImageId);
    }


    /**
     * 根据时间段血糖数据设置UI
     *
     * @param helper
     * @param paramCodeBean
     */
    private void initParam(BaseViewHolder helper, TestInfo paramCodeBean) {
        String valueStr;
        int level = -1;
        float lowFloat,highFloat;
        float value = Float.parseFloat(paramCodeBean.getValue());
        BloodRangeBean rangeBean = RangeHelper.getBloodRangeBean(paramCodeBean.getMemberId());
        if (rangeBean != null) {
            String[] lowAndHigh = TestResultDataUtil.getLowAndHigh(paramCodeBean.getParamCode(), rangeBean);
            lowFloat = Float.parseFloat(lowAndHigh[0]);
            highFloat =  Float.parseFloat(lowAndHigh[1]);
        } else {
            lowFloat = 4;
            highFloat = 10;
        }

        if (value < lowFloat){
            level = 2;
        }else if (value > highFloat){
            level = 4;
        }else {
            level = 3;
        }

        if (TextUtils.isEmpty(paramCodeBean.getValue())) {
            valueStr = "--";
        } else {
            valueStr = paramCodeBean.getValue();
        }
        //设置高低剪头 左间距
        try {
            ImageView iv = helper.getView(R.id.iv_arrow_icon);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
            float leftMargin;
            if (value < 10) {
                leftMargin = DensityUtil.px2dp(15);

            } else {
                leftMargin = DensityUtil.px2dp(10);
            }
            layoutParams.setMargins((int) leftMargin, layoutParams.topMargin, layoutParams.rightMargin,
                    layoutParams.bottomMargin);
        } catch (NumberFormatException e) {
//            e.printStackTrace();
        }

        int valuesImageId;
        int biasedImageId = 0;
        int textColors;
        if (level < 0) {
            textColors = R.color.text_null;
            valuesImageId = 0;
            biasedImageId = 0;
        } else if (level < 3) {
            valuesImageId = R.drawable.hospital_50;
            biasedImageId = R.drawable.blood_33;
            textColors = R.color.blue;
        } else if (level > 3) {
            valuesImageId = R.drawable.hospital_46;
            biasedImageId = R.drawable.blood_31;
            textColors = R.color.red;
        } else {
            valuesImageId = R.drawable.hospital_48;
            textColors = R.color.green;
        }
        if (!TextUtils.isEmpty(paramCodeBean.getParamCode())) { //设置时间段
            helper.setGone(R.id.tv_param_code, true)
                    .setText(R.id.tv_param_code, TestResultDataUtil.getPeriod(paramCodeBean.getParamCode()));
        } else {
            helper.setGone(R.id.tv_param_code, false);
        }
        helper.setText(R.id.tv_values, valueStr)
                .setTextColor(R.id.tv_values, mContext.getResources().getColor(textColors))
                .setVisible(R.id.iv_values, level > 0 ? true : false)
                .setVisible(R.id.iv_arrow_icon, level > 0 ? true : false)
                .setBackgroundRes(R.id.iv_values, valuesImageId)
                .setBackgroundRes(R.id.iv_arrow_icon, biasedImageId);
    }

}
