package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.customview.draggridview.DragGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by Levi on 2016/11/7.
 */
public class CutomView9Activity extends Activity{
    DragGridView draggridview;
    TextView tv_change;
    TextView tv_add;
    TextView tv_getdata;
    List<Item> data=new ArrayList<>();
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview9);
        init();
    }

    private void init() {
        draggridview= (DragGridView) findViewById(R.id.draggridview);
        draggridview.setCol(4);
        adapter=new MyAdapter();
        draggridview.setDragGridItemAdapter(adapter);
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

        for (int i=0;i<10;i++){
            Item item=new Item();
            item.title="频道"+i;
            data.add(item);
        }

        adapter.setDisableDragCount(2);
        adapter.setData(data);

    }

    public static class MyAdapter extends DragGridView.DragGridItemAdapter<Item>{


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
                    if (getDragGridView().getDragEnable()&&state==DragGridView.STATE_DRAG_ENABLE){
                        removeItem(view);
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

            if (state==DragGridView.STATE_DRAG_ENABLE){
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
