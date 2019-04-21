package com.comvee.hospitalabbott.widget.popwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.widget.pick.LoopView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by F011512088 on 2017/11/7.
 */

public class OneLoopPopWin extends PopupWindow implements View.OnClickListener {

    private View contentView;//root view
    private View pickerContainerV;
    private Context mContext;

    private TextView cancelBtn;
    private TextView confirmBtn;

    private LoopView mLoopView;
    private List<String> mList;
    private OnItemListener onItemListener;

    public OneLoopPopWin(Builder builder) {
        mList = builder.getData();
        onItemListener = builder.getListener();
        mContext = builder.getContext();
        initView();
    }

    private void initView() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_bloodrange_picker, null);
        pickerContainerV = contentView.findViewById(R.id.container_picker);

        cancelBtn = (TextView) contentView.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
        confirmBtn = (TextView) contentView.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(this);

        mLoopView = (LoopView) contentView.findViewById(R.id.loopView_range);
        mLoopView.setGravity(2);
        if (mList != null) {
            if (mList.size() > 0)
                for (String str : mList) {
                    if (str.length() > 18)
                        mLoopView.setTextSize(16);
                }
            mLoopView.setDataList(mList);
        }
        if (mList.size() > 0) {
            mLoopView.setInitPosition(0);
        }

        setTouchable(true);
        setFocusable(true);
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

    public void setList(List<String> list) {
        mLoopView.setGravity(2);//居中
        this.mList = list;
        if (mLoopView != null && mList != null) {
            mLoopView.setDataList(mList);
        }
        if (mList.size() > 0) {
            mLoopView.setInitPosition(0);
        }
    }

    public void setTextSize(float size) {
        mLoopView.setTextSize(size);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                if (onItemListener != null)
                    onItemListener.onListener(mLoopView.getSelectedItem());
                dismiss();
                break;
        }
    }

    public static class Builder {
        private Context context;
        private OnItemListener listener;
        private List<String> mData = new ArrayList<>();

        public Builder(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        public OnItemListener getListener() {
            return listener;
        }

        public Builder setListener(OnItemListener listener) {
            this.listener = listener;
            return this;
        }

        public List<String> getData() {
            return mData;
        }

        public Builder setData(List<String> mData) {
            this.mData = mData;
            return this;
        }

        public OneLoopPopWin build() {
            return new OneLoopPopWin(this);
        }
    }

    public interface OnItemListener {
        void onListener(int position);
    }

}
