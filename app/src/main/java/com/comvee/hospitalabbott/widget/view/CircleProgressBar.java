package com.comvee.hospitalabbott.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.comvee.hospitalabbott.R;

import java.text.DecimalFormat;


public class CircleProgressBar extends View {
    //血糖值类型
    private static int STYPE_BLOOD_GLUCOSE = 1;
    //历史次数类型
    private static int STYPE_HISTORICAL_TIMES = 2;

    private Context context;
    private Paint paint;
    private Paint paintCircle;
    private Paint bgPaint;
    private Paint textPaint;
    private int textColor;
    private float textSize;
    //用于获取文字的长度或者高度
    private Rect textBound;

    //画笔线宽
    private float mStrokeWidth;
    //填充颜色
    private int progressBarColor;
    //显示类型
    private int displayType;
    //显示的单位
    private String companyStr;
    private Paint companyPaint;

    private int contentWidth = 120;
    private int contentHeight = 120;
    private int defaultBgColor = 0xffefefef;
    private int sweepAngle;
    private int animAngle;
    private ProgressBarAnimation anim;

    /**
     * 开始和结束的颜色
     */
    private int startColor = Color.parseColor("#3CBFF8");
    private int endColor = Color.parseColor("#64E1B5");
    private int intermediateOne = Color.parseColor("#41C6E8");
    private int intermediateTwo = Color.parseColor("#48D0D0");
    private int intermediateThree = Color.parseColor("#54DABD");

    private String textValue = "";
    //
    private float values = 0;
    //当前显示值
    private float current = 0;

    private Paint cicl;

    private int deviation = 20;

    private float maxHigh = 33.4f;
    private float low = 1.1f;

    Bitmap bitmapTop = null;
    Bitmap bitmapRight = null;
    Bitmap noValuesTop = null;
    Bitmap noValuesCenter = null;

    public CircleProgressBar(Context context) {
        this(context, null);
        this.context = context;
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        mStrokeWidth = typedArray.getDimension(R.styleable.CircleProgressBar_circleProgressStrokeWidth, 5);
        defaultBgColor = typedArray.getColor(R.styleable.CircleProgressBar_bgCircleProgressColor, 0xffefefef);
        progressBarColor = typedArray.getColor(R.styleable.CircleProgressBar_circleProgressColor, Color.parseColor("#fe633c"));
        textColor = typedArray.getColor(R.styleable.CircleProgressBar_circleProgressTextColor, getResources().getColor( R.color.black));
        textSize = typedArray.getDimension(R.styleable.CircleProgressBar_circleProgressTextSize, 16);
        displayType = typedArray.getInt(R.styleable.CircleProgressBar_circleProgressDisplayType, 1);

        float companyTextSize = typedArray.getDimension( R.styleable.CircleProgressBar_companyTextSize, 13);

        typedArray.recycle();
        noValuesTop = BitmapFactory.decodeResource(getResources(),  R.drawable.bloodtwo_44);
        noValuesCenter = BitmapFactory.decodeResource(getResources(),  R.drawable.bloodtwo_46);
        anim = new ProgressBarAnimation();

        textBound = new Rect();

        companyPaint = new Paint();
        companyPaint.setAntiAlias(true);
        companyPaint.setStyle(Paint.Style.FILL);
        companyPaint.setTextSize(getResources().getDimension( R.dimen.text_13));
        if (displayType == STYPE_BLOOD_GLUCOSE) {
            deviation = 20;
            companyStr = "mmol/L";
            companyPaint.setColor(Color.parseColor("#999999"));
        } else if (displayType == STYPE_HISTORICAL_TIMES) {
            deviation = 0;
            companyStr = "次";
            sweepAngle = 360;
            companyPaint.setTextSize(companyTextSize);
            companyPaint.setColor(Color.parseColor("#666666"));
        }

        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        paint.setStrokeWidth(mStrokeWidth);//设置画笔线宽
        paint.setStyle(Paint.Style.STROKE);// 空心,只绘制轮廓线
        paint.setStrokeCap(Paint.Cap.ROUND);// 圆角画笔

        /**
         * 进度条颜色渐变
         */
        int[] mColors = new int[]{intermediateOne, intermediateTwo, intermediateThree, endColor, startColor, intermediateOne};
        float[] positions = new float[]{0f, 0.25f, 0.5f, 0.75f, 0.75f, 1f};
        Shader s = new SweepGradient(contentWidth / 2, contentHeight / 2, mColors, positions);
//        paint.setShader(s);
        paint.setColor(progressBarColor);

        bgPaint = new Paint();
        bgPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        bgPaint.setStrokeWidth(mStrokeWidth);//设置画笔线宽
        bgPaint.setStyle(Paint.Style.STROKE);// 空心,只绘制轮廓线
        bgPaint.setColor(defaultBgColor);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);

        if (displayType == STYPE_BLOOD_GLUCOSE) {
            cicl = new Paint();
            cicl.setStrokeWidth(1);
            cicl.setAntiAlias(true);
            cicl.setStyle(Paint.Style.FILL);
            cicl.setColor(getResources().getColor( R.color.white));
            if (type == -1) {
                bitmapTop = BitmapFactory.decodeResource(getResources(), R.drawable.hospital_101);
                bitmapRight = BitmapFactory.decodeResource(getResources(), R.drawable.hospital_105);
                textColor = getResources().getColor(R.color.blue);
            } else if (type == 0) {
                bitmapTop = BitmapFactory.decodeResource(getResources(), R.drawable.hospital_99);
                textColor = getResources().getColor(R.color.green);
                bitmapRight = null;
            } else if (type == 1) {
                bitmapTop = BitmapFactory.decodeResource(getResources(), R.drawable.hospital_97);
                bitmapRight = BitmapFactory.decodeResource(getResources(), R.drawable.hospital_103);
                textColor = getResources().getColor(R.color.red);
            }

            textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(textSize);
            textPaint.setColor(textColor);
            textPaint.setStyle(Paint.Style.FILL);

            paint = new Paint();
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);//抗锯齿
            paint.setStrokeWidth(mStrokeWidth);//设置画笔线宽
            paint.setStyle(Paint.Style.STROKE);// 空心,只绘制轮廓线
            paint.setStrokeCap(Paint.Cap.ROUND);// 圆角画笔
            paint.setColor(textColor);

            paintCircle = new Paint();
            paintCircle.setFlags(Paint.ANTI_ALIAS_FLAG);//抗锯齿
            paintCircle.setStrokeWidth(mStrokeWidth / 2);//设置画笔线宽
            paintCircle.setStyle(Paint.Style.STROKE);// 空心,只绘制轮廓线
            paintCircle.setStrokeCap(Paint.Cap.ROUND);// 圆角画笔
            paintCircle.setColor(textColor);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        contentWidth = getWidth();
        contentHeight = getHeight();

        /**
         * left - The X coordinate of the left side of the rectagle
         * top - The Y coordinate of the top of the rectangle
         * right - The X coordinate of the right side of the rectagle
         * bottom - The Y coordinate of the bottom of the rectangle
         */
        RectF oval = new RectF(0 + mStrokeWidth / 2 + deviation, 0 + mStrokeWidth / 2 + deviation,
                0 + contentWidth - mStrokeWidth / 2 - deviation, 0 + contentHeight - mStrokeWidth / 2 - deviation);
        canvas.drawArc(oval, 0, 360, false, bgPaint);
        //startAngle开始的角度  sweepAngle扫描经过的角度
        canvas.drawArc(oval, 90, sweepAngle, false, paint);

        if (displayType == STYPE_BLOOD_GLUCOSE) {
            if (values > 0) {
                drawBloodGlucose(canvas);
            } else {
                drawRecord(canvas);
            }

        } else if (displayType == STYPE_HISTORICAL_TIMES) {
            drawHistoricalTimes(canvas);
        }

    }

    //画外圈圆弧和
    private void drawCir(Canvas canvas) {

        canvas.translate(contentWidth / 2, contentHeight / 2);
        canvas.rotate(90 + sweepAngle);
        RectF ovalTwo = new RectF(contentWidth / 2 - mStrokeWidth / 2 - 27, 2,
                contentWidth / 2 - mStrokeWidth / 2 - 13, 16);
        canvas.drawArc(ovalTwo, 0, 360, true, paintCircle);

        canvas.drawCircle(contentWidth / 2 - mStrokeWidth / 2 - 20, 9, 4, cicl);

    }


    //画历史
    private void drawHistoricalTimes(Canvas canvas) {
        companyPaint.getTextBounds(companyStr, 0, companyStr.length(), textBound);
        int companyStrWidth = textBound.width();

        textPaint.getTextBounds(textValue, 0, textValue.length(), textBound);
        int maxWidth = textBound.width() + companyStrWidth;
        //值
        canvas.drawText(textValue, (contentWidth - mStrokeWidth - maxWidth) / 2,
                (contentHeight - mStrokeWidth + textBound.height()) / 2, textPaint);

        //单位
        canvas.drawText(companyStr, (contentWidth - mStrokeWidth + textBound.width() - companyStrWidth) / 2 + 4,
                (contentHeight - mStrokeWidth + textBound.height()) / 2, companyPaint);

    }

    //画血糖值
    private void drawBloodGlucose(Canvas canvas) {
//        textValue = current + "";
//        textPaint.getTextBounds(textValue, 0, textValue.length(), textBound);
//
//        canvas.drawBitmap(bitmapTop, (contentWidth - mStrokeWidth - bitmapTop.getWidth() / 2) / 2, mStrokeWidth + 13 + deviation, new Paint());
//
//        canvas.drawBitmap(noValuesCenter, (contentWidth - mStrokeWidth - noValuesCenter.getWidth()) / 2,
//                (contentHeight - mStrokeWidth + noValuesCenter.getHeight()) / 2, new Paint());
//
//        //绘制单位文字
//        companyPaint.getTextBounds(companyStr, 0, companyStr.length(), textBound);
//        canvas.drawText(companyStr, (contentWidth - mStrokeWidth - textBound.width() + deviation - 10) / 2,
//                contentHeight - (mStrokeWidth + textBound.height()) / 2 - 16 - deviation, companyPaint);
        textValue = current + "";
        textPaint.getTextBounds(textValue, 0, textValue.length(), textBound);

        canvas.drawBitmap(bitmapTop, (contentWidth - mStrokeWidth - bitmapTop.getWidth() / 2) / 2, mStrokeWidth + 13 + deviation, new Paint());

        canvas.drawText(textValue, (contentWidth - mStrokeWidth - textBound.width()) / 2,
                (contentHeight - mStrokeWidth + textBound.height()) / 2, textPaint);
        if (bitmapRight != null)
//            canvas.drawBitmap(bitmapRight, (contentWidth - mStrokeWidth + textBound.width()) / 2 + 4,
//                    (contentHeight - mStrokeWidth + textBound.height()) / 2, new Paint());
            canvas.drawBitmap(bitmapRight, (contentWidth - mStrokeWidth + textBound.width()) / 2 + bitmapRight.getWidth(),
                    (contentHeight - mStrokeWidth + textBound.height()) / 2 - bitmapRight.getHeight(), new Paint());


        //绘制单位文字
        companyPaint.getTextBounds(companyStr, 0, companyStr.length(), textBound);
        canvas.drawText(companyStr, (contentWidth - mStrokeWidth - textBound.width() + deviation - 10) / 2,
                contentHeight - (mStrokeWidth + textBound.height()) / 2 - 16 - deviation, companyPaint);

        drawCir(canvas);

    }

    //画没有值时的血糖记录
    private void drawRecord(Canvas canvas) {

        canvas.drawBitmap(noValuesTop, (contentWidth - mStrokeWidth - noValuesTop.getWidth() / 2) / 2, mStrokeWidth + 13 + deviation, new Paint());

        canvas.drawBitmap(noValuesCenter, (contentWidth - mStrokeWidth - noValuesCenter.getWidth()) / 2 + 6,
                (contentHeight - mStrokeWidth - noValuesCenter.getHeight()) / 2, new Paint());

        //绘制单位文字
        companyPaint.getTextBounds(companyStr, 0, companyStr.length(), textBound);

        canvas.drawText(companyStr, (contentWidth - mStrokeWidth - textBound.width() + deviation - 10) / 2,
                contentHeight - (mStrokeWidth + textBound.height()) / 2 - 16 - deviation, companyPaint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(getMeasuredWidth() / 2, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, getMeasuredHeight() / 2);
        }
    }

    /**
     * 进度条动画
     *
     * @author Administrator
     */
    public class ProgressBarAnimation extends Animation {
        public ProgressBarAnimation() {

        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);
//            LogUtils.e("ago - sweepAngle = "+sweepAngle);
            sweepAngle = (int) (interpolatedTime * animAngle);
            //设置值变化
            if (current < values) {
                current += current + 0.5;
            } else if (current > values) {
                current = values;
            }
            postInvalidate();
        }
    }

    private int type = 0; //0正常，-1偏低，1，偏高

    public void setProgress(float data, int type) {
        this.type = type;
        DecimalFormat decimalFormat = new DecimalFormat(".0");
        values = Float.valueOf(decimalFormat.format(data));
        initPaint();
        float progress = values / maxHigh;
        animAngle = (int) (progress * 360f);
        int time = (int) (progress * 4000);
        postInvalidate();

        anim.setDuration(time);
        this.startAnimation(anim);
    }


    public void setProgress(float data) {
        DecimalFormat decimalFormat = new DecimalFormat(".0");
        values = Float.valueOf(decimalFormat.format(data));
        if (values < low) {
            type = -1;
        } else if (values > maxHigh) {
            type = 1;
        } else {
            type = 0;
        }
        initPaint();

        float progress = values / maxHigh;
        animAngle = (int) (progress * 360f);
        int time = (int) (progress * 4000);

        postInvalidate();

        anim.setDuration(time);
        this.startAnimation(anim);
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
        initPaint();
        postInvalidate();
    }

    public String getTextValue() {
        return textValue;
    }
}