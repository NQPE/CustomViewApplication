package com.hx100.levi.customviewapplication.fragmentsdemo;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.utils.LogUtil;
import com.hx100.levi.customviewapplication.utils.ScreenUtils;

/**
 *
 */

public class TypeHeaderBehavior extends CoordinatorLayout.Behavior<View> {
    public static final String TAG = "tag";
    ServiceThreeFragment fragment;
    View dependencyView;
    Context context;
    public TypeHeaderBehavior(ServiceThreeFragment fragment){
        this.fragment=fragment;
        this.context=fragment.getContext();
    }

    public TypeHeaderBehavior(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        LogUtil.i("onLayoutChild.....  ");
        int top=ScreenUtils.dip2px(context,50)+ ImmersionBar.getStatusBarHeight(fragment.getActivity());
        int bottom=top+fragment.ll_header.getHeight();
//        LogUtil.i("top=="+top);
//        LogUtil.i("bottom=="+bottom);
        fragment.ll_header.layout(0, top,
                fragment.ll_header.getWidth(),
                bottom);
//        LogUtil.i("fl_content gettop" +fragment.fl_content.getTop());
//        LogUtil.i("fl_content getheight" +fragment.fl_content.getHeight());
        return false;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        LogUtil.i("layoutDependsOn.....  ");
        if (dependency.getId()==R.id.ll_header){
            this.dependencyView=dependency;
            return true;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        LogUtil.i("onDependentViewChanged....");
        ViewCompat.setY(child,dependency.getY()+dependency.getHeight());
//        child.setTranslationY(dependencyView.getHeight()+dependencyView.getTranslationY());
        return true;
//        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        LogUtil.i("onStartNestedScroll....");
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        LogUtil.i("onNestedPreScroll....dy=="+dy);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        LogUtil.i("onNestedScroll....dyConsumed=="+dyConsumed);
        LogUtil.i("onNestedScroll....dyUnconsumed=="+dyUnconsumed);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        LogUtil.i("onNestedPreFling....velocityY=="+velocityY);
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        LogUtil.i("onStopNestedScroll....");
    }
}
