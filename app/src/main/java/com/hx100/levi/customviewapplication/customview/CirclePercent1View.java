package com.hx100.levi.customviewapplication.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hx100.levi.customviewapplication.R;

/**
 * 圆形百分比（进度条）
 * Created by Levi on 2016/11/8.
 */
public class CirclePercent1View extends View {
    Paint mPaint;
    int mBackCircleColor = Color.parseColor("#5EB1ED");
    int mPercentCircleColor = Color.parseColor("#7190A9");
    int mPointColor = Color.parseColor("#CADFEF");
    int mBigStrokeWidth;
    int mMiddleStrokeWidth;
    int mSmallStrokeWidth;
    int mWidth = 300;
    int mHeight = 300;
    int mRadius;
    RectF mRectfBig;
    RectF mRectfMiddle;
    int mPercentIndex = 32;//百分比

    public CirclePercent1View(Context context) {
        this(context, null);
    }

    public CirclePercent1View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercent1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mRectfBig = new RectF();
        mRectfMiddle = new RectF();
        initPaint();
        setPercentIndexByAnimation(mPercentIndex);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }
        mWidth = Math.min(mWidth, mHeight);
        mHeight = mWidth;
        mRadius = mWidth / 2;
        mBigStrokeWidth = mRadius / 4;
        mMiddleStrokeWidth = mRadius / 30;
        mSmallStrokeWidth = mRadius / 70;
        mRectfBig.left = mSmallStrokeWidth / 2;
        mRectfBig.top = mSmallStrokeWidth / 2;
        mRectfBig.right = mRectfBig.left + mWidth - mSmallStrokeWidth;
        mRectfBig.bottom = mRectfBig.top + mHeight - mSmallStrokeWidth;
        mRectfMiddle.left = mRectfBig.left + mRadius / 3;
        mRectfMiddle.top = mRectfBig.top + mRadius / 3;
        mRectfMiddle.right = mRectfBig.right - mRadius / 3;
        mRectfMiddle.bottom = mRectfBig.bottom - mRadius / 3;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBack1(canvas);
        drawBack2(canvas);
        drawBack3(canvas);
        drawPoint(canvas);
        drawPercent(canvas);
    }

    private void drawPercent(Canvas canvas) {
        mPaint.setStrokeWidth(mBigStrokeWidth);
        mPaint.setColor(mPercentCircleColor);
        float degrees=270 * mPercentIndex / 100+10;
        if (degrees==10){
            degrees=0;
        }else if (degrees==280){
            degrees=290;
        }
        canvas.drawArc(mRectfMiddle, 55, -degrees, false, mPaint);
    }

    private void drawPoint(Canvas canvas) {
        mPaint.setStrokeWidth(mSmallStrokeWidth);
        mPaint.setColor(mPointColor);
        double degrees = 270 * mPercentIndex / 100 - 45;
        float startX = (float) (mRadius / 10 * Math.cos(degrees * Math.PI / 180)) + mWidth / 2;
        float startY = (float) -(mRadius / 10 * Math.sin(degrees * Math.PI / 180)) + mHeight / 2;
        float endX = (float) (mRadius * 25 / 48 * Math.cos(degrees * Math.PI / 180)) + mWidth / 2;
        float endY = (float) -(mRadius * 25 / 48 * Math.sin(degrees * Math.PI / 180)) + mHeight / 2;
        canvas.drawLine(startX, startY, endX, endY, mPaint);
    }

    private void drawBack3(Canvas canvas) {
        mPaint.setStrokeWidth(mSmallStrokeWidth);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius / 6, mPaint);
        mPaint.setStrokeWidth(mMiddleStrokeWidth);
        mPaint.setColor(mPointColor);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius / 10, mPaint);
    }

    private void drawBack2(Canvas canvas) {
        mPaint.setStrokeWidth(mBigStrokeWidth);
        canvas.drawArc(mRectfMiddle, 125, 290, false, mPaint);
    }

    /**
     * 绘制最外围背景
     *
     * @param canvas
     */
    private void drawBack1(Canvas canvas) {
        mPaint.setColor(mBackCircleColor);
        mPaint.setStrokeWidth(mSmallStrokeWidth);
        canvas.drawArc(mRectfBig, (float) 134.7, (float) 270.6, false, mPaint);

        canvas.save();
        canvas.rotate((float) -135, mWidth / 2, mHeight / 2);
        for (int i = 0; i < 13; i++) {
            canvas.drawLine(mWidth / 2, mRectfBig.top, mWidth / 2, mRadius / 10, mPaint);
            canvas.rotate((float) 22.5, mWidth / 2, mHeight / 2);
        }
        canvas.restore();
    }

    public void setPercentIndex(int index){
        if (index > 100) {
            index = 100;
        }
        if (index < 0) {
            index = 0;
        }
        mPercentIndex=index;
        invalidate();
    }

    public void setPercentIndexByAnimation(int index) {
        if (index > 100) {
            index = 100;
        }
        if (index < 0) {
            index = 0;
            setPercentIndex(index);
            return;
        }
        ObjectAnimator
                .ofInt(this, "percentIndex", 0, index)
                .setDuration(2000*index/100)
                .start();
    }

    public int getPercentIndex(){
        return mPercentIndex;
    }
}
