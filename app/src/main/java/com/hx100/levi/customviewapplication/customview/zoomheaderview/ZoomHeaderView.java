package com.hx100.levi.customviewapplication.customview.zoomheaderview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hx100.levi.customviewapplication.R;
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
    public float mDownY;
    public float mDownGetY;
    private float mTouchSlop;
    //viewpager距离ZoomHeaderView的高度
    public float mViewPagerTop;
    float mViewPagerWidth;
    float mViewPagerHeight;
    public float mStartY;
    float mViewWidth;
    float mViewHeight;
    float mMaxY = 200;
    //viewpager的item的margin 方便移动时计算
    private int viewPagerItemMarginHalf = 90;

    float mCurrentTranslationY;

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
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mListeners = new ArrayList<>();
    }

    public void setViewPager(RecyclerViewPager viewPager) {
        this.mViewPager = viewPager;
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

    boolean isFirst=true;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isFirst){
            if (mViewPager != null) {
                mViewPagerTop = mViewPager.getTop();
                mViewPagerWidth = mViewPager.getWidth();
                mViewPagerHeight = mViewPager.getHeight();
            LogUtil.i(TAG,"mViewPagerTop=="+mViewPagerTop);
            LogUtil.i(TAG,"mViewPagerWidth=="+mViewPagerWidth);
            LogUtil.i(TAG,"mViewPagerHeight=="+mViewPagerHeight);
            }
            mStartY = getY();
            mViewWidth = getWidth();
            mViewHeight = getHeight();
            LogUtil.i(TAG,"mViewWidth=="+mViewWidth);
            LogUtil.i(TAG,"mViewHeight=="+mViewHeight);
            isFirst=false;
        }
//        LogUtil.i(TAG,"scrennheight=="+ ScreenUtils.getScreenHeight(getContext()));
        LogUtil.i(TAG,"onLayout");
//        LogUtil.i(TAG,"viewpagerheight=="+mViewPager.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtil.i(TAG,"onSizeChanged");
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
                if (Math.abs(ev.getRawY() - mDownY) > mTouchSlop && Math.abs(ev.getRawX() - mDownX) < mTouchSlop) {
                    mCurrentTranslationY=getTranslationY();
                    mLastY=ev.getRawY();
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
                float moveY = ev.getRawY() - mLastY;
                mLastY=ev.getRawY();
                setTranslationMove(moveY);
                if (mCurrentTranslationY<=-mMaxY){
                    setVisibility(GONE);
                    // 重新dispatch一次down事件，使得列表可以继续滚动
                    int oldAction = ev.getAction();
                    ev.setLocation(ev.getRawX(),ev.getRawY());
                    ev.setAction(MotionEvent.ACTION_DOWN);
                    ((ViewGroup)getParent()).dispatchTouchEvent(ev);
                    ev.setAction(oldAction);
//                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.i(TAG, "onTouchEvent==ACTION_UP");
                setAnimTranslationMove();
                break;
        }
        return super.onTouchEvent(ev);
    }


    public void setTranslationMove(float move) {
        startScrollListers(move);
        LogUtil.i(TAG,"setTranslationMove move=="+move);
        LogUtil.i(TAG,"setTranslationMove mCurrentTranslationY=="+mCurrentTranslationY);
        mCurrentTranslationY+=move;
        if (mCurrentTranslationY<=-mMaxY){
            mCurrentTranslationY=-mMaxY;
        }
        setTranslationY(mCurrentTranslationY);
        setViewPagerLayoutMove();
    }

    public void setAnimTranslationMove(){
        //0 viewpagager上部状态向上滑动 1：viewpagager上部状态向下滑动
        //2 viewpagager底部状态向下滑动 3：viewpagager底部状态向上滑动
        int type=0;
        if (mCurrentTranslationY<=0){
            type=Math.abs(mCurrentTranslationY)>mMaxY/2?0:1;
        }else {
            type=Math.abs(mCurrentTranslationY)>mMaxY?2:3;
        }
        final ValueAnimator animator = ValueAnimator.ofFloat(0, 500);
        animator.setTarget(this);
        animator.setDuration(500).start();
        final int finalType = type;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                switch (finalType){
                    case 0:
                        if (mCurrentTranslationY<=-mMaxY){
//                            setTranslationMove(0);
                            return;
                        }
                        setTranslationMove(-7);
                        break;
                    case 1:
                        if (mCurrentTranslationY>=-7){
                            mCurrentTranslationY=0;
//                            setTranslationMove(0);
                            return;
                        }
                        setTranslationMove(7);
                        break;
                    case 2:
                        setTranslationMove((mViewHeight-mMaxY)/16);
                        break;
                    case 3:
                        if (mCurrentTranslationY<=7){
                            mCurrentTranslationY=0;
//                            setTranslationMove(0);
                            return;
                        }
                        setTranslationMove(-7);
                        break;
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setTranslationMove(0);
                switch (finalType){
                    case 0:
                        startStatusListers(STATUS_TOP);
                        break;
                    case 1:
                        startStatusListers(STATUS_NORMAL);
                        break;
                    case 2:
                        startStatusListers(STATUS_BOTTOM);
                        break;
                    case 3:
                        startStatusListers(STATUS_NORMAL);
                        break;
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
        if (mCurrentTranslationY>0)return;
        float move=Math.abs(mCurrentTranslationY);
        if (move>mMaxY){
            move=mMaxY;
        }
        float left = viewPagerItemMarginHalf / 2;
        left = (move / mMaxY) * left;
        float top = (mViewPagerTop - mMaxY) * move / mMaxY;
//        LogUtil.i("top=="+top);
        View temp=((ViewGroup)mViewPager.getChildAt(0)).getChildAt(0);
//        temp.layout(0, (int) (0-top),temp.getWidth(), (int) (temp.getHeight()-top));
        temp.setPivotY(temp.getHeight());
//        LogUtil.i("temp.getHeight()=="+temp.getHeight());
        temp.setScaleY((float) (1-(top/mMaxY)*0.11));
        mViewPager.layout(-(int) left * 2, (int) (mViewPagerTop ), (int) (mViewPagerWidth + left * 2), (int) ((mViewPagerTop ) + mViewPagerHeight));
//        LogUtil.i(TAG,"mViewPager.getTranslationY()"+mViewPager.getTranslationY());
//        LogUtil.i(TAG,"mViewPager.mViewPager.getY()"+mViewPager.getY());
    }


}
