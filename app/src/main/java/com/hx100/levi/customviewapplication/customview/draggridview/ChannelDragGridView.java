package com.hx100.levi.customviewapplication.customview.draggridview;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.widget.GridView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by levi on 2017/11/21.
 * 频道拖动的gridview
 */
public class ChannelDragGridView extends GridView{
    public static final String TAG = "TAG_ChannelDragGridView";
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


    public ChannelDragGridView(Context context) {
        super(context);
    }

    public ChannelDragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChannelDragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
