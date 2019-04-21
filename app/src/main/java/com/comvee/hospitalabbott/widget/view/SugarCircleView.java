package com.comvee.hospitalabbott.widget.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class SugarCircleView extends View {
    Paint pIn, pOut;
    private float pInWidth, pOutWidth;
    private int padding;
    private int circleCount;
    /**
     * 属性动画
     */
    private ObjectAnimator objectAnimator;
    private boolean isClick = false;

    public SugarCircleView(Context context) {
        super(context);
        init();
    }

    public SugarCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SugarCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isClick = true;
        }
        return false;
    }

    private void init() {
        padding = 8;
        pIn = new Paint();
        pOut = new Paint();
        //消除锯齿
        pIn.setAntiAlias(true);
        pOut.setAntiAlias(true);
        //空心
        pIn.setStyle(Paint.Style.STROKE);
        pOut.setStyle(Paint.Style.STROKE);

//        postInvalidate();
        pIn.setStrokeWidth(5);
        pOut.setStrokeWidth(10);

        pIn.setColor(Color.parseColor("#ecedf4"));
        pOut.setColor(Color.parseColor("#ff7b5a"));
//      pOut.setShadowLayer(0, 0, 0, Color.BLACK);
        /**
         * 设置描边效果
         */
        pOut.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.SOLID));
        pIn.setMaskFilter(new BlurMaskFilter(2, BlurMaskFilter.Blur.SOLID));
        /**
         * 设置关闭硬件加速才能有 描边效果
         */
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        pInWidth = pIn.getStrokeWidth();
        pOutWidth = pOut.getStrokeWidth();

        pOut.setStrokeJoin(Paint.Join.ROUND);
        pIn.setStrokeJoin(Paint.Join.ROUND);

    }

    /**
     * @param c1 -1 不设置
     * @param c2 -1 不设置
     */
    public void setColor(int c1, int c2) {
        if(c1 != -1) {
            pIn.setColor(c1);
        }
        if(c2 != -1) {
            pOut.setColor(c2);
        }
    }

    public void setWidth(int w1, int w2) {
        pIn.setStrokeWidth(w1);
        pOut.setStrokeWidth(w2);
    }

    public void setCircle(int circleCount) {
        this.circleCount = circleCount;

        postInvalidate();
    }

    public void setCircleAnim(int circleCount, boolean isAnim){
        if(!isAnim){
            setCircle(circleCount);
            return;
        }
        if(objectAnimator !=null) {//正在播放动画先停止
            if (objectAnimator.isRunning())
                objectAnimator.end();
        }
        if(objectAnimator == null){//未初始化时先初始化
            objectAnimator = ObjectAnimator.ofInt(this,"circle",this.circleCount,circleCount);
            objectAnimator.setDuration(1000);
        }
        Log.e("IndexSugarCircleView","this.circleCount :"+ this.circleCount + "   circleCount: " + circleCount);
        objectAnimator.setIntValues(this.circleCount,circleCount);
        objectAnimator.start();
    }

    public int getCircle(){
        return circleCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF1 = new RectF(pInWidth / 2 + padding, pInWidth / 2 + padding, getWidth() - pInWidth / 2 - padding, getHeight() - pInWidth / 2 - padding);
        canvas.drawArc(rectF1, 270, 360, false, pIn);

        RectF rectF2 = new RectF(pOutWidth / 2 + padding, pOutWidth / 2 + padding, getWidth() - pOutWidth / 2 - padding, getHeight() - pOutWidth / 2 - padding);
        canvas.drawArc(rectF2, 270, circleCount, false, pOut);
    }


}
