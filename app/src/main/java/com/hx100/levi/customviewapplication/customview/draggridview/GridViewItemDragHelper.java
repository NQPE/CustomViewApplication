package com.hx100.levi.customviewapplication.customview.draggridview;

import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by levi on 2017/3/28.
 */
public class GridViewItemDragHelper {
    private static final String TAG = "TAG_DragGridViewItemHelper";

    /**
     * A null/invalid pointer ID.
     */
    public static final int INVALID_POINTER = -1;

    /**
     * A view is not currently being dragged or animating as a result of a fling/snap.
     */
    public static final int STATE_IDLE = 0;

    /**
     * A view is currently being dragged. The position is currently changing as a result
     * of user input or simulated user input.
     */
    public static final int STATE_DRAGGING = 1;

    /**
     * A view is currently settling into place as a result of a fling or
     * predefined non-interactive motion.
     */
    public static final int STATE_SETTLING = 2;

    @IntDef({STATE_IDLE, STATE_DRAGGING,STATE_SETTLING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ItemViewState{}

    public static abstract class Callback {
        //当View的拖拽状态改变时回调,state为STATE_IDLE,STATE_DRAGGING,STATE_SETTLING的一种
        //STATE_IDLE:　当前未被拖拽
        //STATE_DRAGGING：正在被拖拽
        //STATE_SETTLING:　被拖拽后需要被安放到一个位置中的状态
        public void onViewDragStateChanged(int state) {}

        //当View被拖拽位置发生改变时回调
        //changedView ：被拖拽的View
        //left : 被拖拽后View的left边缘坐标
        //top : 被拖拽后View的top边缘坐标
        //dx : 拖动的x偏移量
        //dy : 拖动的y偏移量
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {}

        //当一个View被捕获到准备开始拖动时回调,
        //capturedChild : 捕获的View
        //activePointerId : 对应的PointerId
        public void onViewCaptured(View capturedChild, int activePointerId) {}

        //当被捕获拖拽的View被释放是回调
        //releasedChild : 被释放的View
        //xvel : 释放View的x方向上的加速度
        //yvel : 释放View的y方向上的加速度
        public void onViewReleased(View releasedChild, float xvel, float yvel) {}

        //在寻找当前触摸点下的子View时会调用此方法，寻找到的View会提供给tryCaptureViewForDrag()来尝试捕获。
        //如果需要改变子View的遍历查询顺序可改写此方法，例如让下层的View优先于上层的View被选中。
        public int getOrderedChildIndex(int index) {
            return index;
        }

        //获取被拖拽View child 的水平拖拽范围,返回0表示无法被水平拖拽
        public int getViewHorizontalDragRange(View child) {
            return 0;
        }

        //获取被拖拽View child 的垂直拖拽范围,返回0表示无法被水平拖拽
        public int getViewVerticalDragRange(View child) {
            return 0;
        }

        //尝试捕获被拖拽的View
        public abstract boolean tryCaptureView(View child, int pointerId);

    }
}
