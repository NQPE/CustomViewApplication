package com.hx100.levi.customviewapplication.fragmentsdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.hx100.levi.customviewapplication.R;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/5/12.
 */

public class ServiceThreeFragment extends BaseThreeFragment implements View.OnClickListener{

    @BindView(R.id.fl_top)
    public FrameLayout fl_top;
    @BindView(R.id.ll_title_bar)
    public LinearLayout ll_title_bar;
    @BindView(R.id.v_status_bar)
    public View v_status_bar;
    @BindView(R.id.tv_title)
    public TextView tv_title;
    @BindView(R.id.ll_header)
    public LinearLayout ll_header;
    @BindView(R.id.ll_avatar)
    public LinearLayout ll_avatar;
    @BindView(R.id.rl_title_type)
    public RelativeLayout rl_title_type;
    @BindView(R.id.ll_title_type)
    public LinearLayout ll_title_type;
    @BindView(R.id.ll_title_type_hot)
    public LinearLayout ll_title_type_hot;
    @BindView(R.id.tv_title_type_hot)
    public TextView tv_title_type_hot;
    @BindView(R.id.v_title_type_hot)
    public View v_title_type_hot;
    @BindView(R.id.ll_title_type_nearby)
    public LinearLayout ll_title_type_nearby;
    @BindView(R.id.tv_title_type_nearby)
    public TextView tv_title_type_nearby;
    @BindView(R.id.v_title_type_nearby)
    public View v_title_type_nearby;
    @BindView(R.id.v_type_div)
    public View v_type_div;
    @BindView(R.id.fl_content)
    public FrameLayout fl_content;
    @BindView(R.id.viewPager)
    public CustomViewPager viewPager;
    private ArrayList<Fragment> mFragments;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ImmersionBar.setTitleBar(getActivity(), toolbar);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_one_service;
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        Type1Fragment type1Fragment = new Type1Fragment();
        Type2Fragment type2Fragment = new Type2Fragment();
        mFragments.add(type1Fragment);
        mFragments.add(type2Fragment);
    }

    @Override
    protected void initView() {
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        viewPager.setScroll(false);
        CoordinatorLayout.LayoutParams fl_contentLayoutParams = (CoordinatorLayout.LayoutParams) fl_content
                .getLayoutParams();
        fl_contentLayoutParams.setBehavior(new TypeHeaderBehavior(this));
    }

    @Override
    protected void setListener() {
        ll_title_type_hot.setOnClickListener(this);
        ll_title_type_nearby.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_type_hot:
                viewPager.setCurrentItem(0);
                tabSelected(0);
                break;
            case R.id.ll_title_type_nearby:
                viewPager.setCurrentItem(1);
                tabSelected(1);
                break;
        }
    }

    private void tabSelected(int type) {

    }

    private class MyAdapter extends FragmentPagerAdapter {
        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
