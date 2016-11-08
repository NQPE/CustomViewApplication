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
public class CirclePercentView extends View {
    Paint mBackCirclePaint;
    Paint mPercentCirclePaint;
    Paint mPercentTextPaint;
    int mBackCircleColor;
    int mPercentCircleColor;
    int mPercentCircleWidth;
    int mPercentTextSize;
    int mPercentTextColor;
    int mWidth = 300;
    int mHeight = 300;
    int mRadius;
    RectF mRectf;
    int mPercentIndex;//百分比

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView);
        mBackCircleColor = a.getColor(R.styleable.CirclePercentView_back_circle_color, Color.parseColor("#745AAB"));
        mPercentCircleColor = a.getColor(R.styleable.CirclePercentView_percent_circle_color, Color.parseColor("#B8BBDE"));
        mPercentTextColor = a.getColor(R.styleable.CirclePercentView_percent_text_color, Color.parseColor("#ffffff"));
        mPercentCircleWidth = a.getDimensionPixelSize(R.styleable.CirclePercentView_percent_circle_width, 30);
        mPercentTextSize = a.getDimensionPixelSize(R.styleable.CirclePercentView_percent_text_size, 96);
        mPercentIndex = a.getInteger(R.styleable.CirclePercentView_percent_index, 96);
        if (mPercentIndex > 100) {
            mPercentIndex = 100;
        }
        if (mPercentIndex < 0) {
            mPercentIndex = 0;
        }
        a.recycle();
        initPaint();
        setPercentIndexByAnimation(mPercentIndex);
    }

    private void initPaint() {
        mBackCirclePaint = new Paint();
        mBackCirclePaint.setAntiAlias(true);
        mBackCirclePaint.setStyle(Paint.Style.FILL);
        mBackCirclePaint.setColor(mBackCircleColor);
        mPercentTextPaint = new Paint();
        mPercentTextPaint.setAntiAlias(true);
        mPercentTextPaint.setStyle(Paint.Style.STROKE);
        mPercentTextPaint.setTextSize(mPercentTextSize);
        mPercentTextPaint.setColor(mPercentTextColor);
        mPercentCirclePaint = new Paint();
        mPercentCirclePaint.setAntiAlias(true);
        mPercentCirclePaint.setStyle(Paint.Style.STROKE);
        mPercentCirclePaint.setColor(mPercentCircleColor);
        mPercentCirclePaint.setStrokeWidth(mPercentCircleWidth);
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
        if (mPercentCircleWidth > mWidth / 3) {
            mPercentCircleWidth = mWidth / 3;
            mPercentCirclePaint.setStrokeWidth(mPercentCircleWidth);
        }
        mRectf = new RectF(mPercentCircleWidth / 2, mPercentCircleWidth / 2, mWidth - mPercentCircleWidth / 2, mHeight - mPercentCircleWidth / 2);
        mRadius = mWidth / 2;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackCircle(canvas);
        drawPercentCircle(canvas);
        drawPercentText(canvas);
    }

    private void drawPercentText(Canvas canvas) {
        int len = (int) mPercentTextPaint.measureText(mPercentIndex + "%");
        int textheight= (int) (mPercentTextPaint.ascent()+mPercentTextPaint.descent());
        canvas.drawText(mPercentIndex + "%", mWidth / 2 - len / 2, mHeight / 2 -textheight/2, mPercentTextPaint);
    }

    private void drawPercentCircle(Canvas canvas) {
        int degrees = mPercentIndex * 360 / 100;
        canvas.drawArc(mRectf, 270, degrees, false, mPercentCirclePaint);
    }

    private void drawBackCircle(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBackCirclePaint);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        setPercentIndexByAnimation(mPercentIndex);
    }

    public void setPercentIndex(int index) {
        if (index > 100) {
            index = 100;
        }
        if (index < 0) {
            index = 0;
        }
        mPercentIndex = index;
        invalidate();
    }

    public void setPercentIndexByAnimation(int index) {
        if (index > 100) {
            index = 100;
        }
        if (index < 0) {
            index = 0;
            setPercentIndex(0);
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
