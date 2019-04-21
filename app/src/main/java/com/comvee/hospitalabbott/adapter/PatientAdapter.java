package com.comvee.hospitalabbott.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.MyBaseViewHolder;
import com.comvee.hospitalabbott.bean.SearchBean;

import java.util.List;

/**
 * Created by F011512088 on 2018/1/30.
 */

public class PatientAdapter extends QuickAdapter<SearchBean> {

    private int selectPosition = -1;

    public PatientAdapter(List<SearchBean> data) {
        super(R.layout.adapter_patient, data);
    }

    @Override
    protected void convert(MyBaseViewHolder helper, SearchBean item) {
        final int positions = helper.getLayoutPosition() - getHeaderLayoutCount();
        if (positions % 2 == 0) { //判断奇数偶数
            helper.setBackgroundColor(R.id.ll_layout, Color.parseColor("#ffffff"));
        } else {
            helper.setBackgroundColor(R.id.ll_layout, Color.parseColor("#f7f7f9"));
        }
        helper.setText(R.id.name_department, item.getDepartmentName())
                .setText(R.id.no_Bed, item.getBedNo())
                .setText(R.id.name_patient, item.getMemberName());

        ImageView imageView = helper.getView(R.id.patient_checkBox);
        if (selectPosition == positions) {
            imageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.hospital_161));
        } else {
            imageView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.hospital_163));
        }
//        helper.getView(R.id.ll_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectPosition = positions;
//                notifyDataSetChanged();
//                RxBus.getDefault().post(mData.get(selectPosition));
//            }
//        });

    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public SearchBean getSelectSearchBean() {
        if (selectPosition == -1) return null;
        return mData.get(selectPosition);
    }


}