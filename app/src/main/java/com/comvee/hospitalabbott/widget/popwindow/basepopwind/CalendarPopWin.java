package com.comvee.hospitalabbott.widget.popwindow.basepopwind;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.widget.calendar.MyCalendarView;
import com.comvee.hospitalabbott.widget.pick.OnDatePickedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by comv098 on 2017/3/30.
 */

public class CalendarPopWin extends PopupWindow {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    private View contentView;//root view
    private View pickerContainerV;
    private Context mContext;
    private OnDatePickedListener mListener;

    private MyCalendarView calendarView;

    public CalendarPopWin(CalendarPopWin.Builder builder) {
        this.mContext = builder.getContext();
        mListener = builder.getListener();
        initView();
    }

    private void initView() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.popwind_calendar_popwin, null);
        calendarView = (MyCalendarView) contentView.findViewById(R.id.calendarView);

        // 设置是否允许在外点击使其消失
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.popupWindow_anim_style);
        //设置SelectPicPopupWindow弹出窗体的背景
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        contentView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = contentView.findViewById(R.id.container_picker).getHeight();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    LogUtils.e("height =" + height + ", y=" + y);
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
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

    public void showPopWin(Activity activity) {
        showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP,
                0, 0);
    }


    /**
     * Dismiss date picker popWindow
     */
    public void dismissPopWin() {
        dismiss();
    }

    public void setOnChoiceCalendarListener(MyCalendarView.OnChoiceCalendarListener mListener) {
        calendarView.setOnChoiceCalendarListener(mListener);
    }

    public void setCurrentDate(Calendar calendar) {
        calendarView.setShowDate(calendar);
    }

    public void updateData(String memberId) {
        calendarView.readHistoryData(memberId);
    }

    ;

    public static class Builder {
        private Context context;
        private OnDatePickedListener listener;

        public Builder(Context context, OnDatePickedListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public CalendarPopWin build() {
            return new CalendarPopWin(this);
        }


        public Context getContext() {
            return context;
        }

        public OnDatePickedListener getListener() {
            return listener;
        }


    }
}
