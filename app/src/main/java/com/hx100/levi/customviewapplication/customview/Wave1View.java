package com.hx100.levi.customviewapplication.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hx100.levi.customviewapplication.utils.LogUtil;

/**
 * 波浪view
 * Created by Levi on 2016/11/7.
 */
public class Wave1View extends View{
    int mWidth=500;
    int mHeight=250;
    int mRectWidth;
    int mRectHeight;
    float mRadius=25;
    Paint mPaint;
    int mColor=0XFFFFFFFF;
    RectF mRectFLeft;
    RectF mRectFRight;
    int mCount;
    public Wave1View(Context context) {
        this(context, null);
    }

    public Wave1View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Wave1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关掉硬件加速
        mRectFLeft=new RectF();
        mRectFRight=new RectF();
        Drawable drawable=getBackground();
        if (drawable instanceof ColorDrawable){
            mColor=((ColorDrawable) drawable).getColor();
        }
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
        setBackgroundDrawable(null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode==MeasureSpec.EXACTLY){
            mWidth=widthSize;
        }
        if (heightMode==MeasureSpec.EXACTLY){
            mHeight=heightSize;
        }
        mRectHeight=mHeight;
        mRectWidth=mWidth;
        mCount=mHeight/(int)(mRadius*2);
        if (mCount%2==0){
            mCount++;
        }
        mRadius=mHeight/mCount/2;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas);
        drawSemiCircle(canvas);
    }

    /**
     * 绘制半圆
     * @param canvas
     */
    private void drawSemiCircle(Canvas canvas) {
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        for (int i=0;i<mCount;i++){
            if (i%2!=0){
                mRectFLeft.left=0-mRadius;
                mRectFLeft.top=i*mRadius*2;
                mRectFLeft.right=mRectFLeft.left+mRadius*2;
                mRectFLeft.bottom=mRectFLeft.top+mRadius*2;
                canvas.drawArc(mRectFLeft, 270, 180, true, mPaint);
            }
        }
        for (int i=0;i<mCount;i++){
            if (i%2!=0){
                mRectFRight.left=mRectWidth-mRadius;
                mRectFRight.top=i*mRadius*2;
                mRectFRight.right=mRectFRight.left+mRadius*2;
                mRectFRight.bottom=mRectFRight.top+mRadius*2;
                canvas.drawArc(mRectFRight, 90, 180, true, mPaint);
            }
        }
    }

    private void drawRect(Canvas canvas) {
        canvas.drawRect(0,0,mRectWidth,mRectHeight,mPaint);
    }
}
