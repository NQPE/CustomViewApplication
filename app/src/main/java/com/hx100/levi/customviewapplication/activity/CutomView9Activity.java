package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.customview.draggridview.DragGridView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Created by Levi on 2016/11/7.
 */
public class CutomView9Activity extends Activity{
    protected DragGridView draggridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview9);
        init();
    }

    private void init() {
        draggridview= (DragGridView) findViewById(R.id.draggridview);
        draggridview.setCol(4);
        draggridview.setDragGridItemAdapter(new MyAdapter());
    }

    public static class MyAdapter extends DragGridView.DragGridItemAdapter{

        @Override
        public View onCreateItemView(ViewGroup parent) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draggridview_test, parent, false);
            return view;
        }

        @Override
        public void onBindView(View view, final int position) {
            ((TextView)view.findViewById(R.id.tv_item)).setText("频道"+position);
            ((TextView)view.findViewById(R.id.tv_item)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"频道"+position,Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}
