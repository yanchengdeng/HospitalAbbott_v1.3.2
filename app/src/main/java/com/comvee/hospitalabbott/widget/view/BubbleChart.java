package com.comvee.hospitalabbott.widget.view;

import android.content.Context;
import android.util.AttributeSet;

import com.comvee.hospitalabbott.widget.BubbleChartRenderer;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;

public class BubbleChart extends BarLineChartBase<BubbleData> implements BubbleDataProvider {

    public BubbleChart(Context context) {
        super(context);
    }

    public BubbleChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new BubbleChartRenderer(this, mAnimator, mViewPortHandler);
    }

    public BubbleData getBubbleData() {
        return mData;
    }
}