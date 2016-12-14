package com.hx100.levi.customviewapplication.customview.zoomheaderview;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hx100.levi.customviewapplication.utils.LogUtil;

/**
 * Created by Levi on 2016/12/13.
 */
public class ZoomHeaderBehavior extends CoordinatorLayout.Behavior<View>{
    public static final String TAG = "tag_ZoomHeaderBehavior";
    public ZoomHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private ZoomHeaderView mDependency;
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof ZoomHeaderView){
            this.mDependency= (ZoomHeaderView) dependency;
        }
        return dependency instanceof ZoomHeaderView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
//        LogUtil.i(TAG,"dependency.getY()=="+dependency.getY());
        if (dependency.getY()<0){
            float alpha=Math.abs(dependency.getY())/((ZoomHeaderView)dependency).mMaxY;
            if (alpha>0.95){
                alpha=1;
            }
            if (alpha<0.01){
                alpha=0;
            }
//            LogUtil.i(TAG,"alpha=="+alpha);
            child.setAlpha(alpha);
        }
        return true;
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
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        //如果在顶部
        if (((NestedScrollView) target).getScrollY()== 0) {
            //向下滑动
            if (dy < 0) {
//                LogUtil.i(TAG,"dy=="+dy);
                mDependency.getViewPager().setVisibility(View.VISIBLE);
                mDependency.startTranslation(dy);
//                target.setAlpha(0);
//                mDependency.setY(mDependency.getY() - dy);
//                //小于阀值
//                if (mDependency.getY() < 500) {
//                    mDependency.restore(mDependency.getY());
//                }
            }else {
                mDependency.getViewPager().setVisibility(View.GONE);
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
//当进行快速滑动
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }
}
