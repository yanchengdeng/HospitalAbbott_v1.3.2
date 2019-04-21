package com.comvee.hospitalabbott.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comvee.hospitalabbott.R;


/**
 * Created by comv098 on 2017/1/22.
 */

public class BottomView extends LinearLayout {

    private TextView one;
    private TextView two;
    private TextView three;
    private TextView four;
    private TextView countTv;


    private BottomOnClick bottomOnClick;

    public interface BottomOnClick{
        void onBedClick();
        void onWardClick();
        void onZxingClick();
        void onSetClick();
    }

    public void setBottomOnClick(BottomOnClick bottomOnClick) {
        this.bottomOnClick = bottomOnClick;
    }

    public BottomView(Context context) {
        this(context, null);
    }

    public BottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setBackgroundColor(getResources().getColor(R.color.bottom_color));
        LayoutInflater.from(context).inflate(R.layout.custom_bottom_bar, this, true);
        one = (TextView) findViewById(R.id.bottom_one);
        two = (TextView) findViewById(R.id.bottom_two);
        three = (TextView) findViewById(R.id.bottom_three);
        four = (TextView) findViewById(R.id.bottom_four);
        countTv = (TextView) findViewById(R.id.fragment_main_countTv);

        findViewById(R.id.bottom_one_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomOnClick!=null)
                    bottomOnClick.onBedClick();
            }
        });
        findViewById(R.id.bottom_two_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomOnClick!=null)
                    bottomOnClick.onWardClick();
            }
        });
        findViewById(R.id.bottom_three_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomOnClick!=null)
                    bottomOnClick.onZxingClick();
            }
        });

        findViewById(R.id.bottom_four_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomOnClick!=null)
                    bottomOnClick.onSetClick();
            }
        });
    }

    public void selectIndex(int position) {
       switch (position){
           case 0:
               one.setSelected(true);
               two.setSelected(false);
               three.setSelected(false);
               four.setSelected(false);
               break;
           case 1:
               two.setSelected(true);
               one.setSelected(false);
               three.setSelected(false);
               four.setSelected(false);
               break;
           case 2:
               three.setSelected(true);
               one.setSelected(false);
               two.setSelected(false);
               four.setSelected(false);
               break;
           case 3:
               four.setSelected(true);
               one.setSelected(false);
               two.setSelected(false);
               three.setSelected(false);
               break;
       }
    }

    public void setCountView(int total){
        countTv.setText(total+"");
        countTv.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
    }
}
