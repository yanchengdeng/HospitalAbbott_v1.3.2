package com.comvee.hospitalabbott.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.MyBaseViewHolder;
import com.comvee.hospitalabbott.bean.TimeModel;

import java.util.List;

/**
 * Created by comv098 on 2017/3/7.
 */

public class ResultTimeAdapter extends QuickAdapter<String> {

    private int selectedIndex = -1;
    private TimeInterface timeInterface;

    public ResultTimeAdapter(List data, TimeInterface timeInterface) {
        super(R.layout.adapter_result_time, data);
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
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.rounded_rectangle_select);
            helper.setBackground(R.id.time_tv, drawable)
                    .setTextColor(R.id.time_tv, mContext.getResources().getColor(R.color.white));
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

    public interface TimeInterface {
        void onTime(TimeModel timeModel);
    }
}
