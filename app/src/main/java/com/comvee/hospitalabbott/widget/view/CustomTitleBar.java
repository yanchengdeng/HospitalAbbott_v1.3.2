package com.comvee.hospitalabbott.widget.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.tool.NextSubscriber;
import com.comvee.hospitalabbott.tool.RxJavaUtil;


public class CustomTitleBar extends RelativeLayout {
    //    private Button titleBarLeftBtn;
    private RelativeLayout titleLeft;
    private RelativeLayout titleRight;
    private ImageView titleBarLeftIv;
    private TextView titleBarRightTV;
    private TextView titleBarTitle;
    private int rightButtonTextColor;

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_title_bar, this, true);
        titleLeft = (RelativeLayout) findViewById(R.id.title_rl_left);
        titleRight = (RelativeLayout) findViewById(R.id.title_rl_right);
//        titleBarLeftBtn = (Button) findViewById(R.id.title_bar_left);
        titleBarLeftIv = (ImageView) findViewById(R.id.title_bar_left);
        titleBarRightTV = (TextView) findViewById(R.id.title_bar_right);
        titleBarTitle = (TextView) findViewById(R.id.title_bar_title);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar);
        if (attributes != null) {
            //处理titleBar背景色
            int titleBarBackGround = attributes.getResourceId(R.styleable.CustomTitleBar_title_background_color,
                    Color.GREEN);
            if (titleBarBackGround != 0)
                setBackgroundResource(titleBarBackGround);
            //先处理左边按钮
            //获取是否要显示左边按钮
            boolean leftButtonVisible = attributes.getBoolean(R.styleable.CustomTitleBar_left_button_visible, true);
            if (leftButtonVisible) {
//                titleBarLeftBtn.setVisibility(View.VISIBLE);
                titleBarLeftIv.setVisibility(View.VISIBLE);
            } else {
//                titleBarLeftBtn.setVisibility(View.INVISIBLE);
                titleBarLeftIv.setVisibility(View.INVISIBLE);
            }
            //设置左边按钮的文字
            String leftButtonText = attributes.getString(R.styleable.CustomTitleBar_left_button_text);
            if (!TextUtils.isEmpty(leftButtonText)) {
//                titleBarLeftBtn.setText(leftButtonText);
                //设置左边按钮文字颜色
                int leftButtonTextColor = attributes.getColor(R.styleable.CustomTitleBar_left_button_text_color,
                        Color.WHITE);
//                titleBarLeftBtn.setTextColor(leftButtonTextColor);
            } else {
                //设置左边图片icon 这里是二选一 要么只能是文字 要么只能是图片
                int leftButtonDrawable = attributes.getResourceId(R.styleable.CustomTitleBar_left_button_drawable,
                        R.drawable.top_back);
                if (leftButtonDrawable != -1) {
//                    titleBarLeftBtn.setBackgroundResource(leftButtonDrawable);
                    titleBarLeftIv.setImageResource(leftButtonDrawable);
                }
            }

//            int leftButtonDrawable = attributes.getResourceId(R.styleable.CustomTitleBar_left_button_drawable,
//                    R.drawable.hospital_76);
//            if (leftButtonDrawable != -1) {
//                titlerLeftIV.setBackgroundResource(leftButtonDrawable);
//            }

            //处理标题
            //先获取标题是否要显示图片icon
            int titleTextDrawable = attributes.getResourceId(R.styleable.CustomTitleBar_title_text_drawable, -1);
            if (titleTextDrawable != -1) {
                titleBarTitle.setBackgroundResource(titleTextDrawable);
            } else {
                //如果不是图片标题 则获取文字标题
                String titleText = attributes.getString(R.styleable.CustomTitleBar_title_text);
                if (!TextUtils.isEmpty(titleText)) {
                    titleBarTitle.setText(titleText);
                }
                //获取标题显示颜色
                int titleTextColor = attributes.getColor(R.styleable.CustomTitleBar_title_text_color, Color.WHITE);
                titleBarTitle.setTextColor(titleTextColor);
            }

            //先处理右边按钮
            //获取是否要显示右边按钮
            boolean rightButtonVisible = attributes.getBoolean(R.styleable.CustomTitleBar_right_button_visible, true);
            if (rightButtonVisible) {
                titleBarRightTV.setVisibility(View.VISIBLE);
            } else {
                titleBarRightTV.setVisibility(View.INVISIBLE);
            }
            //设置右边按钮的文字
            String rightButtonText = attributes.getString(R.styleable.CustomTitleBar_right_button_text);
            if (!TextUtils.isEmpty(rightButtonText)) {
                titleBarRightTV.setText(rightButtonText);
                //设置右边按钮文字颜色
                rightButtonTextColor = attributes.getColor(R.styleable.CustomTitleBar_right_button_text_color, Color.WHITE);
                titleBarRightTV.setTextColor(rightButtonTextColor);
            } else {
                //设置右边图片icon 这里是二选一 要么只能是文字 要么只能是图片
                int rightButtonDrawable = attributes.getResourceId(R.styleable.CustomTitleBar_right_button_drawable, -1);
                if (rightButtonDrawable != -1) {
                    titleBarRightTV.setBackgroundResource(rightButtonDrawable);
                }
            }
            attributes.recycle();
        }
    }

    public void setTitleClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
//            titleBarLeftBtn.setOnClickListener(onClickListener);
            titleRight.setOnClickListener(onClickListener);
//            titleBarRightBtn.setOnClickListener(onClickListener);
            titleRight.setOnClickListener(onClickListener);
        }
    }

    //    public Button getTitleBarLeftBtn() {
//        return titleBarLeftBtn;
//    }


    public ImageView getTitleBarLeftBtn() {
        return titleBarLeftIv;
    }

    public TextView getTitleBarRightBtn() {
        return titleBarRightTV;
    }

    public TextView getTitleBarTitle() {
        return titleBarTitle;
    }

    public CustomTitleBar setTitleBarTitleText(String titleText) {
        titleBarTitle.setText(titleText);
        return this;
    }

    public CustomTitleBar setTitleRlLeftVisibility(boolean visibility) {
        if (visibility)
            titleLeft.setVisibility(VISIBLE);
        else
            titleLeft.setVisibility(GONE);
        return this;
    }

    public CustomTitleBar bindActivity(final Activity context) {
        titleLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
        return this;
    }

    public CustomTitleBar setLeftClick(OnClickListener onClickListener) {
        titleLeft.setOnClickListener(onClickListener);
        return this;
    }

    public CustomTitleBar setRightVisibility(boolean visibility) {
        if (visibility)
            titleRight.setVisibility(VISIBLE);
        else
            titleRight.setVisibility(GONE);
        return this;
    }

    public void setTitleBarRightBtn(String titleText, @Nullable NextSubscriber nextSubscriber) {
        titleBarRightTV.setVisibility(VISIBLE);
        titleBarRightTV.setText(titleText);
        titleBarRightTV.setTextColor(getResources().getColor(R.color.white));
        RxJavaUtil.clicks(titleRight, nextSubscriber);
//        titleRight.setOnClickListener(l);
    }

}