package com.comvee.hospitalabbott.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.MyBaseViewHolder;
import com.comvee.hospitalabbott.bean.Branch;
import com.comvee.hospitalabbott.bean.HospitalBed;
import com.comvee.hospitalabbott.bean.ParamCodeBean;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.ui.detection.TestBloodActivity;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by F011512088 on 2018/1/10.
 */

public class GroupBedAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, MyBaseViewHolder> {

    public static final int TYPE_DEPARTMENT = 0;//Department
    public static final int TYPE_BED = 1;
    private WeakReference<Activity> weakReference;
    private Drawable arrowDrawable;
    private String paramCode = "";

    public GroupBedAdapter(Activity activity, List data) {
        super(data);
        weakReference = new WeakReference<>(activity);
        arrowDrawable = activity.getResources().getDrawable(R.drawable.ms__arrow).mutate();
        arrowDrawable.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_IN);
        addItemType(TYPE_DEPARTMENT, R.layout.adapter_department_item);
        addItemType(TYPE_BED, R.layout.adapter_bed_item);
    }

    public GroupBedAdapter(Activity activity, List data, String paramCod) {
        this(activity, data);
        this.paramCode = paramCod;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    protected void convert(final MyBaseViewHolder helper, final MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_DEPARTMENT:
                final Branch branch = (Branch) item;
                helper.setGone(R.id.no_member, !((Branch) item).hasSubItem());//没有患者 true 有患者
                TextView textView = helper.getView(R.id.department_name);
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDrawable, null);
                helper.setText(R.id.department_name, branch.getDepartmentName())
                        .getView(R.id.item_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((Branch) item).hasSubItem()) {
                            int pos = helper.getAdapterPosition();
                            if (branch.isExpanded()) {
                                collapse(pos, false);
                                animateArrow(true);
                            } else {
                                expand(pos, false);
                                animateArrow(false);
                            }
                        }
                    }
                });
                break;
            case TYPE_BED:
                final HospitalBed bedModel = (HospitalBed) item;
                initData(helper, bedModel);
                break;
        }
    }

    private void initData(MyBaseViewHolder helper, final HospitalBed bedModel) {
        helper.setText(R.id.bedCard_tv_memberName, bedModel.getMemberName())
                .setText(R.id.bedCard_tv_BedNo, bedModel.getBedNo());
        if (bedModel.getSex().equals("1")) { //男
            helper.setImageResource(R.id.bedCard_iv_BedLog, R.drawable.bloodthree_01);
        } else if (bedModel.getSex().equals("2")) { //女
            helper.setImageResource(R.id.bedCard_iv_BedLog, R.drawable.bloodthree_03);
        } else {
            helper.setImageResource(R.id.bedCard_iv_BedLog, R.drawable.hospital_40);
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //测血糖
                TestBloodActivity.startBloodActivity(weakReference.get(), bedModel, paramCode);
            }
        });
        ParamCodeBean paramCodeBean = bedModel.getParamCodeModel();
        if (paramCodeBean == null) return;
        if (paramCodeBean.getStatus().equals("2")) {
            helper.setGone(R.id.bedCard_ll_refuseMonitor, true)
                    .setGone(R.id.bedCard_ll_values, false)
                    .setText(R.id.refuseMonitor_tv_period, TestResultDataUtil.getPeriod(paramCodeBean.getParamCode()));
        } else {
            helper.setGone(R.id.bedCard_ll_refuseMonitor, false)
                    .setGone(R.id.bedCard_ll_values, true);
            initParam(helper, paramCodeBean);
        }

    }

    /**
     * 根据时间段血糖数据设置UI
     *
     * @param helper
     * @param paramCodeBean
     */
    private void initParam(MyBaseViewHolder helper, ParamCodeBean paramCodeBean) {
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
            ImageView iv = helper.getView(R.id.bedCard_iv_biased);
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
            helper.setGone(R.id.bedCard_tv_period, true)
                    .setText(R.id.bedCard_tv_period, TestResultDataUtil.getPeriod(paramCodeBean.getParamCode()));
        } else {
            helper.setGone(R.id.bedCard_tv_period, false);
        }
        helper.setText(R.id.bedCard_tv_values, valueStr)
                .setTextColor(R.id.bedCard_tv_values, mContext.getResources().getColor(textColors))
                .setGone(R.id.bedCard_iv_values, level > 0 ? true : false)
                .setGone(R.id.bedCard_iv_biased, level > 0 ? true : false)
                .setBackgroundRes(R.id.bedCard_iv_values, valuesImageId)
                .setBackgroundRes(R.id.bedCard_iv_biased, biasedImageId)
                .setVisible(R.id.bedCard_iv_isNetWork, paramCodeBean.isNetWork()); //该条信息是否与服务器同步
    }

    /**
     * 分组缩放动画
     *
     * @param shouldRotateUp
     */
    private void animateArrow(boolean shouldRotateUp) {
        int start = shouldRotateUp ? 0 : 10000;
        int end = shouldRotateUp ? 10000 : 0;
        ObjectAnimator animator = ObjectAnimator.ofInt(arrowDrawable, "level", start, end);
        animator.start();
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }
}
