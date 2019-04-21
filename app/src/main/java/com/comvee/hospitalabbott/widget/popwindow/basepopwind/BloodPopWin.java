package com.comvee.hospitalabbott.widget.popwindow.basepopwind;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.tool.TestResultDataUtil;
import com.comvee.hospitalabbott.widget.pick.LoopView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comv098 on 2017/3/30.
 */

public class BloodPopWin extends PopupWindow implements View.OnClickListener {


    private View contentView;//root view
    private View pickerContainerV;
    private Context mContext;
    private OnBloodListener mListener;

    private TextView cancelBtn;
    private TextView confirmBtn;
    private LoopView integerPicker;
    private LoopView decimalPicker;

    private List<String> integerList = new ArrayList();
    private List<String> decimalList = new ArrayList();

    private String textCancel;
    private String textConfirm;

    private int selectInteger = 0;
    private int selectDecimal = 0;

    public BloodPopWin(BloodPopWin.Builder builder) {
        this.mContext = builder.getContext();
        mListener = builder.getListener();
        textCancel = builder.getTextCancel();
        textConfirm = builder.getTextConfirm();
        selectInteger = builder.getSelectInteger();
        selectDecimal = builder.getSelectDecimal();
        initView();
    }

    private void initView() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.popwind_blood_picker, null);
        pickerContainerV = contentView.findViewById(R.id.container_picker);

        cancelBtn = (TextView) contentView.findViewById(R.id.btn_cancel);
        confirmBtn = (TextView) contentView.findViewById(R.id.btn_confirm);
        cancelBtn.setText(textCancel);
        confirmBtn.setText(textConfirm);

        integerPicker = (LoopView) contentView.findViewById(R.id.pickerView_integer);
        decimalPicker = (LoopView) contentView.findViewById(R.id.pickerView_decimal);

        if (integerList == null || integerList.size() == 0) {
            integerList = TestResultDataUtil.getBloodIntegerValues();
        }
        if (decimalList == null || decimalList.size() == 0) {
            decimalList = TestResultDataUtil.getBloodDecimalValues();
        }
        initPickerViews(); // init year and month loop view

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

        setTouchable(true);
        setFocusable(true);
        // setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
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

    private void initPickerViews() {
        integerPicker.setDataList((ArrayList) integerList);
        integerPicker.setInitPosition(selectInteger);
        decimalPicker.setDataList((ArrayList) decimalList);
        decimalPicker.setInitPosition(selectDecimal);
    }


    @Override
    public void onClick(View v) {
       if (v == cancelBtn) {
            dismissPopWin();
        } else if (v == confirmBtn) {
            if (null != mListener && decimalList.size() != 0 && integerList.size() != 0) {
                float blood = Float.valueOf(integerList.get(integerPicker.getSelectedItem())
                        + decimalList.get(decimalPicker.getSelectedItem())) / 10f;
                mListener.onBloodCompleted(blood +"");
            }
            dismissPopWin();
        }
    }

    /**
     * Show date picker popWindow
     *
     * @param activity
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);
            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }

    /**
     * Dismiss date picker popWindow
     */
    public void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                dismiss();
            }
        });

        pickerContainerV.startAnimation(trans);
    }

    public static class Builder {
        private Context context;
        private OnBloodListener listener;

        public Builder(Context context, OnBloodListener listener) {
            this.context = context;
            this.listener = listener;
        }

        public BloodPopWin build() {
            return new BloodPopWin(this);
        }

        private String textCancel = "取消";
        private String textConfirm = "确定";
        private int selectInteger = 0;
        private int selectDecimal = 0;

        public Context getContext() {
            return context;
        }

        public OnBloodListener getListener() {
            return listener;
        }

        public String getTextCancel() {
            return textCancel;
        }

        public BloodPopWin.Builder setTextCancel(String textCancel) {
            this.textCancel = textCancel;
            return this;
        }

        public String getTextConfirm() {
            return textConfirm;
        }

        public BloodPopWin.Builder setTextConfirm(String textConfirm) {
            this.textConfirm = textConfirm;
            return this;
        }

        public int getSelectInteger() {
            return selectInteger;
        }

        public BloodPopWin.Builder setSelectInteger(int selectInteger) {
            this.selectInteger = selectInteger;
            return this;
        }

        public int getSelectDecimal() {
            return selectDecimal;
        }

        public BloodPopWin.Builder setSelectDecimal(int selectDecimal) {
            this.selectDecimal = selectDecimal;
            return this;
        }
    }

    public interface OnBloodListener {

        /**
         * Listener when date has been checked
         */
        void onBloodCompleted(String dateDesc);
    }

}
