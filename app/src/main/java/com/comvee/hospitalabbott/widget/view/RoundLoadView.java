package com.comvee.hospitalabbott.widget.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.connector.AnimationListener;


/**
 * Created by F011512088 on 2017/12/28.
 */

public class RoundLoadView extends View {

    private static final String TAG = RoundLoadView.class.getSimpleName();
    private final int state_round = 0;//椭圆状态
    private final int state_shrink = 1;//缩小状态
    private final int state_circle = 2; //加载状态
    private final int state_stretch = 3;//伸开状态

    private int width = 300;
    private int height = 50;

    private int elasticWidth;//缩放的长度 = 椭圆的2分之一宽度 - 加载圆的半径

    private Paint mOvalPaint; //椭圆画笔
    private Paint mCirclePaint;//加载动态画笔
    private Paint mCircleStrokePaint;//加载动态画笔
    private Paint mElasticPaint;//扩散缩小动画画笔
    private Paint mTextPaint; //文字画笔

    private float roundWidth;
    private float roundHeight;
    private int roundColor;

    private float roundStrokeWidth;
    private int roundStrokeColor;

    private int circleColor;
    private float circleStrokeWidth;
    private int circleStrokeColor;

    private String text;
    private float textSize;
    private int textColor;

    private int state = state_round;

    private int duration = 800;
    private float distance = 0;//当前动画执行的百分比取值为0-1
    private int repeatCount = 0;//加载执行次数, 执行一次1s，重复执行10,10s后网络自动断开

    private ValueAnimator valueAnimator;
    private ValueAnimator.AnimatorUpdateListener updateListener;
    private ValueAnimator.AnimatorListener listener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (state) {
                case state_shrink:
                    state = state_circle;
                    valueAnimator.setDuration(1000);
                    valueAnimator.start();
                    break;
                case state_circle:
                    ++repeatCount;
                    if (repeatCount < 10) {
                        valueAnimator.start();
                    } else {
                        state = state_stretch;
                        valueAnimator.setDuration(duration);
                        valueAnimator.start();
                    }
                    break;
                case state_stretch:
                    state = state_round;
                    stopAnimator();
                    invalidate();
                    break;
            }
        }
    };

    public RoundLoadView(Context context) {
        this(context, null);
    }

    public RoundLoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs);
    }

    private void initData(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundLoadView);

        roundStrokeWidth = ta.getDimension(R.styleable.RoundLoadView_round_stroke_width, 0);
        roundStrokeColor = ta.getColor(R.styleable.RoundLoadView_round_stroke_color, Color.parseColor("#ffffff"));

        roundWidth = ta.getDimension(R.styleable.RoundLoadView_round_width, width);
        roundHeight = ta.getDimension(R.styleable.RoundLoadView_round_height, height - roundStrokeWidth * 2);
        roundColor = ta.getColor(R.styleable.RoundLoadView_round_color, Color.parseColor("#0099ff"));

        circleColor = ta.getColor(R.styleable.RoundLoadView_circle_color, Color.parseColor("#0099ff"));
        circleStrokeWidth = ta.getDimension(R.styleable.RoundLoadView_circle_stroke_width, 2);
        circleStrokeColor = ta.getColor(R.styleable.RoundLoadView_circle_stroke_color, Color.parseColor("#ffffff"));

        text = ta.getString(R.styleable.RoundLoadView_round_load_text);
        textSize = ta.getDimension(R.styleable.RoundLoadView_round_load_text_size, 17);
        textColor = ta.getColor(R.styleable.RoundLoadView_round_load_text_color, Color.parseColor("#ffffff"));

        initPaint();
        initAnimator();
    }

    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(circleColor);

        mCircleStrokePaint = new Paint();
        mCircleStrokePaint.setStyle(Paint.Style.STROKE);
        mCircleStrokePaint.setAntiAlias(true);
        mCircleStrokePaint.setColor(circleStrokeColor);

        mElasticPaint = new Paint();
        mElasticPaint.setStyle(Paint.Style.FILL);
        mElasticPaint.setAntiAlias(true);
        mElasticPaint.setColor(roundColor);

        mOvalPaint = new Paint();
        mOvalPaint.setStyle(Paint.Style.FILL);
        mOvalPaint.setAntiAlias(true);
        mOvalPaint.setColor(roundColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
    }

    private void initAnimator() {
        updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                distance = (float) animation.getAnimatedValue();
//                Log.d("updateListener", "distance = " + distance);
                invalidate();
            }
        };
        listener = new AnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d(TAG, "--动画重复--");
            }
        };
        valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(duration);
        valueAnimator.addUpdateListener(updateListener);
        valueAnimator.addListener(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {//填充父窗体或者是指定宽度
            // Parent has told us how big to be. So be it.
            width = getMeasuredWidth();
        }
        if (heightMode == MeasureSpec.EXACTLY) {//填充父窗体或者是指定高度
            // Parent has told us how big to be. So be it.
            height = getMeasuredHeight();
        }
        setMeasuredDimension(width, height);
        elasticWidth = (width - height) / 2;
//        Log.d(TAG, "width = " + width + ", height = " + height + " elasticWidth = " + elasticWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(width / 2, height / 2);
        switch (state) {
            case state_round:
                canvasRound(canvas);
                canvasText(canvas, true);
                break;
            case state_shrink:
                canvasText(canvas, false);
                canvasElastic(canvas, true);
                break;
            case state_circle:
                canvasCircle(canvas);
                break;
            case state_stretch:
                canvasText(canvas, false);
                canvasElastic(canvas, false);
                break;
        }
    }

    /**
     * 加载的圆形
     */
    private void canvasCircle(Canvas canvas) {
        int radius = height / 2;
        canvas.drawCircle(0, 0, radius, mCirclePaint);
        int rectSize = radius - 10;
        RectF rectF = new RectF(-rectSize, -rectSize, rectSize, rectSize);
        canvas.drawArc(rectF, distance * 360, 320, false, mCircleStrokePaint);
    }

    /**
     * 椭圆缩成圆形或圆形扩展为椭圆
     *
     * @param canvas
     * @param isElastic 判断缩小、伸展动画, true缩小，false伸展
     */
    private void canvasElastic(Canvas canvas, boolean isElastic) {
        float top;
        if (isElastic) top = width / 2 - elasticWidth * distance;
        else
            top = elasticWidth * distance + height / 2;
        RectF rect = new RectF(-top, -height / 2, top, height / 2);
        canvas.drawRoundRect(rect, height / 2, height / 2, mOvalPaint);
    }

    /**
     * 画圆角矩形
     *
     * @param canvas
     */
    private void canvasRound(Canvas canvas) {
        RectF rectF = new RectF(-width / 2, -height / 2, width / 2, height / 2);
        canvas.drawRoundRect(rectF, height / 2, height / 2, mOvalPaint);
    }

    /**
     * 描绘文字
     *
     * @param isElastic 判断缩小、伸展动画, true缩小，false伸展
     */
    private void canvasText(Canvas canvas, boolean isElastic) {
        Rect textBound = new Rect();
        mTextPaint.getTextBounds(text, 0,
                text.length(), textBound);
        float topX = textBound.width() / 2;
        float topY = textBound.height() / 2;
        mTextPaint.setAlpha((int) (((isElastic ? 1 - distance : distance)) * 255));//参数a为透明度，取值范围为0~255，数值越小越透明。
        canvas.drawText(text, -topX, topY, mTextPaint);
    }

    public void setState(int state) {
        this.state = state;
        if (valueAnimator.isRunning())
            valueAnimator.cancel();
        invalidate();
    }

    public boolean isRunning() {
        return valueAnimator.isRunning();
    }

    public void startAnimator() {
        state = state_shrink;
        valueAnimator.start();
    }

    public void stopAnimator() {
        if (valueAnimator.isRunning())
            valueAnimator.cancel();
        state = state_round;
        repeatCount = 0;
        distance = 0;
        invalidate();
    }

}
