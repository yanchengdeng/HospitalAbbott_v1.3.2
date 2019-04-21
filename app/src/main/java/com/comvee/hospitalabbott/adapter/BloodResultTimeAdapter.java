package com.comvee.hospitalabbott.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.base.MyBaseViewHolder;
import com.comvee.hospitalabbott.bean.TimeModel;

import java.util.List;

/**
 * Created by comv098 on 2017/3/7.
 */

public class BloodResultTimeAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> dayList;
    private int selectedIndex = -1;


    public BloodResultTimeAdapter(Context context, List<String> dayList) {
        this.mContext = context;
        this.dayList = dayList;


    }

    @Override
    public int getCount() {
        return dayList.size();
    }

    @Override
    public Object getItem(int i) {
        return dayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolod viewHolod = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_result_time, null);
            viewHolod = new ViewHolod();
            viewHolod.time_tv = (TextView) view.findViewById(R.id.time_tv);

            view.setTag(viewHolod);
        } else {
            viewHolod = (ViewHolod) view.getTag();
        }
        viewHolod.time_tv.setText(dayList.get(position));
        if (selectedIndex == position) {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.rounded_rectangle_select);
            viewHolod.time_tv.setBackground(drawable);
            viewHolod.time_tv.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            viewHolod.time_tv.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle_no));
            viewHolod.time_tv.setTextColor(mContext.getResources().getColor(R.color.text_gray_66));
        }


        return view;
    }

    public class ViewHolod {
        private TextView time_tv;

    }



    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        notifyDataSetChanged();
    }
}
