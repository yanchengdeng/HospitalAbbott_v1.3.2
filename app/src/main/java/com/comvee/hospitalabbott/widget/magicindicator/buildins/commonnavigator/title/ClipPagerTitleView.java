package com.comvee.hospitalabbott.widget.magicindicator.buildins.commonnavigator.title;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.comvee.hospitalabbott.widget.magicindicator.buildins.UIUtil;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;
import com.comvee.hospitalabbott.widget.magicindicator.buildins.commonnavigator.model.TitleData;


/**
 * 类似今日头条切换效果的指示器标题
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class ClipPagerTitleView extends View implements IMeasurablePagerTitleView {
    private TitleData mTitleData;
    private String mText;
    private String number;
    private int mTextColor;
    private int mClipColor;
    private boolean mLeftToRight;
    private float mClipPercent;

    private Paint mPaint;
    private Paint mNumberPaint;
    private Paint mCirclePaint;
    private Rect mTextBounds = new Rect();
    private Rect numberTextBound;

    private boolean isText;

    public ClipPagerTitleView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        int textSize = UIUtil.dip2px(context, 16);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);

        mNumberPaint = new Paint();
        mNumberPaint.setStyle(Paint.Style.FILL);
        mNumberPaint.setAntiAlias(true);
        mNumberPaint.setColor(Color.WHITE);
        mNumberPaint.setTextSize(UIUtil.dip2px(context, 10));

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.parseColor("#fe633c"));

        numberTextBound = new Rect();
        int padding = UIUtil.dip2px(context, 10);
        setPadding(padding, 0, padding, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureTextBounds();
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result = size;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                int width = mTextBounds.width() + getPaddingLeft() + getPaddingRight();
                result = Math.min(width, size);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = mTextBounds.width() + getPaddingLeft() + getPaddingRight();
                break;
            default:
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result = size;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                int height = mTextBounds.height() + getPaddingTop() + getPaddingBottom();
                result = Math.min(height, size);
                break;
            case MeasureSpec.UNSPECIFIED:
                result = mTextBounds.height() + getPaddingTop() + getPaddingBottom();
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x = (getWidth() - mTextBounds.width()) / 2;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int y = (int) ((getHeight() - fontMetrics.bottom - fontMetrics.top) / 2);

        // 画底层
//        mPaint.setColor(mTextColor);
//        canvas.drawText(mText, x, y, mPaint);
        // 画clip层
//        canvas.save(Canvas.CLIP_SAVE_FLAG);
//        if (mLeftToRight) {//右
//            canvas.clipRect(0, 0, getWidth() * mClipPercent, getHeight());
//        } else {//左
//            canvas.clipRect(getWidth() * (1 - mClipPercent), 0, getWidth(), getHeight());
//        }
//        mPaint.setColor(mClipColor);
//        canvas.drawText(mText, x, y, mPaint);
//        canvas.restore();

        if (!isText) {
            mPaint.setColor(mTextColor);
        } else {
            mPaint.setColor(mClipColor);
        }
        canvas.drawText(mText, x, y, mPaint);
        if (!TextUtils.isEmpty(number) && !number.equals("0")) {
            //获得最长文字的宽度
            mNumberPaint.getTextBounds(number, 0, number.length(), numberTextBound);
            int padding = 6;
            int radius = Math.max(numberTextBound.width() / 2, numberTextBound.height() / 2) + padding;
            int circleX = (getWidth() + mTextBounds.width()) / 2 + radius + padding;
            int circleY = radius + padding * 2;
            canvas.drawCircle(circleX, circleY, radius, mCirclePaint);
            canvas.drawText(number, circleX - numberTextBound.width()/ 2, circleY + numberTextBound.height() / 2, mNumberPaint);
        }
    }

    @Override
    public void onSelected(int index, int totalCount) {
    }

    @Override
    public void onDeselected(int index, int totalCount) {
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
//        Log.e("onLeave 离开", "index = " + index + ", totalCount = " + totalCount);
        if (mTitleData != null && mTitleData.getPosition() == index) {//判断离开的item是否是当前view
            isText = false;
        }

        mLeftToRight = !leftToRight;
        mClipPercent = 1.0f - leavePercent;
        invalidate();
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
//        Log.e("onEnter 进入:", "index = " + index + ", totalCount = " + totalCount);
        if (mTitleData != null && mTitleData.getPosition() == index) {//判断进入的item是否是当前view
            isText = true;
        }
        mLeftToRight = leftToRight;
        mClipPercent = enterPercent;
        invalidate();
    }

    private void measureTextBounds() {
        mPaint.getTextBounds(mText, 0, mText == null ? 0 : mText.length(), mTextBounds);
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
        requestLayout();
    }

    public void setTitleData(TitleData mTitleData) {
        this.mTitleData = mTitleData;
        this.mText = mTitleData.getTitle();
        this.number = mTitleData.getNum();
        requestLayout();
    }

    public float getTextSize() {
        return mPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        mPaint.setTextSize(textSize);
        requestLayout();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidate();
    }

    public int getClipColor() {
        return mClipColor;
    }

    public void setClipColor(int clipColor) {
        mClipColor = clipColor;
        invalidate();
    }

    @Override
    public int getContentLeft() {
        int contentWidth = mTextBounds.width();
        return getLeft() + getWidth() / 2 - contentWidth / 2;
    }

    @Override
    public int getContentTop() {
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 - contentHeight / 2);
    }

    @Override
    public int getContentRight() {
        int contentWidth = mTextBounds.width();
        return getLeft() + getWidth() / 2 + contentWidth / 2;
    }

    @Override
    public int getContentBottom() {
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 + contentHeight / 2);
    }
}
