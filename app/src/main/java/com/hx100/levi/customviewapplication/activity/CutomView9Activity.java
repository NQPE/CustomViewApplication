package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.customview.draggridview.MyDragGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by Levi on 2016/11/7.
 */
public class CutomView9Activity extends Activity{
    MyDragGridView draggridview;
    MyDragGridView other_draggridview;
    TextView tv_change;
    TextView tv_add;
    TextView tv_getdata;
    List<Item> data=new ArrayList<>();
    List<Item> otherData=new ArrayList<>();
    MyAdapter adapter;
    OtherAdapter otherAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview9);
        init();
    }

    private void init() {
        setDragTitle();
        setDraggridview();
        setOtherDraggridview();


    }

    private void setOtherDraggridview() {
        other_draggridview= (MyDragGridView) findViewById(R.id.other_draggridview);
        other_draggridview.setCol(4);
        otherAdapter=new OtherAdapter();
        other_draggridview.setDragEnable(false);
        other_draggridview.setDragGridItemAdapter(otherAdapter);

//        for (int i=0;i<10;i++){
//            Item item=new Item();
//            item.title="其他频道"+i;
//            otherData.add(item);
//        }
//        otherAdapter.setData(otherData);
    }

    private void setDragTitle() {
        tv_change= (TextView) findViewById(R.id.tv_change);
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draggridview.setDragEnable(!draggridview.getDragEnable());
            }
        });
        tv_add= (TextView) findViewById(R.id.tv_add);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter!=null){
                    Item item=new Item();
                    item.title="频道"+draggridview.getChildCount();
                    adapter.addItem(item);
                }
            }
        });
        tv_getdata= (TextView) findViewById(R.id.tv_getdata);
        tv_getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter!=null&&adapter.getData()!=null){
                    Toast.makeText(v.getContext(),"size=="+adapter.getData().size(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDraggridview() {
        draggridview= (MyDragGridView) findViewById(R.id.draggridview);
        draggridview.setCol(4);
        adapter=new MyAdapter();
        draggridview.setDragGridItemAdapter(adapter);
        for (int i=0;i<10;i++){
            Item item=new Item();
            item.title="频道"+i;
            data.add(item);
        }

        adapter.setDisableDragCount(2);
        adapter.setData(data);
    }

    public  class MyAdapter extends MyDragGridView.DragGridItemAdapter<Item>{


        @Override
        public View onCreateItemView(ViewGroup parent) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draggridview_test, parent, false);
            return view;
        }

        @Override
        public void onBindView(final int state, final View view, final Item itemData) {
//            Log.i("tag","onBindView position=="+position);
            ((TextView)view.findViewById(R.id.tv_item)).setText(itemData.title);
            ((TextView)view.findViewById(R.id.tv_item)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(v.getContext(),itemData.title,Toast.LENGTH_SHORT).show();
                    if (getDragGridView().getDragEnable()&&state== MyDragGridView.STATE_DRAG_ENABLE){
//                        removeItem(view);
                        removeMoveItemToTarget(other_draggridview,view);
                    }
                }
            });
            ((TextView)view.findViewById(R.id.tv_item)).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!getDragGridView().getDragEnable()){
                        getDragGridView().setDragEnable(true);
                    }
//                    Toast.makeText(v.getContext(),"长按"+position,Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            if (state== MyDragGridView.STATE_DRAG_ENABLE){
                view.findViewById(R.id.iv_del).setVisibility(dragGridView.getDragEnable()?View.VISIBLE:View.GONE);
            }else {
                view.findViewById(R.id.iv_del).setVisibility(View.GONE);
                ((TextView)view.findViewById(R.id.tv_item)).setText("固定"+itemData.title);
            }
        }

    }
    public  class OtherAdapter extends MyDragGridView.DragGridItemAdapter<Item>{


        @Override
        public View onCreateItemView(ViewGroup parent) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draggridview_test, parent, false);
            return view;
        }

        @Override
        public void onBindView(final int state, final View view, final Item itemData) {
//            Log.i("tag","onBindView position=="+position);
            ((TextView)view.findViewById(R.id.tv_item)).setText(itemData.title);
            ((TextView)view.findViewById(R.id.tv_item)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeMoveItemToTarget(draggridview,view);
//                    Toast.makeText(v.getContext(),itemData.title,Toast.LENGTH_SHORT).show();
//                    if (getDragGridView().getDragEnable()&&state==DragGridView.STATE_DRAG_ENABLE){
//                        removeItem(view);
//                    }
                }
            });
//            ((TextView)view.findViewById(R.id.tv_item)).setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (!getDragGridView().getDragEnable()){
//                        getDragGridView().setDragEnable(true);
//                    }
////                    Toast.makeText(v.getContext(),"长按"+position,Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//            });

            if (state== MyDragGridView.STATE_DRAG_ENABLE){
                view.findViewById(R.id.iv_del).setVisibility(dragGridView.getDragEnable()?View.VISIBLE:View.GONE);
            }else {
                view.findViewById(R.id.iv_del).setVisibility(View.GONE);
                ((TextView)view.findViewById(R.id.tv_item)).setText("固定"+itemData.title);
            }
        }

    }

    public static class Item implements Serializable{
        public String title;
    }
}
