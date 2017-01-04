package com.hx100.levi.customviewapplication.customview.zoomheaderview;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.utils.LogUtil;

/**
 * Created by Levi on 2016/12/13.
 */
public class ZoomHeaderBehavior extends CoordinatorLayout.Behavior<View> {
    public static final String TAG = "tag_ZoomHeaderBehavior";
    private ZoomHeaderView mDependency;
    boolean isZoomHeaderViewTouch;
    float mDownX;
    float mDownY;
    private float mTouchSlop;

    public ZoomHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof ZoomHeaderView) {
            this.mDependency = (ZoomHeaderView) dependency;
        }
        return dependency instanceof ZoomHeaderView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        LogUtil.i(TAG,"dependency.getY()=="+dependency.getY());
        if (dependency.getY() < 0) {
            float alpha = Math.abs(dependency.getY()) / ((ZoomHeaderView) dependency).mMaxY;
            if (alpha > 0.95) {
                alpha = 1;
//                isZoomHeaderViewTouch=false;
                LogUtil.i(TAG, "onDependentViewChanged");
            }
            if (alpha < 0.01) {
                alpha = 0;
            }
//            LogUtil.i(TAG,"alpha=="+alpha);
//            child.findViewById(R.id.ll_header).setVisibility(View.INVISIBLE);
            //recyclerView Top始终处于ZoomHeaderView bottom
//            child.setY(dependency.getY() + dependency.getHeight());
//            child.setAlpha(alpha);
        }
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
//        return true;//这里返回true，才会接受到后续滑动事件。
        //只关心垂直事件
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    boolean isNestedScroll;
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//进行滑动事件处理
        LogUtil.i(TAG, "onNestedScroll dyConsumed==" + dyConsumed);
        LogUtil.i(TAG, "onNestedScroll dyUnconsumed==" + dyUnconsumed);
        isNestedScroll=false;
        if (dyUnconsumed <= 0) {
            isNestedScroll=true;
//            mDependency.getViewPager().setVisibility(View.VISIBLE);
//            mDependency.setTranslationMove(-dyUnconsumed);
//            if (isZoomHeaderViewTouch) {
////                mDependency.getViewPager().setVisibility(View.VISIBLE);
////                mDependency.setTranslationMove(dyUnconsumed);
//            }
//            mDependency.getViewPager().setVisibility(View.VISIBLE);
//            mDependency.startTranslation(mDependency.mMaxY+dyUnconsumed);
//            target.setAlpha(1-Math.abs(dyUnconsumed)/mDependency.mMaxY);
        }

    }

    int currentY=0;
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        LogUtil.i(TAG, "onNestedPreScroll");
        //如果在顶部
        if (((NestedScrollView) target).getScrollY() == 0) {
            //向下滑动
            if (dy < 0) {
                isZoomHeaderViewTouch=true;
                LogUtil.i(TAG, "onNestedPreScroll dy=="+dy);
                mDependency.getViewPager().setVisibility(View.VISIBLE);
                consumed[1]=dy;
                if (isNestedScroll){
                    mDependency.setTranslationMove(-dy);
                    return;
                }
                mDependency.setTranslationMove(currentY-dy);
                currentY=dy;
//                isZoomHeaderViewTouch=true;
//                if (!isZoomHeaderViewTouch){
//                    mDependency.mDownGetY = mDependency.getY();
//                }
//                isZoomHeaderViewTouch = true;
//                mDependency.getViewPager().setVisibility(View.VISIBLE);
//                mDependency.setTranslationMove(dy);

//                mDependency.getViewPager().setVisibility(View.VISIBLE);
//                LogUtil.i(TAG,"dy=="+dy);
//                mDependency.getViewPager().setVisibility(View.VISIBLE);
//                mDependency.startTranslation(mDependency.mMaxY-dy);
//                target.setAlpha(0);
//                mDependency.setY(mDependency.getY() - dy);
//                //小于阀值
//                if (mDependency.getY() < 500) {
//                    mDependency.restore(mDependency.getY());
//                }
            } else {
//                if (mDependency.status==ZoomHeaderView.STATUS_TOP){
//                    mDependency.getViewPager().setVisibility(View.INVISIBLE);
//                }
                isZoomHeaderViewTouch=false;
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        LogUtil.i(TAG, "onStopNestedScroll");
        if (isZoomHeaderViewTouch){
            mDependency.setAnimTranslationMove();
            isZoomHeaderViewTouch=false;
        }
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
//当进行快速滑动
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

//    @Override
//    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                LogUtil.i(TAG, "onInterceptTouchEvent==ACTION_DOWN");
//                mDownY = ev.getRawY();
//                mDownX = ev.getRawX();
////                    LogUtil.i(TAG,"onInterceptTouchEvent mDownY=="+mDownY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (Math.abs(ev.getRawY() - mDownY) > mTouchSlop && Math.abs(ev.getRawX() - mDownX) < mTouchSlop && isZoomHeaderViewTouch) {
//                    mDownY = ev.getRawY();
//                    mDependency.mDownY = ev.getRawY();
//                    mDependency.getViewPager().setVisibility(View.VISIBLE);
////                    mDependency.getViewPager().setVisibility(View.VISIBLE);
////                    mDependency.showViewPagerLayoutMove();
//                    LogUtil.i(TAG, "onInterceptTouchEvent==mDependency.getY()==" + mDependency.getY());
////                    mDependency.onTouchEvent(ev);
//                    return true;
//                }
//                LogUtil.i(TAG, "onInterceptTouchEvent==ACTION_MOVE");
//                LogUtil.i(TAG,"onInterceptTouchEvent==ACTION_MOVE move==="+(ev.getRawY()-mDownY));
//                break;
//            case MotionEvent.ACTION_UP:
//                LogUtil.i(TAG, "onInterceptTouchEvent==ACTION_UP");
//                break;
//        }
////        LogUtil.i(TAG,"onInterceptTouchEvent=="+isZoomHeaderViewTouch);
//
//
//        return super.onInterceptTouchEvent(parent, child, ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
//        if (isZoomHeaderViewTouch){
////            float moveY = ev.getRawY() - mDownY;
////            LogUtil.i(TAG,"onTouchEvent...moveY=="+moveY);
////            mDependency.onTouchEvent(ev);
////            mDependency.setTranslationMove(moveY);
//            if (ev.getAction()==MotionEvent.ACTION_UP){
//                mDependency.mDownGetY = mDependency.mStartY;
//            }
//            return mDependency.onTouchEvent(ev);
//        }
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                LogUtil.i(TAG, "onTouchEvent==ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                LogUtil.i(TAG, "onTouchEvent==ACTION_MOVE");
//                break;
//            case MotionEvent.ACTION_UP:
//                LogUtil.i(TAG, "onTouchEvent==ACTION_UP");
//                isZoomHeaderViewTouch=false;
////                mDependency.setAnimTranslationMove();
//                break;
//        }
////        LogUtil.i(TAG,"onTouchEvent=="+isZoomHeaderViewTouch);
////        LogUtil.i(TAG,"onTouchEvent...ev.getY()=="+ev.getY());
//
//        return super.onTouchEvent(parent, child, ev);
//    }
}
