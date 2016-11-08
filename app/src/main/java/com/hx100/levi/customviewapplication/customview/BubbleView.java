package com.hx100.levi.customviewapplication.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hx100.levi.customviewapplication.R;

/**
 * Created by Levi on 2016/11/7.
 */
public class BubbleView extends View{
    Paint mPaint;
    int bubble_color;//背景颜色
    int mHeight=200;
    int mWidth=200;
    int mRectWidth;
    int mRectHeight;
    int mTriangleHeight;
    int mTriangleWidth;
    Path mTrianglePath;
    public BubbleView(Context context) {
        this(context,null);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint=new Paint();
        mTrianglePath=new Path();
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.BubbleView);
        bubble_color=typedArray.getColor(R.styleable.BubbleView_bubble_color, Color.parseColor("#ffffff"));
        mPaint.setColor(bubble_color);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode==MeasureSpec.EXACTLY){
            mWidth=widthSize;
        }
        if (heightMode==MeasureSpec.EXACTLY){
            mHeight=heightSize;
        }
        mRectHeight=mHeight*80/100;
        mTriangleHeight=(mHeight-mRectHeight)*90/100;
        mRectWidth=mWidth*95/100;
        mTriangleWidth= (int) (2*1.73*mTriangleHeight);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas);
        drawTriangle(canvas);
    }

    /**
     * 画矩形
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
        int left=(mWidth-mRectWidth)/2;
        int top=(mHeight-mRectHeight-mTriangleHeight)/2;
        RectF rectF=new RectF(left,top,left+mRectWidth,top+mRectHeight);
        canvas.drawRoundRect(rectF, 20, 20, mPaint);
    }

    /**
     * 画三角
     * @param canvas
     */
    private void drawTriangle(Canvas canvas) {
        int startX=(mWidth-mRectWidth)/2+(mRectWidth-mTriangleWidth)/2;
        int startY=(mHeight-mRectHeight-mTriangleHeight)/2+mRectHeight;
        mTrianglePath.moveTo(startX,startY);
        mTrianglePath.lineTo(startX+mTriangleWidth/2,startY+mTriangleHeight);
        mTrianglePath.lineTo(startX+mTriangleWidth,startY);
        mTrianglePath.close();
        canvas.drawPath(mTrianglePath,mPaint);
    }
}
