package com.comvee.hospitalabbott.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.comvee.hospitalabbott.R;
import com.comvee.hospitalabbott.bean.MemberHistoryModel;
import com.comvee.hospitalabbott.bean.ParamLogListBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by comv098 on 2017/2/6.
 */

public class BrokenLineView extends View {

    //控件的大小
    private int mWidth;
    private int mHeight;

    private Paint xyLinePaint;
    private Paint brokenLineTextPaint;
    private Paint linePaint;
    private Paint brokenLineCirclePaint;
    private Paint brokenTextPaint;

    //坐标轴线条粗细
    private float xyLineWidth;
    //坐标轴线条颜色
    private int xyLineColor;

    //坐标轴字体大小
    private float brokenLineTextSize;
    //坐标轴字体颜色
    private int brokenLineTextColor;

    //折线粗细
    private float lineWidth;
    //折线颜色
    private int lineColor;
    //数值字体颜色
    private int brokenTextColor;

    //圆点半径
    private float brokenLineCircleRadius;
    //圆点颜色
    private int brokenLineCircleColor;

    //x轴的间距
    private float XScale;
    //y轴的间距
    private float YScale;
    //最长文字的宽度
    private float yStrMaxScale;
    private Rect textBound;
    /**
     * 数据
     */
    private String[] xString = new String[]{"", "0AM", "3AM","空腹", "早晨后", "午餐前", "午餐后"
            , "晚餐前", "晚餐后", "睡前", "随机"}; //x坐标轴数据
    private String[] yString = new String[]{"0", "1.0", "2.0", "3.0", "4.0",
            "5.0", "6.0", "7.0", "8.0", "9.0", "10"}; //Y坐标轴数据
    float[] data = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    String[] levelData = new String[11];
    String[] stateData = new String[11];

    //Region用来判断不规则图像的点击事件
    private List<Region> mRegionList;

    private double arrayMax = 10;  //控制Y抽上的空格数，为10格

    private DecimalFormat decimalFormat;
    private double min;
    private double average;

    public BrokenLineView(Context context) {
        this(context, null);
    }

    public BrokenLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrokenLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.BrokenLineView, defStyleAttr, 0);

        xyLineWidth = array.getDimension(R.styleable.BrokenLineView_xyBrokenLineWidth, 1);
        xyLineColor = array.getColor(R.styleable.BrokenLineView_xyBrokenLineColor,
                Color.parseColor("#e3e3ea"));

        brokenLineTextSize = array.getDimension(R.styleable.BrokenLineView_brokenLineTextSize,
                getResources().getDimension(R.dimen.text_13));
        brokenLineTextColor = array.getColor(R.styleable.BrokenLineView_brokenLineTextColor,
                Color.parseColor("#000000"));

        lineWidth = array.getDimension(R.styleable.BrokenLineView_brokenLineWidth, 1);
        lineColor = array.getColor(R.styleable.BrokenLineView_brokenLineColor,
                Color.parseColor("#999999"));

        brokenLineCircleRadius = array.getDimension(R.styleable.BrokenLineView_brokenLineCircleRadius, 8);
        brokenLineCircleColor = array.getColor(R.styleable.BrokenLineView_brokenLineCircleColor,
                Color.parseColor("#44b875"));

        brokenTextColor = array.getColor(R.styleable.BrokenLineView_brokenTextColor, Color.parseColor("#ffffff"));

        array.recycle();
        textBound = new Rect();
        initPaint();


    }

    private void initPaint() {
        xyLinePaint = new Paint();
        xyLinePaint.setAntiAlias(true);
        xyLinePaint.setColor(xyLineColor);
        xyLinePaint.setStrokeWidth(xyLineWidth);
        xyLinePaint.setStyle(Paint.Style.FILL);

        brokenLineTextPaint = new Paint();
        brokenLineTextPaint.setAntiAlias(true);
        brokenLineTextPaint.setColor(brokenLineTextColor);
        brokenLineTextPaint.setTextSize(brokenLineTextSize);
        brokenLineTextPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);

        brokenLineCirclePaint = new Paint();
        brokenLineCirclePaint.setAntiAlias(true);
        brokenLineCirclePaint.setColor(brokenLineCircleColor);
        brokenLineCirclePaint.setStyle(Paint.Style.FILL);

        brokenTextPaint = new Paint();
        brokenTextPaint.setAntiAlias(true);
        brokenTextPaint.setColor(brokenTextColor);
        brokenTextPaint.setTextSize(brokenLineTextSize);
        brokenTextPaint.setStyle(Paint.Style.FILL);

    }

    private void initData() {
        float[] minAndMax = getMaxAndMin(data);
        min = minAndMax[0];
        float max = minAndMax[1];
        average = max > 10 ? (max - min + 1) / 10 : 1;
//        LogUtils.e(" min = " + min + " max = " + max + " average = " + average);


        yString = new String[11];

        int values = 0;
        for (int i = 0; i < 11; i++) {
            if (max <= 10) { //说明跨度区间没有大于11
                yString[i] = (values + i) == 0 ? 0 + "" : decimalFormat.format(values + i);
            } else {
                values = (int) min;
                yString[i] = decimalFormat.format(values + i * average);
            }

        }

        YScale = (float) ((mHeight - getPaddingBottom() - getPaddingTop() - 10) / arrayMax);

//        XScale = (mWidth - getPaddingRight() - getPaddingLeft() - 15) / xString.length;
        XScale = (mWidth - 15) / xString.length;

        //获得最长文字的宽度
        brokenLineTextPaint.getTextBounds(yString[yString.length - 1] + "", 0,
                (yString[yString.length - 1] + "").length(), textBound);
        yStrMaxScale = textBound.width() + 7;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 父容器传过来的宽度的值
        mWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        // 父容器传过来的高度的值
        mHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingLeft()
                - getPaddingRight();

        initData();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        mWidth = (int) (getWidth() - getResources().getDimension(R.dimen.brokenLine_right));
//        mHeight = getHeight();

        drawYStr(canvas);
        drawXStr(canvas);

        drawBrokenLine(canvas);
        drawCircle(canvas);
    }

    /**
     * 绘制折线
     *
     * @param canvas
     */
    private void drawBrokenLine(Canvas canvas) {
        boolean isTo = true;
        Path path = new Path();
        //重置绘制路线，即隐藏之前绘制的轨迹
        path.reset();
        for (int i = 1; i < xString.length; i++) {
            float scale = data[i];
            String levelStr = levelData[i];
            String stateStr = stateData[i];
            if (!TextUtils.isEmpty(levelStr)) {
                double a = (stateStr.equals("2") ? 0.5 : scale - Double.valueOf(yString[0])) / average;
                float y = (float) (mHeight - getPaddingBottom() - (YScale * a));
                if (isTo) {
                    path.moveTo(getPaddingLeft() + (XScale * i) + yStrMaxScale, y);
                    isTo = false;
                } else {
                    path.lineTo(getPaddingLeft() + (XScale * i) + yStrMaxScale, y);
                }
            }
        }
        canvas.drawPath(path, linePaint);
    }


    /**
     * 首先画出小圆点，然后将各个小圆点收尾相连接就是折线效果
     */
    private void drawCircle(Canvas canvas) {
        mRegionList = new ArrayList<>();
        Region circleRegion;//圆的Region
        Path circlePath;
        for (int i = 1; i < data.length; i++) {
            //当显示的数值的个数大于X轴上显示的个数则不做显示效果
//            if (i < xString.length) {
            //因为圆点是以数据值来画得,数据值和Y轴坐标最小值的差就是整个数据的区间;
            float scale = data[i];
            String levelStr = levelData[i];
            String stateStr = stateData[i];
            if (!TextUtils.isEmpty(levelStr)) {
                if (levelStr.equals("1")) {
                    brokenLineCirclePaint.setColor(getResources().getColor(R.color.blue));
                } else if (levelStr.equals("5")) {
                    brokenLineCirclePaint.setColor(getResources().getColor(R.color.red));
                } else if (levelStr.equals("3")) {
                    brokenLineCirclePaint.setColor(getResources().getColor(R.color.green));
                }
                if (stateStr.equals("2")) {
                    brokenLineCirclePaint.setColor(getResources().getColor(R.color.red));
                }

                float x = getPaddingLeft() + (i * XScale) + yStrMaxScale;
                double a = (stateStr.equals("2") ? 0.5 : scale - Double.valueOf(yString[0])) / average;
                float y = (float) (mHeight - getPaddingBottom() - (YScale * a));


                /**
                 * 显示文字
                 */
                String text = stateStr.equals("2") ? "拒" : String.valueOf(data[i]);
                brokenLineTextPaint.getTextBounds(text, 0, text.length(), textBound);
//                canvas.drawText(text,
//                        getPaddingLeft() + (XScale * i + XScale) - textBound.width() / 2,
//                        y - brokenLineCircleRadius,
//                        brokenLineTextPaint);
                drawText(canvas, x, y - brokenLineCircleRadius,
                        textBound.width(), textBound.height(), text);

                /**
                 * 圆点
                 */
                circlePath = new Path();
                circlePath.addCircle(x, y,
                        brokenLineCircleRadius,
                        Path.Direction.CW);

                canvas.drawPath(circlePath, brokenLineCirclePaint);

                //设置区域路径和剪辑描述的区域
                Region region = new Region((int) (x - brokenLineCircleRadius), (int) (y - brokenLineCircleRadius),
                        (int) (x + brokenLineCircleRadius), (int) (y + brokenLineCircleRadius));
                circleRegion = new Region();
                //把圆的path 添加到圆的Region中
                circleRegion.setPath(circlePath, region);
                //记录画出来的圆点
                mRegionList.add(circleRegion);
            }
//            }
        }
    }

    /**
     * 绘制Y轴上的数值与网线
     *
     * @param canvas
     */
    private void drawYStr(Canvas canvas) {

        for (int i = 0; i < yString.length; i++) {
            String text = yString[i];
            brokenLineTextPaint.getTextBounds(text, 0, text.length(), textBound);
            // +textBound.height()/2 主要是为了让字体和间断线居中
            float y = mHeight - getPaddingBottom() - (YScale * i);
            if( i == 10 )  //从下往上，最后一个Y坐标文字位置往下移点
                y = y + 10;
            canvas.drawText(text,
                    getPaddingLeft() + (yStrMaxScale - 7 - textBound.width()),
                    y + textBound.height() / 2,
                    brokenLineTextPaint);

            canvas.drawLine((float) (getPaddingLeft() + yStrMaxScale),
                    y,
//                    (float) (getPaddingLeft() + mWidth - 6.7 - 8.3),
                    mWidth,
                    y,
                    xyLinePaint);
        }

    }

    /**
     * 绘制x轴数值和网线
     *
     * @param canvas
     */
    private void drawXStr(Canvas canvas) {
        // -yStrMaxScale 为X轴留点边界。  /6分成7等分
        int Yspring = (int) getResources().getDimension(R.dimen.line_spacing_height);
        for (int i = 0; i < xString.length; i++) {
            brokenLineTextPaint.getTextBounds(xString[i], 0, xString[i].length(), textBound);

            // -textBound.width()/2 是为了让字体和间断线居中
            int textX = (int) (getPaddingLeft() + (i * XScale) - textBound.height() / 2);
            int textY = mHeight - getPaddingBottom() + textBound.height() / 2 + 4;
            float[] floats = new float[]{
                    textX + yStrMaxScale, textY + Yspring,
                    textX + yStrMaxScale, textY + Yspring * 2,
                    textX + yStrMaxScale, textY + Yspring * 3,
                    textX + yStrMaxScale, textY + Yspring * 4,
                    textX + yStrMaxScale, textY + Yspring * 5
            };
            canvas.drawPosText(xString[i], floats, brokenLineTextPaint);

            // 画间网线
            canvas.drawLine(getPaddingLeft() + (i * XScale) + yStrMaxScale,
                    (float) (getPaddingTop() + 10 - (i == 0 ? 6.7 : 0)),
                    getPaddingLeft() + (i * XScale) + yStrMaxScale,
                    mHeight - getPaddingBottom(), xyLinePaint);
        }
    }


    /**
     * 获得数组的最大值与最小值
     */
    private float[] getMaxAndMin(float[] mData) {
        float[] values = new float[2];
        float min;
        float max;
        min = max = mData[mData.length - 1];
        for (int i = 0; i < mData.length; i++) {
            if (mData[i] != 0) {
                if (mData[i] > max)   // 判断最大值
                    max = mData[i];
                if (mData[i] < min)   // 判断最小值
                    min = mData[i];
            }
        }
        if (max == min) {
            min = max / yString.length;
        }
        if (min == 0.0) {
            min = (float) 1.0;
        }
        values[0] = min;
        values[1] = max;
        return values;
    }

    public void setData(MemberHistoryModel historyModel) {
        this.data = getValue(historyModel);
        this.levelData = getLevel(historyModel);
        this.stateData = getState(historyModel);
        initData();

        postInvalidate();
    }

    private float[] getValue(MemberHistoryModel bean) {
        float[] data = new float[11];
        data[0] = 0;
        List<ParamLogListBean> list = bean.getBeforeDawnList();
        data[1] = list.isEmpty() ? 0 : Float.valueOf(list.get(0).getValue());
        list = bean.getThreeclockList();
        data[2] = list.isEmpty() ? 0 : Float.valueOf(list.get(0).getValue());
        list = bean.getBeforeBreakfast();
        data[3] = list.isEmpty() ? 0 : Float.valueOf(list.get(0).getValue());
        list = bean.getAfterBreakfast();
        data[4] = list.isEmpty() ? 0 : Float.valueOf(list.get(0).getValue());
        list = bean.getBeforeLunch();
        data[5] = list.isEmpty() ? 0 : Float.valueOf(list.get(0).getValue());
        list = bean.getAfterLunch();
        data[6] = list.isEmpty() ? 0 : Float.valueOf(list.get(0).getValue());
        list = bean.getBeforeDinner();
        data[7] = list.isEmpty() ? 0 : Float.valueOf(list.get(0).getValue());
        list = bean.getAfterDinner();
        data[8] = list.isEmpty() ? 0 : Float.valueOf(list.get(0).getValue());
        list = bean.getBeforeSleep();
        data[9] = list.isEmpty() ? 0 : Float.valueOf(list.get(0).getValue());
        list = bean.getRandomtime();
        data[10] = list.isEmpty() ? 0 : Float.valueOf(list.get(0).getValue());
        return data;
    }

    private String[] getLevel(MemberHistoryModel bean) {
        String[] data = new String[11];
        data[0] = "";
        List<ParamLogListBean> list = bean.getBeforeDawnList();
        data[1] = list.isEmpty() ? "" : list.get(0).getLevel();
        list = bean.getThreeclockList();
        data[2] = list.isEmpty() ? "" : list.get(0).getLevel();
        list = bean.getBeforeBreakfast();
        data[3] = list.isEmpty() ? "" : list.get(0).getLevel();
        list = bean.getAfterBreakfast();
        data[4] = list.isEmpty() ? "" : list.get(0).getLevel();
        list = bean.getBeforeLunch();
        data[5] = list.isEmpty() ? "" : list.get(0).getLevel();
        list = bean.getAfterLunch();
        data[6] = list.isEmpty() ? "" : list.get(0).getLevel();
        list = bean.getBeforeDinner();
        data[7] = list.isEmpty() ? "" : list.get(0).getLevel();
        list = bean.getAfterDinner();
        data[8] = list.isEmpty() ? "" : list.get(0).getLevel();
        list = bean.getBeforeSleep();
        data[9] = list.isEmpty() ? "" : list.get(0).getLevel();
        list = bean.getRandomtime();
        data[10] = list.isEmpty() ? "" : list.get(0).getLevel();
        return data;
    }

    private String[] getState(MemberHistoryModel bean) {
        String[] data = new String[11];
        data[0] = "";
        List<ParamLogListBean> list = bean.getBeforeDawnList();
        data[1] = list.isEmpty() ? "" : list.get(0).getStatus();
        list = bean.getThreeclockList();
        data[2] = list.isEmpty() ? "" : list.get(0).getStatus();
        list = bean.getBeforeBreakfast();
        data[3] = list.isEmpty() ? "" : list.get(0).getStatus();
        list = bean.getAfterBreakfast();
        data[4] = list.isEmpty() ? "" : list.get(0).getStatus();
        list = bean.getBeforeLunch();
        data[5] = list.isEmpty() ? "" : list.get(0).getStatus();
        list = bean.getAfterLunch();
        data[6] = list.isEmpty() ? "" : list.get(0).getStatus();
        list = bean.getBeforeDinner();
        data[7] = list.isEmpty() ? "" : list.get(0).getStatus();
        list = bean.getAfterDinner();
        data[8] = list.isEmpty() ? "" : list.get(0).getStatus();
        list = bean.getBeforeSleep();
        data[9] = list.isEmpty() ? "" : list.get(0).getStatus();
        list = bean.getRandomtime();
        data[10] = list.isEmpty() ? "" : list.get(0).getStatus();
        return data;
    }

    private Path pathRound = new Path();

    /**
     * 先绘制数值气泡， 在绘制数值
     *
     * @param canvas
     */
    private void drawText(Canvas canvas, float x, float y, float textWidth, float textHeight, String text) {
        float height = textHeight / 2 + 10;
        float startX = x;
        float startY = y - 30;
        float roundWidth = text.length() > 2 ? (text.length() > 3 ? 30 : 26) : 22;
        pathRound.rewind();
        if (startY - height > 4) {//气泡上方
            pathRound.addRoundRect(new RectF(startX - roundWidth, startY - 15, startX + roundWidth, startY + 15), 15, 15, Path.Direction.CW);
            pathRound.moveTo(startX - 6, startY + 15);
            pathRound.lineTo(startX + 6, startY + 15);
            pathRound.lineTo(startX, startY + 15 + 6);
            pathRound.close();

            canvas.drawPath(pathRound, brokenLineCirclePaint);
            canvas.drawText(text, startX - textWidth / 2, startY + height / 2 - 2, brokenTextPaint);

        } else {//气泡下方
            startY = startY + 45;
            pathRound.addRoundRect(new RectF(startX - roundWidth, startY + 15, startX + roundWidth, startY + 15 * 3), 15, 15, Path.Direction.CW);
            pathRound.moveTo(startX - 6, startY + 15);
            pathRound.lineTo(startX + 6, startY + 15);
            pathRound.lineTo(startX, startY + 15 - 6);
            pathRound.close();
            canvas.drawPath(pathRound, brokenLineCirclePaint);
            canvas.drawText(text, startX - textWidth / 2, startY + height * 2 + 4, brokenTextPaint);
        }

    }

    /**
     *  float width = textWidth / 2 + 20;
     float height = textHeight / 2 + 10;
     float startX = x;
     float startY = y - 30;
     float roundWidth = text.length() > 2 ? 26 : 22;
     pathRound.rewind();
     if (startY - height > 4) {
     pathRound.addRoundRect(new RectF(startX - roundWidth, startY - 15, startX + roundWidth, startY + 15), 15, 15, Path.Direction.CW);
     pathRound.moveTo(startX - 6, startY + 15);
     pathRound.lineTo(startX + 6, startY + 15);
     pathRound.lineTo(startX, startY + 15 + 6);
     pathRound.close();

     canvas.drawPath(pathRound, brokenLineCirclePaint);
     canvas.drawText(text, startX - textWidth / 2, startY + height / 2 - 2, brokenTextPaint);

     } else {
     startY = startY + 45;
     pathRound.addRoundRect(new RectF(startX - roundWidth, startY + 15, startX + roundWidth, startY + 15 * 3), 15, 15, Path.Direction.CW);
     pathRound.moveTo(startX - 6, startY + 15);
     pathRound.lineTo(startX + 6, startY + 15);
     pathRound.lineTo(startX, startY + 15 - 6);
     pathRound.close();
     canvas.drawPath(pathRound, brokenLineCirclePaint);
     canvas.drawText(text, startX - textWidth / 2, startY + height * 2 + 4, brokenTextPaint);
     }

     */

}
