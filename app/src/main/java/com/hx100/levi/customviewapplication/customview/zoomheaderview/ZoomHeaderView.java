package com.hx100.levi.customviewapplication.customview.zoomheaderview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hx100.levi.customviewapplication.customview.zoomheaderview.recyclerviewpager.RecyclerViewPager;
import com.hx100.levi.customviewapplication.customview.zoomheaderview.recyclerviewpager.ViewUtils;
import com.hx100.levi.customviewapplication.utils.LogUtil;
import com.hx100.levi.customviewapplication.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Levi on 2016/12/13.
 */
public class ZoomHeaderView extends LinearLayout {
    public static final String TAG = "tag_ZoomHeaderView";
    //正常的状态
    public static final int STATUS_NORMAL = 1000;
    //滑动到最顶部
    public static final int STATUS_TOP = 1001;
    //滑动到最底部
    public static final int STATUS_BOTTOM = 1002;
    public int status = STATUS_NORMAL;
    private RecyclerViewPager mViewPager;
    private List<OnZoomHeaderScrollListener> mListeners;
    float mLastX;
    float mLastY;
    float mDownX;
    float mDownY;
    private float mTouchSlop;
    //viewpager距离ZoomHeaderView的高度
    public float mViewPagerTop;
    float mViewPagerWidth;
    float mViewPagerHeight;
    float mStartY;
    float mViewWidth;
    float mViewHeight;
    float mMaxY = 200;
    //viewpager的item的margin 方便移动时计算
    private int viewPagerItemMarginHalf = 90;

    public ZoomHeaderView(Context context) {
        this(context, null);
    }

    public ZoomHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
//        LogUtil.i("count=="+getChildCount());
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mListeners = new ArrayList<>();
//        LogUtil.i(TAG,"mTouchSlop=="+mTouchSlop);
    }

    public void setViewPager(RecyclerViewPager viewPager) {
        this.mViewPager = viewPager;
//        setViewPagerOnZoomHeaderScrollListener();
    }

    public void setViewPagerItemMarginHalf(int marginLeft) {
        this.viewPagerItemMarginHalf = marginLeft;
    }

    public RecyclerViewPager getViewPager() {
        return mViewPager;
    }

    public void addOnZoomHeaderScrollListener(OnZoomHeaderScrollListener listener) {
        if (mListeners == null) {
            return;
        }
        this.mListeners.add(listener);
    }

    public interface OnZoomHeaderScrollListener {
        void onZoomHeaderScrollListener(ZoomHeaderView view, float move);

        void onZoomHeaderStatusListener(ZoomHeaderView view, int status);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mViewPager != null) {
            mViewPagerTop = mViewPager.getTop();
            mViewPagerWidth = mViewPager.getWidth();
            mViewPagerHeight = mViewPager.getHeight();
//            LogUtil.i(TAG,"mViewPagerTop=="+mViewPagerTop);
        }
        mStartY = getY();
        mViewWidth = getWidth();
        mViewHeight = getHeight();
//        LogUtil.i(TAG,"scrennheight=="+ ScreenUtils.getScreenHeight(getContext()));
//        LogUtil.i(TAG,"height=="+this.getHeight());
//        LogUtil.i(TAG,"viewpagerheight=="+mViewPager.getHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.i(TAG, "onInterceptTouchEvent==ACTION_DOWN");
                mDownY = ev.getRawY();
                mDownX = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i(TAG, "onInterceptTouchEvent==ACTION_MOVE");
//                LogUtil.i(TAG,"ev.getY()-mDownY"+(ev.getY()-mDownY));
                if (Math.abs(ev.getRawY() - mDownY) > mTouchSlop && Math.abs(ev.getRawX() - mDownX) < mTouchSlop) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.i(TAG, "onInterceptTouchEvent==ACTION_UP");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.i(TAG, "onTouchEvent==ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.i(TAG, "onTouchEvent==ACTION_MOVE");
                float moveY = ev.getRawY() - mDownY;
                float currentY = getY();
//                setTranslationY(currentY + moveY);
//                translationViewPager(this, ev);
//                startScrollListers(moveY);
                setTranslationMove(moveY);
//                LogUtil.i(TAG, "moveY==" + moveY);
//                LogUtil.i(TAG, "currentY==" + currentY);
//                LogUtil.i(TAG, "setTranslationY==" + (currentY + moveY));
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.i(TAG, "onTouchEvent==ACTION_UP");
//                startAnimTranslation(getY() - mStartY);
                setAnimTranslationMove();
                break;
        }
        return super.onTouchEvent(ev);
    }


    public void setTranslationMove(float move) {
        startScrollListers(move);
        if (getY() - mStartY < 0) {//viewpagager处于上部状态
            setTranslationY(move);
            setViewPagerLayoutMove();
            LogUtil.i(TAG,"viewpagager处于上部状态 getY()=="+getY());
            LogUtil.i(TAG,"viewpagager处于上部状态 move=="+move);
        } else {//viewpagager处于在底部的状态
            setTranslationY(move);
        }
//        if (move <= 0) {//向上滑动
//        } else {//向下滑动
//        }
    }

    public void setAnimTranslationMove(){
        if (getY() - mStartY < 0) {//viewpagager处于上部状态
            if (Math.abs(getY() - mStartY)>mMaxY/2){//viewpagager上部状态向上滑动
                startUpObjectAnimator(getY() - mStartY, -mMaxY);
            }else {//viewpagager上部状态向下滑动
                startUpObjectAnimator(getY() - mStartY, 0);
            }
        } else {//viewpagager处于在底部的状态
            if (Math.abs(getY() - mStartY)>mMaxY/2){//viewpagager底部状态向下滑动
                startDownObjectAnimator(getY() - mStartY, mViewHeight);
            }else {//viewpagager底部状态向上滑动
                startDownObjectAnimator(getY() - mStartY, 0);
            }
        }
    }

    private void startUpObjectAnimator(float first, final float last) {
//        ObjectAnimator//
//                .ofFloat(this, "viewPagerLayout", first, last)//
//                .setDuration(500)//
//                .start();
        ValueAnimator animator = ValueAnimator.ofFloat(first, last);
        animator.setTarget(this);
        animator.setDuration(500).start();
//      animator.setInterpolator(value)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                setTranslationY(-(Float) animation.getAnimatedValue());
//                setViewPagerLayout((Float) animation.getAnimatedValue());
                setTranslationMove((Float) animation.getAnimatedValue());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (last == mMaxY) {
                    startStatusListers(STATUS_TOP);
                } else {
                    startStatusListers(STATUS_NORMAL);
                }
            }
        });
    }

    private void startDownObjectAnimator(final float first, final float last) {
//        ObjectAnimator//
//                .ofFloat(this, "viewPagerLayout", first, last)//
//                .setDuration(500)//
//                .start();
        ValueAnimator animator = ValueAnimator.ofFloat(first, last);
        animator.setTarget(this);
        animator.setDuration(500).start();
//      animator.setInterpolator(value)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                setTranslationY((Float) animation.getAnimatedValue());
                setTranslationMove((Float) animation.getAnimatedValue());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (first - last < 0) {
                    startStatusListers(STATUS_BOTTOM);
//                    Toast.makeText(getContext(),"finish",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void startScrollListers(float move) {
        if (mListeners != null && mListeners.size() > 0) {
            for (OnZoomHeaderScrollListener listener : mListeners) {
                listener.onZoomHeaderScrollListener(this, move);
            }
        }
    }

    private void startStatusListers(int status) {
        this.status = status;
        if (mListeners != null && mListeners.size() > 0) {
            for (OnZoomHeaderScrollListener listener : mListeners) {
                listener.onZoomHeaderStatusListener(this, status);
            }
        }
    }


    private void setViewPagerLayoutMove() {
        if (mViewPager == null) {
            return;
        }
//        if (move > mMaxY) {
//            move = mMaxY;
//        }
//        if (move < 0) {
//            return;
//        }
        float move=Math.abs(getY()-mStartY);
        if (move>mMaxY){
            move=mMaxY;
        }
        float left = viewPagerItemMarginHalf / 2;
        left = (move / mMaxY) * left;
        float top = (mViewPagerTop - mMaxY) * move / mMaxY;
        mViewPager.layout(-(int) left * 2, (int) (mViewPagerTop - top), (int) (mViewPagerWidth + left * 2), (int) ((mViewPagerTop - top) + mViewPagerHeight));
//        LogUtil.i(TAG,"mViewPager.getTranslationY()"+mViewPager.getTranslationY());
//        LogUtil.i(TAG,"mViewPager.mViewPager.getY()"+mViewPager.getY());
    }
}
