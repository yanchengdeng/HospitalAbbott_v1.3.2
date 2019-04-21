package com.comvee.hospitalabbott.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class StopAutoScrollView extends ScrollView {
    public StopAutoScrollView(Context context) {
        super(context);
    }

    public StopAutoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StopAutoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
    }
}