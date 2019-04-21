package com.comvee.hospitalabbott.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;


/**
 * Created by comv098 on 2017/2/5.
 */

public class AboutItemView extends RelativeLayout {

    private TextView leftTV;
    private TextView rightTV;
    private ImageView rightIV;

    private String leftStr = "";
    private String rightStr = "";

    private float left_drawablePadding;
    private float marginLeft;
    private float marginRight;
    private int right_icon;
    private float right_icon_marginRight;
    private boolean isShow;
    private int left_icon;

    public AboutItemView(Context context) {
        this(context, null);
    }

    public AboutItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AboutItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.about_item_view, this, true);
        leftTV = (TextView) findViewById(R.id.left_tv);
        rightTV = (TextView) findViewById(R.id.right_tv);
        rightIV = (ImageView) findViewById(R.id.right_iv);

        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.AboutItemView);
        left_icon = attr.getResourceId(R.styleable.AboutItemView_left_log, 0);
        left_drawablePadding = attr.getDimension(R.styleable.AboutItemView_left_drawablePadding, 0);

        int rightTVColor = attr.getColor(R.styleable.AboutItemView_right_text_color,
                getResources().getColor(R.color.text_gray_66));

        marginLeft = attr.getDimension(R.styleable.AboutItemView_marginLeft, 16);
        marginRight = attr.getDimension(R.styleable.AboutItemView_marginRight, 16);
        right_icon = attr.getResourceId(R.styleable.AboutItemView_right_icon, R.drawable.hospital_107);
        right_icon_marginRight = attr.getDimension(R.styleable.AboutItemView_right_icon_marginRight, 16);
        isShow = attr.getBoolean(R.styleable.AboutItemView_right_icon_visibility, true);
        leftStr = attr.getString(R.styleable.AboutItemView_left_text);
        rightStr = attr.getString(R.styleable.AboutItemView_right_text);
        attr.recycle();

        LayoutParams leftLP = (LayoutParams) leftTV.getLayoutParams();
        leftLP.setMargins((int) marginLeft, 0, 0, 0);
        leftTV.setLayoutParams(leftLP);
        leftTV.setText(leftStr);
        leftTV.setCompoundDrawablePadding((int) left_drawablePadding);
        if (left_icon != 0)
            leftTV.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(left_icon),
                    null, null, null);

        LayoutParams rightTVLP = (LayoutParams) rightTV.getLayoutParams();
        rightTVLP.setMargins(0, 0, (int) right_icon_marginRight, 0);
        rightTV.setLayoutParams(rightTVLP);
        rightTV.setText(rightStr);
        rightTV.setTextColor(rightTVColor);
        rightIV.setImageDrawable(getResources().getDrawable(right_icon));
        LayoutParams rightIVLP = (LayoutParams) rightIV.getLayoutParams();
        rightIVLP.setMargins(0, 0, (int) marginRight, 0);
        rightIV.setLayoutParams(rightIVLP);

        if (!isShow) {
            rightIV.setVisibility(GONE);
        }

    }

    public void setRightTV(String rightStr) {
        rightTV.setText(rightStr);
    }

    public String getRightStr() {
        return rightTV.getText().toString().trim();
    }
}
