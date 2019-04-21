package com.comvee.hospitalabbott.widget.spinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.comvee.hospitalabbott.R;


/**
 * Created by comv098 on 2017/3/10.
 */

public class SpinnerWindow extends PopupWindow {

    private Context mContext;
    private ListView mListView;

    public SpinnerWindow(Context context,
                         AdapterView.OnItemClickListener onItemClickListener) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View mMenuView = inflater.inflate(R.layout.popup_window_spinner, null);
        mListView = (ListView) mMenuView.findViewById(R.id.spinner_list);
//        mListView.setId(getId());
        mListView.setDivider(mContext.getResources().getDrawable(R.drawable.divider));
        mListView.setItemsCanFocus(true);
        mListView.setOnItemClickListener(onItemClickListener);

        // 设置是否允许在外点击使其消失
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.popupWindow_anim_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.spinner_list).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }


    public void setListAdapter(MaterialSpinnerBaseAdapter adapter) {
        mListView.setAdapter(adapter);
    }

}
