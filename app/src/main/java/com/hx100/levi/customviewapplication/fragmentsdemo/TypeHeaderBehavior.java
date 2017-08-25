package com.hx100.levi.customviewapplication.fragmentsdemo;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
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
    int offset;
    int top;
    boolean  shrinking;
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
        top=ScreenUtils.dip2px(context,50)+ ImmersionBar.getStatusBarHeight(fragment.getActivity());
        offset=fragment.ll_header.getHeight()-top;
        ViewGroup.MarginLayoutParams lp= (ViewGroup.MarginLayoutParams) fragment.fl_content.getLayoutParams();
        lp.topMargin=top;
        LogUtil.i("top=="+top);
        LogUtil.i("offset=="+offset);
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
        ViewCompat.setTranslationY(child,dependency.getHeight()+dependency.getTranslationY()-top);
        return true;
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
        if (dy<0){//下拉
            return;
        }
        float newTranslateY = fragment.ll_header.getTranslationY() - dy;
//        LogUtil.i("newTranslateY"+newTranslateY);
        if (Math.abs(newTranslateY)<offset){
            consumed[1]=dy;
            ViewCompat.setTranslationY(fragment.ll_header,newTranslateY);
        }

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        LogUtil.i("onNestedScroll....dyConsumed=="+dyConsumed);
        LogUtil.i("onNestedScroll....dyUnconsumed=="+dyUnconsumed);
        if (dyUnconsumed>0){//上拉
            return;
        }
        float newTranslateY = fragment.ll_header.getTranslationY() - dyUnconsumed;
        LogUtil.i("onNestedScroll-----------newTranslateY=="+newTranslateY);

        if (newTranslateY <= 0) {
            fragment.ll_header.setTranslationY(newTranslateY);
            return;
        }
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
