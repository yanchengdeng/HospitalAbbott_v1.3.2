package com.comvee.hospitalabbott.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.tool.Utils;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.UIUtil;

public class DropDownPopWindow {

    private Activity activity;
    private View showView;
    private View anchorView;
    private ImageView arrow;
    int itemCount;
    PopupWindow mPopWindow;
    public DropDownPopWindow(Activity activity, View showView, View anchorView, ImageView arrow, int itemCount) {
        this.activity = activity;
        this.showView = showView;
        this.anchorView = anchorView;
        this.arrow = arrow;
        this.itemCount = itemCount;
        setPopWindow();
    }

    private void setPopWindow() {
        //显示遮罩层
     //   Utils.setScreenAlpha(activity, 0.9f);
        int windowWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        mPopWindow = new PopupWindow(showView, windowWidth, UIUtil.dip2px(activity, 45*itemCount), true);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //隐藏遮罩层
                Utils.setScreenAlpha(activity, 1f);
                arrow.setImageResource(R.drawable.hospital_34);
            }
        });
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        mPopWindow.showAsDropDown(anchorView, (windowWidth) / 2, 0);
        mPopWindow.setAnimationStyle(R.style.popwin_anim_style);
        mPopWindow.setFocusable(true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.update();
        arrow.setImageResource(R.drawable.hospital_36);
    }

    public void dismiss(){
        if (mPopWindow !=null){
            mPopWindow.dismiss();
        }
    }
}
