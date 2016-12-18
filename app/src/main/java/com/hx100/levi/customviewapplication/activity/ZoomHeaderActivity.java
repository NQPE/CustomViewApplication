package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.customview.zoomheaderview.ZoomHeaderView;
import com.hx100.levi.customviewapplication.customview.zoomheaderview.recyclerviewpager.RecyclerViewPager;
import com.hx100.levi.customviewapplication.customview.zoomheaderview.recyclerviewpager.RecyclerViewPagerAdapter;
import com.hx100.levi.customviewapplication.utils.LogUtil;

/**
 * Created by Levi on 2016/11/7.
 */
public class ZoomHeaderActivity extends Activity implements View.OnClickListener{
    public static final String TAG = "tag_ZoomHeaderActivity";
    ZoomHeaderView zoomHeaderView;
    RecyclerViewPager recyclerViewPager;
    NestedScrollView nestedScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomheader);
        init();
    }

    private void init() {
        zoomHeaderView= (ZoomHeaderView) findViewById(R.id.zoomHeaderView);
        nestedScrollView= (NestedScrollView) findViewById(R.id.nestedScrollView);
        recyclerViewPager= (RecyclerViewPager) findViewById(R.id.recyclerViewPager);
        recyclerViewPager.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewPager.setAdapter(new RecyclerViewPagerAdapter(recyclerViewPager,new ViewpagerAdapter()));
        zoomHeaderView.setViewPager(recyclerViewPager);
        zoomHeaderView.addOnZoomHeaderScrollListener(new ZoomHeaderView.OnZoomHeaderScrollListener() {
            @Override
            public void onZoomHeaderScrollListener(ZoomHeaderView view, float move) {

            }

            @Override
            public void onZoomHeaderStatusListener(ZoomHeaderView view, int status) {
                if (status==ZoomHeaderView.STATUS_BOTTOM){
                    finish();
                }
                if (status==ZoomHeaderView.STATUS_TOP){
                    LogUtil.i(TAG,"status==ZoomHeaderView.STATUS_TOP");
                    recyclerViewPager.setVisibility(View.INVISIBLE);
//                    recyclerViewPager.setAlpha(0);
                    nestedScrollView.findViewById(R.id.ll_header).setVisibility(View.VISIBLE);
                }
            }
        });
    }


    class ViewpagerAdapter extends RecyclerView.Adapter<ViewpagerAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            return new ViewHolder(inflater.inflate(R.layout.item_header1, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
            }
        }

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
