package com.comvee.hospitalabbott.widget.popwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.widget.pick.LoopView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by F011512088 on 2017/4/11.
 */

public class DataPopWin extends PopupWindow implements View.OnClickListener {

    private View contentView;//root view
    private View pickerContainerV;
    private Context mContext;
    private OnDataListener mListener;

    private TextView cancelBtn;
    private TextView confirmBtn;
    private String textCancel;
    private String textConfirm;

    private LoopView yearLV;
    private LoopView monthLV;
    private LoopView dayLV;

    private List<String> yearList = new ArrayList<>();
    private List<String> monthList = new ArrayList<>();
    private List<String> dayList = new ArrayList<>();


    public DataPopWin(DataPopWin.Builder builder) {
        this.mContext = builder.getContext();
        mListener = builder.getListener();
        textCancel = builder.getTextCancel();
        textConfirm = builder.getTextConfirm();
        initView();
    }

    private void initView() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_data_picker, null);
        pickerContainerV = contentView.findViewById(R.id.container_picker);
        cancelBtn = (TextView) contentView.findViewById(R.id.btn_cancel);
        cancelBtn.setText(textCancel);
        cancelBtn.setOnClickListener(this);
        confirmBtn = (TextView) contentView.findViewById(R.id.btn_confirm);
        confirmBtn.setText(textConfirm);
        confirmBtn.setOnClickListener(this);

        yearLV = (LoopView) contentView.findViewById(R.id.dataView_year);
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        for (int i = 2010; i < 2020; i++) {
            yearList.add(i + "年");
        }
        yearLV.setDataList(yearList);
        yearLV.setInitPosition(year - 2010 < 0 ? 0 : year - 2010);
        monthLV = (LoopView) contentView.findViewById(R.id.dataView_month);
        for (int i = 1; i < 13; i++) {
            monthList.add(i + "月");
        }
        monthLV.setDataList(monthList);
        monthLV.setInitPosition(month - 1);
        dayLV = (LoopView) contentView.findViewById(R.id.dataView_day);
        for (int i = 1; i < 31; i++) {
            dayList.add(i + "日");
        }
        dayLV.setDataList(dayList);
        dayLV.setInitPosition(day - 1);

        setTouchable(true);
        setFocusable(true);
        // setOutsideTouchable(true);
//        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = pickerContainerV.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                //回去时间日期
                if (mListener != null)
                    mListener.onDataCompleted(yearList.get(yearLV.getSelectedItem())
                            + monthList.get(monthLV.getSelectedItem()) + dayList.get(dayLV.getSelectedItem()));
                dismiss();
                break;
        }
    }

    /**
     * Show date  popWindow
     *
     * @param view
     */
    public void showPopWin(View view) {
        showAtLocation(view, Gravity.TOP, 0, 0);
        showAsDropDown(view);
    }

    public static class Builder {
        private Context context;
        private DataPopWin.OnDataListener listener;

        public Builder(Context context, DataPopWin.OnDataListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public DataPopWin build() {
            return new DataPopWin(this);
        }

        private String textCancel = "取消";
        private String textConfirm = "确定";

        public Context getContext() {
            return context;
        }

        public DataPopWin.OnDataListener getListener() {
            return listener;
        }

        public String getTextCancel() {
            return textCancel;
        }

        public DataPopWin.Builder setTextCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public String getTextConfirm() {
            return textConfirm;
        }

        public DataPopWin.Builder setTextConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }


    }

    public interface OnDataListener {

        /**
         * Listener when date has been checked
         */
        void onDataCompleted(String dateDesc);
    }


}
