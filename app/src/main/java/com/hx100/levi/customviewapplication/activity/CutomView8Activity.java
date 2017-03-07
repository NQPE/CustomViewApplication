package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hx100.levi.customviewapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 *
 *
 * Created by Levi on 2016/11/7.
 */
public class CutomView8Activity extends Activity{
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView recyclerView;

    protected  MyAdapter adapter = null;
    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview8);
        init();
    }

    private void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        recyclerView = (RecyclerView) findViewById(R.id.rl_list);
        swipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
                        getResources().getDisplayMetrics()));
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        list=new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add("this is"+i+" item");
        }
        adapter=new MyAdapter(list);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });
    }

    public static class MyAdapter extends RecyclerView.Adapter{
        List data;
        public MyAdapter(List<String> data) {
            this.data=data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test, parent, false);
            return new ViewHolder1(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder1){
                ((ViewHolder1) holder).tv_item.setText((String)data.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView tv_item;
        RelativeLayout rl_item;

        public ViewHolder1(View itemView) {
            super(itemView);
            tv_item= (TextView) itemView.findViewById(R.id.tv_item);
            rl_item= (RelativeLayout) itemView.findViewById(R.id.rl_item);
        }
    }
}
