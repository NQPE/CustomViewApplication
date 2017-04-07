package com.hx100.levi.customviewapplication.customview.draggridview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * Created by levi on 2017/3/27.
 * 可拖拽的GridView
 */
public class DragGridView extends ViewGroup {
    public static final String TAG = "TAG_DragGridView";

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

    @IntDef({STATE_IDLE, STATE_DRAGGING, STATE_SETTLING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ItemViewState {
    }

    //行(Row)
    int mRow = 1;
    //列(Col)
    int mCol = 0;
    //view宽度
    int mWidth;
    //view高度
    int mHeight;
    //一个item的宽度
    int mItemWidth;
    //一个item的高度
    int mItemHeight;

    //拖动状态
    int mDragState = STATE_IDLE;

    //控件是否可拖动
    boolean mDragEnable = true;

    DragGridItemAdapter mDragGridIteAdapter;

    //拖动的itemview
    View mDragGridItemView;
    //与拖动的item重叠的itemview
    View mOverlapGridItemView;
    //K 为itemview V 为itemview对应的信息
    HashMap<View, InfoAttachItemView> mMapInfoItemView;
    //K 为position V 为对应的坐标信息
    HashMap<Integer, XYAttachPosition> mMapXYPosition = new HashMap<>();

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setChildrenDrawingOrderEnabled(true);
//        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setClipChildren(false);
    }

    @Override
    protected LayoutParams generateLayoutParams(
            LayoutParams p) {
        Log.i(TAG, "generateLayoutParams p");
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        Log.i(TAG, "generateLayoutParams attrs");
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        Log.i(TAG, "generateDefaultLayoutParams");
        //must wrap content
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "onFinishInflate");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged");
        if (mMapInfoItemView != null) return;
        for (int i=0;i<getChildCount();i++){
            //初始化MapInfo
            initMapInfoItemView(getChildAt(i), i);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "onDetachedFromWindow");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure");
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        mWidth = sizeWidth;
        int childCount = getChildCount();
        Log.i(TAG, "childCount==" + childCount);
        //总共有多少行
        mRow = (childCount / mCol) + (childCount % mCol != 0 ? 1 : 0);
        Log.i(TAG, "mRow==" + mRow);
        for (int i = 0; i < childCount; i++) {
            // 必须测量每一个child的高
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        //这里默认每个griditem宽高是一样的
        if (childCount != 0) {
            // 获得第一个childview
            View child = getChildAt(0);
            //得到child的MarginLayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            mItemHeight = child.getMeasuredHeight();
            mItemWidth = child.getMeasuredWidth();
            //总高度=childview的高度*行数
            height = (mItemHeight + lp.topMargin + lp.bottomMargin) * mRow + getPaddingBottom() + getPaddingTop();
        }

        mHeight = modeHeight == MeasureSpec.EXACTLY ? Math.max(sizeHeight, height) : height;

        Log.i(TAG, "onMeasure     mWidth==" + mWidth + "   mHeight==" + mHeight);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG, "onLayout");
        int childCount = getChildCount();
        //每个item的平均宽度
        int childAverageWidth = (mWidth - getPaddingLeft() - getPaddingRight()) / mCol;
        mItemWidth = Math.min(childAverageWidth, mItemWidth);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        //判断一行是否显示完毕 判断换行
        int tempCount = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 判断child的状态
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            //得到child的MarginLayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int lc = (left + (childAverageWidth - mItemWidth) / 2 + lp.leftMargin - lp.rightMargin);
            int rc = lc + child.getMeasuredWidth();
            int tc = (top + lp.topMargin);
            int bc = tc + child.getMeasuredHeight();
//            Log.i(TAG, "onLayout lc,rc,tc,bc=="+lc+"  "+rc+"  "+tc+"  "+bc);
            child.layout(lc, tc, rc, bc);

            //初始化mapXY
            initMapXYPosition(i, lc, tc);

            left += childAverageWidth;
            tempCount++;
            if (tempCount == mCol) {
                left = getPaddingLeft();
                top += mItemHeight + lp.bottomMargin + lp.topMargin;
                tempCount = 0;
            }
        }
    }

    /**
     * 初始化MapInfo 存储itemview的position 信息
     *
     * @param child
     * @param i
     */
    private void initMapInfoItemView(View child, int i) {
        if (mMapInfoItemView==null){
            mMapInfoItemView = new HashMap<>();
        }
        InfoAttachItemView itemInfo = new InfoAttachItemView();
        itemInfo.itemPosition = i;
        itemInfo.itemState = STATE_IDLE;
        mMapInfoItemView.put(child, itemInfo);
    }

    /**
     * 初始化mapXY 记录各个position的位置
     *
     * @param i
     * @param lc
     * @param tc
     */
    private void initMapXYPosition(int i, int lc, int tc) {
        XYAttachPosition xyAttachPosition = createXYAttachPosition(lc, tc);
        mMapXYPosition.put(i, xyAttachPosition);
    }

    private XYAttachPosition createXYAttachPosition(int lc, int tc) {
        XYAttachPosition xyAttachPosition = new XYAttachPosition();
        xyAttachPosition.X = lc;
        xyAttachPosition.Y = tc;
        return xyAttachPosition;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return processTouchEvent(event);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        //重写此方法  让选择的itemview 绘制顺序在最后
        // 方便移动时 覆盖到其他itemview的上方
        if (mDragGridItemView == null) {
            return i;
        }
        if (i == indexOfChild(mDragGridItemView)) {
            return childCount - 1;
        } else if (i == childCount - 1) {
            return indexOfChild(mDragGridItemView);
        } else {
            return i;
        }
//        return super.getChildDrawingOrder(childCount, i);
    }

    /**
     * 处理拦截事件
     *
     * @param ev
     * @return
     */
    private boolean shouldInterceptTouchEvent(MotionEvent ev) {
        //获取action
        final int action = MotionEventCompat.getActionMasked(ev);
        //获取action对应的index
        final int actionIndex = MotionEventCompat.getActionIndex(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                final float x = ev.getX();
                final float y = ev.getY();
                mDragGridItemView = findTopChildUnder((int) x, (int) y);
                if (mDragGridItemView != null) {
                    //重写刷新draw的缓存 好重新排序drwaing
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }

        return false;
    }

    /**
     * 处理触摸事件
     *
     * @param ev
     * @return
     */
    private boolean processTouchEvent(MotionEvent ev) {

        final int action = MotionEventCompat.getActionMasked(ev);
        final int actionIndex = MotionEventCompat.getActionIndex(ev);
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                final float y = ev.getY();
                if (mDragGridItemView != null) {
                    //拖拽至指定位置
                    dragTo(mDragGridItemView.getLeft(), mDragGridItemView.getTop(), (int) x, (int) y);
                }
                //处理挤压动画
                handleMoveAnimation();
                break;
            case MotionEvent.ACTION_UP:
                mDragGridItemView = null;
                break;
        }

        return true;
    }

    /**
     * 用handle的消息队列处理机制来player挤压动画
     */
    Handler handlerMoveAnimation=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj instanceof AnimatorSet){
                ((AnimatorSet)msg.obj).start();
            }
        }
    };
    /**
     * 处理挤压的移动动画
     */
    private void handleMoveAnimation() {
        if (mDragGridItemView == null) return;
        int overlapPosition = getOverlapPosition(mDragGridItemView);
        if (overlapPosition==-1)return;
        if (overlapPosition==mMapInfoItemView.get(mDragGridItemView).itemPosition)return;
        AnimatorSet animatorSet=createMoveAnimation(overlapPosition,mMapInfoItemView.get(mDragGridItemView).itemPosition);
        handleMoveData(overlapPosition,mMapInfoItemView.get(mDragGridItemView).itemPosition);
        Message msg=new Message();
        msg.obj=animatorSet;
        handlerMoveAnimation.sendMessageDelayed(msg,200);
    }

    /**
     * 创建挤压动画
     * @param overlapPosition
     * @param itemPosition
     */
    private AnimatorSet createMoveAnimation(int overlapPosition, int itemPosition) {
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(200);
        ArrayList<Animator> valueAnimators = new ArrayList<>();
        if (overlapPosition>itemPosition){
            for (int i=overlapPosition;i>itemPosition;i--){
                View itemview=getItemViewByPosition(i);
                if (itemview==null)continue;
                ValueAnimator animator = creatValueAnimator(itemview,i,i-1);
                valueAnimators.add(animator);
            }
        }else {
            for (int i=overlapPosition;i<itemPosition;i++){
                View itemview=getItemViewByPosition(i);
                if (itemview==null)continue;
                ValueAnimator animator = creatValueAnimator(itemview,i,i+1);
                valueAnimators.add(animator);
            }
        }
        animSet.playTogether(valueAnimators);
        return animSet;
    }

    /**
     * 获得在某个位置的view
     * @param position
     * @return
     */
    private View getItemViewByPosition(int position) {
        View item=null;
        //遍历map中的键
        for (View key : mMapInfoItemView.keySet()) {
            if (mMapInfoItemView.get(key).itemPosition==position) {
                item=key;
                break;
            }
        }
        return item;
    }

    /**
     * 得到拖动的item移动到哪个position
     *
     * @param mDragGridItemView
     * @return
     */
    private int getOverlapPosition(View mDragGridItemView) {
        int position = -1;
        int dragitemcenterx = mDragGridItemView.getLeft() + mItemWidth / 2;
        int dragitemcentery = mDragGridItemView.getTop() + mItemHeight / 2;
        //遍历map中的键
        for (Integer key : mMapXYPosition.keySet()) {
            if (dragitemcenterx >= mMapXYPosition.get(key).X + mItemWidth / 4 && dragitemcenterx < mMapXYPosition.get(key).X + mItemWidth * 3 / 4 &&
                    dragitemcentery >= mMapXYPosition.get(key).Y + mItemHeight / 4 && dragitemcentery < mMapXYPosition.get(key).Y + mItemHeight * 3 / 4) {
                position = key;
                break;
            }
        }
        return position;
    }


    /**
     * 处理move之后的数据
     *
     * @param overlapPosition
     * @param itemPosition
     */
    private void handleMoveData(int overlapPosition, int itemPosition) {
        List<View> tempViews=new ArrayList<>();
        List<Integer> tempPosition=new ArrayList<>();
        if (overlapPosition>itemPosition){
            for (int i=overlapPosition;i>=itemPosition;i--){
                View itemview=getItemViewByPosition(i);
                tempViews.add(itemview);
                tempPosition.add(i==itemPosition?overlapPosition:(i-1));
            }
        }else {
            for (int i=overlapPosition;i<=itemPosition;i++){
                View itemview=getItemViewByPosition(i);
                tempViews.add(itemview);
                tempPosition.add(i==itemPosition?overlapPosition:(i+1));
            }
        }

        for (int j=0;j<tempViews.size();j++){
            mMapInfoItemView.get(tempViews.get(j)).itemPosition=tempPosition.get(j);
        }

//        //遍历map中的键
//        for (View key : mMapInfoItemView.keySet()) {
//            Log.i(TAG,"view==="+key.hashCode());
//            Log.i(TAG,"position==="+mMapInfoItemView.get(key).itemPosition);
//        }
        tempViews.clear();
        tempViews=null;
        tempPosition.clear();
        tempPosition=null;
    }


    /**
     *
     * @param itemview  需要移动的view
     * @param beforepos 当前item的position
     * @param afterpos  需要移动去的那个position
     * @return
     */
    private ValueAnimator creatValueAnimator(final View itemview, final int beforepos, int afterpos) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setTarget(itemview);
        final int l = mMapXYPosition.get(beforepos).X;
        final int offset_l =mMapXYPosition.get(afterpos).X- mMapXYPosition.get(beforepos).X;
        final int t = mMapXYPosition.get(beforepos).Y;
        final int offset_t = mMapXYPosition.get(afterpos).Y- mMapXYPosition.get(beforepos).Y;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int lc = (int) (l + offset_l * (float) animation.getAnimatedValue());
                int tc = (int) (t + offset_t * (float) animation.getAnimatedValue());
                itemview.layout(lc, tc, lc + mItemWidth, tc + mItemHeight);
            }
        });
        return animator;
    }



    /**
     * 移动view
     *
     * @param left
     * @param top
     * @param dx
     * @param dy
     */
    private void dragTo(int left, int top, int dx, int dy) {
        if (dx != 0) {
            //移动View
            ViewCompat.offsetLeftAndRight(mDragGridItemView, dx - left);
        }
        if (dy != 0) {
            //移动View
            ViewCompat.offsetTopAndBottom(mDragGridItemView, dy - top);
        }

    }

    /**
     * 根据x和y坐标和来找到触摸的draggriditem
     *
     * @param x
     * @param y
     * @return
     */
    private View findTopChildUnder(int x, int y) {
        final int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (x >= child.getLeft() && x < child.getRight() &&
                    y >= child.getTop() && y < child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    /**
     * 依附于itemview上面的信息
     *
     * @param <T>
     */
    public static class InfoAttachItemView<T> implements Serializable {
        //itemview位置
        public int itemPosition;

        //itemview状态
        public int itemState;

        //itemview对应的itemdata
        public T itemData;
    }

    /**
     * 各个position的位置信息
     */
    public static class XYAttachPosition implements Serializable {
        //左上角X
        public int X;

        //左上角Y
        public int Y;
    }


    /**
     * DragGridItemAdapter 设置数据用的adapter
     *
     * @param <T>
     */
    public abstract static class DragGridItemAdapter<T> {
        private DragGridView dragGridView;

        public abstract View onCreateItemView(ViewGroup parent);

        public abstract void onBindView(View view, int position);

        public int getAdapterType() {
            return 0;
        }

        public abstract int getItemCount();

        private void attachDragGridView(DragGridView dragGridView) {
            this.dragGridView = dragGridView;
        }

        public DragGridView getDragGridView() {
            return dragGridView;
        }

        public void notifyChanged() {
            notifyAddItemViews();
        }

        private void notifyAddItemViews() {
            this.dragGridView.removeAllViews();
            for (int i = 0; i < getItemCount(); i++) {
                View itemview = onCreateItemView(dragGridView);
                onBindView(itemview, i);
                this.dragGridView.addView(itemview);
            }
        }
    }

/**===================================提供给外部的方法start================================*/

    /**
     * 设置列数
     *
     * @param col
     */
    public void setCol(int col) {
        this.mCol = col;
        requestLayout();
    }

    /**
     * 设置DragGridItemAdapter
     *
     * @param adapter
     */
    public void setDragGridItemAdapter(DragGridItemAdapter adapter) {
        this.mDragGridIteAdapter = adapter;
        this.mDragGridIteAdapter.attachDragGridView(this);
        this.mDragGridIteAdapter.notifyAddItemViews();
    }
/**==================================提供给外部的方法end========================================*/

/**==================================工具函数start=====================================*/

    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return 屏幕宽px
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高px
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


/**=====================================工具函数end==================================*/

}
