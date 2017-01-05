package com.hx100.levi.customviewapplication.customview.zoomheaderview;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.utils.LogUtil;

/**
 * Created by Levi on 2016/12/13.
 */
public class ZoomHeaderBehavior extends CoordinatorLayout.Behavior<View> {
    // app:layout_behavior="com.hx100.levi.customviewapplication.customview.zoomheaderview.ZoomHeaderBehavior"
    public static final String TAG = "tag_ZoomHeaderBehavior";
    private ZoomHeaderView mDependency;
    boolean isZoomHeaderViewTouch;
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
        LogUtil.i(TAG, "onDependentViewChanged mDependency.getHeight()=="+mDependency.getHeight() );
        LogUtil.i(TAG, "onDependentViewChanged child.getHeight()=="+child.getHeight());
//        ViewCompat.offsetTopAndBottom(child, (int)dependency.getY()+dependency.getHeight());
        child.setTranslationY(dependency.getY()+dependency.getHeight());
//        child.scrollTo(0, -(int)dependency.getY()+dependency.getHeight());
//        child.layout(0,(int)mDependency.getY()+mDependency.getHeight(),child.getWidth(),(int)mDependency.getY()+mDependency.getHeight()+child.getHeight());
//        child.invalidate();
//        child.offsetTopAndBottom(10);
        if (dependency.getY() < 0) {
            float alpha = Math.abs(dependency.getY()) / ((ZoomHeaderView) dependency).mMaxY;
            if (alpha > 0.95) {
                alpha = 1;
//                isZoomHeaderViewTouch=false;
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
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        LogUtil.i(TAG, "onLayoutChild ");
//        ((CoordinatorLayout.LayoutParams)child.getLayoutParams()).topMargin=1500;
        child.layout(0,(int)mDependency.getY()+mDependency.getHeight(),child.getWidth(),(int)mDependency.getY()+mDependency.getHeight()+child.getHeight());
        return false;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
//        return true;//这里返回true，才会接受到后续滑动事件。
        //只关心垂直事件
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//进行滑动事件处理
        LogUtil.i(TAG, "onNestedScroll dyConsumed==" + dyConsumed);
        LogUtil.i(TAG, "onNestedScroll dyUnconsumed==" + dyUnconsumed);
//        if (!canViewScrollUp(target)){
//            LogUtil.i(TAG, "onNestedScroll !canViewScrollUp(target)&&dyUnconsumed <= 0");
//            mDependency.getViewPager().setVisibility(View.VISIBLE);
//            mDependency.setTranslationMove(-dyUnconsumed);
//        }
        if (!canViewScrollUp(target) && dyUnconsumed < 0) {
            isInterceptScroll = true;
        }
//        if (isInterceptScroll) {
//            LogUtil.i(TAG, "onNestedScroll isInterceptScroll");
//            mDependency.getViewPager().setVisibility(View.VISIBLE);
//            mDependency.setTranslationMove(-dyUnconsumed);
//        }
    }

    boolean isInterceptScroll;
    boolean isInterceptPreScroll;
    int currentY = 0;

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        LogUtil.i(TAG, "onNestedPreScroll dy==" + dy);

        if (!isInterceptPreScroll && !canViewScrollUp(target) && dy < 0 ) {
            LogUtil.i(TAG, "onNestedPreScroll !isInterceptPreScroll && !canViewScrollUp(target) && dy < 0 ");
            isInterceptPreScroll = true;
        }
//        if (isInterceptPreScroll) {
//            LogUtil.i(TAG, "onNestedPreScroll isInterceptPreScroll");
//            consumed[1] = dy;
//            mDependency.setVisibility(View.VISIBLE);
////            mDependency.bringToFront();
//            mDependency.setTranslationMove(isInterceptScroll?-dy:currentY - dy);
//            currentY = dy;
//        }

    }

    private boolean canViewScrollUp(View view) {
        return ViewCompat.canScrollVertically(view, -1);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        LogUtil.i(TAG, "onStopNestedScroll");
//        if (isInterceptScroll||isInterceptPreScroll){
//            mDependency.setAnimTranslationMove();
//        }
        if (isInterceptScroll) {
            isInterceptScroll = false;
        }
        if (isInterceptPreScroll) {
            isInterceptPreScroll = false;
        }
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        //当进行快速滑动
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

}
