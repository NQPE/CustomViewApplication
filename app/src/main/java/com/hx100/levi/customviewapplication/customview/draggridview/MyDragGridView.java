package com.hx100.levi.customviewapplication.customview.draggridview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by levi on 2017/3/27.
 * 可拖拽的GridView
 */
public class MyDragGridView extends ViewGroup {
    public static final String TAG = "TAG_DragGridView";

    /**
     * item处于可滑动状态 但是未滑动
     */
    public static final int STATE_DRAG_ENABLE = 1000;
    /**
     * item不可滑动
     */
    public static final int STATE_DRAG_DISABLE =1001;
    /**
     * item处于正在滑动状态
     */
    public static final int STATE_DRAG_ING = 1002;


    @IntDef({STATE_DRAG_ENABLE, STATE_DRAG_DISABLE, STATE_DRAG_ING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ItemViewState {
    }

    //行(Row)
    int mRow = 1;
    //列(Col) 默认4列
    int mCol = 4;
    //view宽度
    int mWidth;
    //view高度
    int mHeight;
    //一个item的宽度
    int mItemWidth;
    //一个item的高度
    int mItemHeight;

    //记录down接触点的坐标
    float mDownX,mDownY;

    //整个viewgroup控件是否可拖动
    boolean mDragEnable = false;

    DragGridItemAdapter mDragGridIteAdapter;

    //拖动的itemview
    View mDragGridItemView;
    //K 为itemview V 为itemview对应的信息
    HashMap<View, InfoAttachItemView> mMapInfoItemView;
    //K 为position V 为对应的坐标信息
    HashMap<Integer, XYAttachPosition> mMapXYPosition ;
    //最小滑动距离
    int mTouchSlop;
    //是否可拖动控件切换的监听
    OnDragEnableListener mOnDragEnableListener;
    /**
     * item挤压移动 动画持续时间
     * 默认200
     */
    int mMoveAnimDuration=200;

    //处理长按改变拖拽状态时无缝拖拽view的问题
    boolean mLongClickDragEnable;

    //拖动item与其他item重叠范围判断
    float mOverlapScale=1/8;

    //数据管理manager
    DragViewDataManager mDataManager=new DragViewDataManager();

    public MyDragGridView(Context context) {
        this(context, null);
    }

    public MyDragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setChildrenDrawingOrderEnabled(true);
//        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setClipChildren(false);
        setClipToPadding(false);
        mTouchSlop= ViewConfiguration.get(getContext()).getScaledTouchSlop();
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
//        Log.i(TAG, "mRow==" + mRow);
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
        if (mMapXYPosition!=null){
            layoutChildViews(childCount);
            return;
        }
        calculateMapXY(childCount);
        layoutChildViews(childCount);

    }

    private void calculateMapXY(int childCount) {
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
//            child.layout(lc, tc, rc, bc);

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

    private void layoutChildViews(int childCount) {
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int lc=mMapXYPosition.get(mMapInfoItemView.get(child).itemPosition).X;
            int tc=mMapXYPosition.get(mMapInfoItemView.get(child).itemPosition).Y;
            int rc=lc+mItemWidth;
            int bc=tc+mItemHeight;
//                Log.i(TAG,"i=="+i+"  lc=="+lc+"  tc=="+tc);
            child.layout(lc, tc, rc, bc);
        }
    }


    @Override
    public void addView(View child, int index, LayoutParams params) {
        Log.i(TAG,"addView(View child, int index, LayoutParams params)");
        super.addView(child, index, params);
    }

    @Override
    public void removeViewAt(int index) {
        Log.i(TAG,"removeViewAt(int index)");
        super.removeViewAt(index);
    }

    @Override
    public void removeView(View view) {
        Log.i(TAG,"removeView(View view)");
        super.removeView(view);
    }

    @Override
    public void removeAllViews() {
        Log.i(TAG,"removeAllViews()");
        super.removeAllViews();
    }
    /**
     * 初始化MapInfo 存储itemview的position 信息
     *
     * @param child
     * @param i
     */
    private void initMapInfoItemView(View child, int i,int state,Object itemData) {
        if (mMapInfoItemView==null){
            mMapInfoItemView = new HashMap<>();
        }
        InfoAttachItemView itemInfo = new InfoAttachItemView();
        itemInfo.itemPosition = i;
        itemInfo.itemState =state;
        itemInfo.itemData=itemData;
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
        if (mMapXYPosition==null){
            mMapXYPosition=new HashMap<>();
        }
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                Log.i(TAG," dispatchTouchEvent Down");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG," dispatchTouchEvent Move");
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.i(TAG," dispatchTouchEvent Up");
//                break;
//        }
        if (mLongClickDragEnable){
//            Log.i(TAG,"mLongClickDragEnable");
            mLongClickDragEnable=false;
            // 重新dispatch一次down事件，使得拖拽itemview可以无缝拖拽
            int oldAction = ev.getAction();
            ev.setAction(MotionEvent.ACTION_DOWN);
            dispatchTouchEvent(ev);
            ev.setAction(oldAction);
        }
        return super.dispatchTouchEvent(ev);
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
        if (!mDragEnable)return false;
        //获取action
        final int action = MotionEventCompat.getActionMasked(ev);
        //获取action对应的index
        final int actionIndex = MotionEventCompat.getActionIndex(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                final float x = ev.getX();
                final float y = ev.getY();
                mDragGridItemView = findTopChildUnder((int) x, (int) y);
                if (checkDisableDrag(mDragGridItemView)){
                    mDragGridItemView=null;
                }
                if (mDragGridItemView != null) {
                    if (getParent()!=null){
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    //重写刷新draw的缓存 好重新排序drwaing
                    invalidate();
                }
                mDownX=x;
                mDownY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (checkTouchSlop(ev.getX(),ev.getY())){
                    return true;
                }

                break;
        }

        return false;
    }

    /**
     * 验证view是否是disable状态
     * @param itemview
     * @return
     */
    private boolean checkDisableDrag(View itemview) {
        //防止null意外报错
        if (mMapInfoItemView.get(itemview)==null)return true;
        return mMapInfoItemView.get(itemview).itemState==STATE_DRAG_DISABLE;
    }

    /**
     * 检查是否是超出最大距离
     * @param x
     * @param y
     * @return
     */
    private boolean checkTouchSlop(float x, float y) {
        if (Math.abs(y-mDownY)>mTouchSlop||Math.abs(x-mDownX)>mTouchSlop){
            return true;
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
                //这里-mItemWidth/2 主要是为了滑动的时候不是以左上角为起点
                //否则拖动的item的移动中心在左上角而不是中心点
                final float x = ev.getX()-mItemWidth/2;
                final float y = ev.getY()-mItemHeight/2;

                if (mDragGridItemView != null) {
                    //拖拽至指定位置
                    dragTo(mDragGridItemView.getLeft(), mDragGridItemView.getTop(), (int) x, (int) y);
                    //处理挤压动画
                    handleMoveAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                handleUpAnimation();
                break;
        }

        return true;
    }

    /**
     * 抬起手指 拖动的view复原
     */
    private void handleUpAnimation() {
        if (mDragGridItemView==null)return;
        creatMoveAnimator(mDragGridItemView,
                mMapXYPosition.get(mMapInfoItemView.get(mDragGridItemView).itemPosition).X,
                mMapXYPosition.get(mMapInfoItemView.get(mDragGridItemView).itemPosition).Y)
        .start();
        mDragGridItemView = null;
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
        int itemPosition=mMapInfoItemView.get(mDragGridItemView).itemPosition;
        if (overlapPosition==itemPosition)return;
        AnimatorSet animatorSet=createMoveAnimation(overlapPosition,itemPosition);
        handleMoveData(overlapPosition,itemPosition);
        Message msg=new Message();
        msg.obj=animatorSet;
        handlerMoveAnimation.sendMessageDelayed(msg,mMoveAnimDuration);
    }

    /**
     * 创建挤压动画
     * @param overlapPosition
     * @param itemPosition
     */
    private AnimatorSet createMoveAnimation(int overlapPosition, int itemPosition) {
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(mMoveAnimDuration);
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
        int lastPostion=mMapXYPosition.size()-1;

        //遍历map中的键
        for (Integer key : mMapXYPosition.keySet()) {
            if (dragitemcenterx >= mMapXYPosition.get(key).X + mItemWidth *mOverlapScale && dragitemcenterx < mMapXYPosition.get(key).X + mItemWidth * (1-mOverlapScale) &&
                    dragitemcentery >= mMapXYPosition.get(key).Y + mItemHeight *mOverlapScale && dragitemcentery < mMapXYPosition.get(key).Y + mItemHeight * (1-mOverlapScale)) {
                position = key;
                break;
            }
        }

        if (position==-1){
            //超出下边界 也算是 覆盖在了最后一个item上
            if (dragitemcentery>mMapXYPosition.get(lastPostion).Y+mItemHeight){
                return lastPostion;
            }else if (dragitemcenterx>mMapXYPosition.get(lastPostion).X+mItemWidth&&
                    dragitemcentery>mMapXYPosition.get(lastPostion).Y){
                //在最后一个item的右边 也算是覆盖
                return lastPostion;
            }
        }

        //判断itemview state是否可拖动
        if (position!=-1){
            if (checkDisableDrag(getItemViewByPosition(position))){
                return -1;
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
     * 采用itemview.layout来移动
     * @param itemview  需要移动的view
     * @param afterX  需要移动去的X
     * @param afterY  需要移动去的Y
     * @return
     */
    private ValueAnimator creatMoveAnimator(final View itemview, final int afterX, final int afterY) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(mMoveAnimDuration);
        animator.setTarget(itemview);
        final int l = itemview.getLeft();
        final int offset_l =afterX- l;
        final int t = itemview.getTop();
        final int offset_t = afterY-t;
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
     *采用itemview.setTranslationX/Y来移动
     * @param itemview  需要移动的view
     * @param afterX  需要移动去的X
     * @param afterY  需要移动去的Y
     * @return
     */
    private ValueAnimator creatMoveAnimation(final View itemview, final int afterX, int afterY) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(360);
        animator.setTarget(itemview);
        final int l = itemview.getLeft();
        final int offset_l =afterX- l;
        final int t = itemview.getTop();
        final int offset_t = afterY-t;
        Log.i(TAG,"offset_l=="+offset_l);
        Log.i(TAG,"offset_t=="+offset_t);
        Log.i(TAG,"l=="+l);
        Log.i(TAG,"t=="+t);
        Log.i(TAG,"afterX=="+afterX);
        Log.i(TAG,"afterY=="+afterY);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction= (float) animation.getAnimatedValue();
                itemview.setTranslationX(evaluateInt(animatedFraction, 0, offset_l));
                itemview.setTranslationY(evaluateInt(animatedFraction, 0, offset_t));
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
     * 处理OnDragEnableListener
     */
    private void handleOnDragEnableListener() {
        if (mOnDragEnableListener==null)return;
        mOnDragEnableListener.onDragEnableListener(mDragEnable);
    }

    /**
     * 处理OnBindItemView
     */
    private void handleOnBindItemView() {
        if (mDragGridIteAdapter==null)return;
//        Log.i(TAG,"handleOnBindItemView  mMapInfoItemView.size()=="+mMapInfoItemView.size());
        for (int i=0;i<mMapInfoItemView.size();i++){
            View item=getItemViewByPosition(i);
            mDragGridIteAdapter.onBindView(mMapInfoItemView.get(item).itemState,item,mMapInfoItemView.get(item).itemData);
        }
    }

    /**
     * 处理因为长按而改变拖拽状态时 itemview无法无缝连接的成为拖动的itemview
     * 必须还要抬手再次触摸滑动才可拖动的问题
     *
     * 重新dispatch一次down事件，使得拖拽itemview可以无缝拖拽
     *
     */
    private void handleDispatchTouchEvent() {
        if (mDragEnable){
            mLongClickDragEnable=true;
        }
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
     * 得到最后一个item的下一个item的XY坐标
     * 这个XY坐标是相对于屏幕来的
     * 主要用于添加item的移动动画的准备
     * @param itemView
     * @return
     */
    private XYAttachPosition creatPrepareAddLastItemXY(View itemView) {
        XYAttachPosition xy=new XYAttachPosition();
        //每个item的平均宽度
        int childAverageWidth = (mWidth - getPaddingLeft() - getPaddingRight()) / mCol;
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int[] location=new int[2];
        //得到child的MarginLayoutParams
        MarginLayoutParams lp = (MarginLayoutParams) itemView.getLayoutParams();
        int lastIndex=mMapXYPosition.size()-1;
        if (mItemHeight==0){
            mItemHeight=itemView.getMeasuredHeight();
        }
        if (lastIndex>0){
            getItemViewByPosition(lastIndex).getLocationOnScreen(location);
            if ((lastIndex+1)%mCol==0){
                location[0]=location[0]-childAverageWidth*(mCol-1);
            }else {
                location[0]=location[0]+childAverageWidth;
                location[1]=location[1]-(mItemHeight+ lp.bottomMargin + lp.topMargin);
            }
        }else {
            this.getLocationOnScreen(location);
            location[0]=location[0]+left;
            location[1]=location[1]+top;
        }

        xy.X=location[0];
        xy.Y=location[1];
        return xy;
    }


    /**
     * 数据管理类
     */
    public class DragViewDataManager {
        /**
         * 数据变化 引起的页面及数据重构
         */
        public void addItems(List data) {
            removeAllViews();
            for (int i = 0; i < data.size(); i++) {
                int state=i<mDragGridIteAdapter.disableDragCount?STATE_DRAG_DISABLE:STATE_DRAG_ENABLE;
                View itemview = mDragGridIteAdapter.onCreateItemView(mDragGridIteAdapter.getDragGridView());
                mDragGridIteAdapter.onBindView(state,itemview, data.get(i));
                //初始化MapInfo
                initMapInfoItemView(itemview, i,state, data.get(i));
                mDragGridIteAdapter.dragGridView.addView(itemview);
            }
        }

        /**
         * 删除某一个itemView
         * @param item
         */
        public void removeItem(final View item){
            int overlapPosition=mMapInfoItemView.size()-1;
            int itemPosition=mMapInfoItemView.get(item).itemPosition;
            AnimatorSet animatorSet=createMoveAnimation(overlapPosition,itemPosition);
            handleMoveData(overlapPosition,itemPosition);
            handleRemoveData(overlapPosition,item);
            handleDragView(item);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    item.setVisibility(INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    MyDragGridView.this.removeView(item);
                }
            });
            Message msg=new Message();
            msg.obj=animatorSet;
            handlerMoveAnimation.sendMessageDelayed(msg,0);
        }

        /**
         * 这里添加是只支持添加到最后一个
         *
         */
        public View addItem(Object itemData){
            View itemView=mDragGridIteAdapter.onCreateItemView(MyDragGridView.this);
            mDragGridIteAdapter.onBindView(STATE_DRAG_ENABLE,itemView,itemData);
            handleAddData(itemData,itemView);
            MyDragGridView.this.addView(itemView);
            return itemView;
        }

        /**
         * 返回重新排序后的数据集
         *
         */
        public List getData(){
            if (mMapInfoItemView==null||mMapInfoItemView.size()==0)return null;
            ArrayList list=new ArrayList();
            for (int i=0;i<mMapInfoItemView.size();i++){
                //遍历map中的键
                for (View key : mMapInfoItemView.keySet()) {
                    if (mMapInfoItemView.get(key).itemPosition==i) {
                        list.add(mMapInfoItemView.get(key).itemData);
                        break;
                    }
                }

            }
            return list;
        }

        /**
         * 处理添加数据
         */
        private void handleAddData(Object itemData, View itemView) {
            //mMapXYPosition重置为null 就会再次走一次layout里面的测量流程
            mMapXYPosition.clear();
            mMapXYPosition=null;
            //处理mMapInfoItemView
            InfoAttachItemView itemInfo=new InfoAttachItemView();
            itemInfo.itemData=itemData;
            itemInfo.itemState=STATE_DRAG_ENABLE;
            itemInfo.itemPosition=mMapInfoItemView.size();
            mMapInfoItemView.put(itemView,itemInfo);
        }


        private void handleDragView(View item) {
            if (mDragGridItemView!=null){
                mDragGridItemView=null;
                return;
            }
            mDragGridItemView=item;
            //重写刷新draw的缓存 好重新排序drwaing
            invalidate();
            //注意这里一定要给mDragGridItemView赋null值
            // 不然会持有item的引用导致报数组越界的错误
            mDragGridItemView=null;
        }

        private void handleRemoveData(int overlapPosition,View item) {
            mMapInfoItemView.remove(item);
            mMapXYPosition.remove(overlapPosition);
        }
    }
    /**
     * DragGridItemAdapter 设置数据用的adapter
     *
     * @param <T>
     */
    public abstract static class DragGridItemAdapter<T> {
        protected MyDragGridView dragGridView;
        protected int disableDragCount=0;

        /**
         * 设置数据集
         * 必须在DragGridView.setDragGridItemAdapter 之后使用
         * @param list
         */
        public void setData(List<T> list){
            if (dragGridView==null)return;
            dragGridView.mDataManager.addItems(list);
        }

        //关联dragGridView
        private void attachDragGridView(MyDragGridView dragGridView){
            this.dragGridView=dragGridView;
        }

        public abstract View onCreateItemView(ViewGroup parent);

        public abstract void onBindView(@ItemViewState int state,View view, T itemData);

        /**
         * 设置data排序的前N个item固定不能拖动
         *  注意 一定要在setdata之前
         * @return
         */
        public void setDisableDragCount(int count) {
            this.disableDragCount=count;
        }

        /**
         * 返回data排序的前N个item固定不能拖动
         * @return
         */
        public int getDisableDragCount() {
            return disableDragCount;
        }

        public MyDragGridView getDragGridView() {
            return dragGridView;
        }

        /**
         * 返回重新排序之后的data
         * @return
         */
        public List<T> getData(){
            return  dragGridView.mDataManager.getData();
        }

//        public void notifyChanged() {
//            dragGridView.mDataManager.addItems(getData());
//        }

        public void removeItem(View item){
            dragGridView.mDataManager.removeItem(item);
        }

        public View addItem(T itemData){
            return dragGridView.mDataManager.addItem(itemData);
        }

        /**
         * 移动某个view去某处
         * @param itemview
         * @param afterX
         * @param afterY
         * @param animAdapter
         */
        public void moveItemToPositon(final View itemview, final int afterX, int afterY,AnimatorListenerAdapter animAdapter){
            ValueAnimator animator=dragGridView.creatMoveAnimation(itemview,afterX,afterY);
            if (animAdapter!=null){
                animator.addListener(animAdapter);
            }
            animator.start();
        }

        /**
         * 删除某个view并添加到另外一个targetDragGridView里面
         * 必须保证itemData 即T 一致
         * @param itemview
         */
        public void removeMoveItemToTarget(final MyDragGridView target, final View itemview){
            XYAttachPosition xy=target.getPrepareAddLastItemXY(itemview);
            final Object t=dragGridView.mMapInfoItemView.get(itemview).itemData;
            final View addItemView=target.getAdapter().addItem(t);
            AnimatorListenerAdapter animAdapter=new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    addItemView.setVisibility(INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
//                    itemview.setVisibility(GONE);
                    addItemView.setVisibility(VISIBLE);
                    dragGridView.getAdapter().removeItem(itemview);
                }
            };
            moveItemToPositon(itemview,xy.X,xy.Y,animAdapter);
        }

    }

    /**
     * viewgroup控件在可拖动与不可拖动直接切换时触发的listener
     */
    public interface OnDragEnableListener{
        /**
         *
         * @param dragEnable
         */
         void onDragEnableListener(boolean dragEnable);
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
    }

    /**
     * 整个viewgroup控件是否可拖动子view
     *
     * @param enable
     */
    public void setDragEnable(boolean enable){
        mDragEnable=enable;
        handleOnDragEnableListener();
        handleOnBindItemView();
        handleDispatchTouchEvent();
    }


    /**
     * 得到是否是可拖动状态
     * @return
     */
    public boolean getDragEnable(){
        return mDragEnable;
    }

    /**
     * 设置是否可拖动子view的监听接口
     */
    public void setOnDragEnableListener(OnDragEnableListener listener){
        this.mOnDragEnableListener=listener;
    }

    public DragViewDataManager getDataManager(){
        return mDataManager;
    }

    /**
     * 返回adapter
     * @return
     */
    public DragGridItemAdapter getAdapter(){
        return mDragGridIteAdapter;
    }

    /**
     * 得到最后一个item的下一个item的XY坐标
     * 主要用于添加item的移动动画的准备
     * @param itemView
     * @return
     */
    public XYAttachPosition getPrepareAddLastItemXY(View itemView){

        return creatPrepareAddLastItemXY(itemView);
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

    /**
     * Integer 估值器
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public static Integer evaluateInt(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int) (startInt + fraction * (endValue - startInt));
    }

/**=====================================工具函数end==================================*/

}
