package com.hx100.levi.customviewapplication.customview.CustomBehavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.utils.LogUtil;

/**
 *
 */

public class CoverHeaderScrollBehavior extends CoordinatorLayout.Behavior<View> {
    public static final String TAG = "CoverHeaderScroll";

    public CoverHeaderScrollBehavior(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    LinearLayout ll_header;
    RelativeLayout rl_title;
    FrameLayout fl_top_bg;
    boolean is_first=true;
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        if (!is_first)return false;
        LogUtil.i("onLayoutChild.....  not first");
        ll_header= (LinearLayout) parent.findViewById(R.id.ll_header);
        rl_title= (RelativeLayout) parent.findViewById(R.id.rl_title);
        fl_top_bg= (FrameLayout) parent.findViewById(R.id.fl_top_bg);
        is_first=false;
        ll_header.setTranslationY(150);
        child.setTranslationY(ll_header.getHeight()+ll_header.getTranslationY());
        LogUtil.i("onLayoutChild.....  ll_header.getHeight()=="+ll_header.getHeight());
        LogUtil.i("onLayoutChild..... ll_header.getTranslationY()=="+ll_header.getTranslationY());
        return true;
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
//        if(params!=null && params.height == CoordinatorLayout.LayoutParams.MATCH_PARENT){
//            child.layout(0,0,parent.getWidth(),parent.getHeight());
//            child.setTranslationY(getHeaderHeight());
//            return true;
//        }

//        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency.getId()==R.id.ll_header){
            return true;
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        LogUtil.i("onDependentViewChanged....");
        child.setTranslationY(ll_header.getHeight()+ll_header.getTranslationY());
        return true;
//        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        LogUtil.i("onNestedPreScroll....dy=="+dy);
        LogUtil.i("onNestedPreScroll....ll_header.getTranslationY()=="+ll_header.getTranslationY());
        if (dy<0){//下拉
            return;
        }
        float newTranslateY = ll_header.getTranslationY() - dy;
        float minHeaderTranslate = -450;
        LogUtil.i("onNestedPreScroll-----------newTranslateY=="+newTranslateY);

        if (newTranslateY > minHeaderTranslate) {
            ll_header.setTranslationY(newTranslateY);
            consumed[1] = dy;
        }
//        if (dy>=0){//上拉
//            if (ll_header.getTranslationY()>=-450){
//                ll_header.setTranslationY(ll_header.getTranslationY() - dy);
//                consumed[1]=dy;
//            }
//        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        LogUtil.i("onNestedScroll....dyConsumed=="+dyConsumed);
        LogUtil.i("onNestedScroll....dyUnconsumed=="+dyUnconsumed);
        if (dyUnconsumed>0){//上拉
            return;
        }
            float newTranslateY = ll_header.getTranslationY() - dyUnconsumed;
            float maxHeaderTranslate = 150;
            LogUtil.i("onNestedScroll-----------newTranslateY=="+newTranslateY);

            if (newTranslateY < maxHeaderTranslate) {
                ll_header.setTranslationY(newTranslateY);
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
