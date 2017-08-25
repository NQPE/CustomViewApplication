package com.hx100.levi.customviewapplication.fragmentsdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.hx100.levi.customviewapplication.R;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/5/8.
 */

public class FragmentThreeActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_category)
    LinearLayout ll_category;
    @BindView(R.id.ll_service)
    LinearLayout ll_service;
    @BindView(R.id.ll_mine)
    LinearLayout ll_mine;
    private ArrayList<Fragment> mFragments;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_fragment_one;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).navigationBarColor(R.color.colorPrimary).navigationBarWithKitkatEnable(false).init();
    }

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        HomeThreeFragment homeThreeFragment = new HomeThreeFragment();
        CategoryThreeFragment categoryThreeFragment = new CategoryThreeFragment();
        ServiceThreeFragment serviceThreeFragment = new ServiceThreeFragment();
        MineThreeFragment mineThreeFragment = new MineThreeFragment();
        mFragments.add(homeThreeFragment);
        mFragments.add(categoryThreeFragment);
        mFragments.add(serviceThreeFragment);
        mFragments.add(mineThreeFragment);
    }

    @Override
    protected void initView() {
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        ll_home.setSelected(true);
    }

    @Override
    protected void setListener() {
        ll_home.setOnClickListener(this);
        ll_category.setOnClickListener(this);
        ll_service.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                viewPager.setCurrentItem(0);
                tabSelected(ll_home);
                break;
            case R.id.ll_category:
                viewPager.setCurrentItem(1);
                tabSelected(ll_category);
                break;
            case R.id.ll_service:
                viewPager.setCurrentItem(2);
                tabSelected(ll_service);
                break;
            case R.id.ll_mine:
                viewPager.setCurrentItem(3);
                tabSelected(ll_mine);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                tabSelected(ll_home);
                mImmersionBar.statusBarDarkFont(false).navigationBarColor(R.color.colorPrimary).init();
                break;
            case 1:
                tabSelected(ll_category);
                mImmersionBar.statusBarDarkFont(true, 0.2f).init();
                break;
            case 2:
                tabSelected(ll_service);
                mImmersionBar.statusBarDarkFont(false).init();
                break;
            case 3:
                tabSelected(ll_mine);
                mImmersionBar.statusBarDarkFont(true).init();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void tabSelected(LinearLayout linearLayout) {
        ll_home.setSelected(false);
        ll_category.setSelected(false);
        ll_service.setSelected(false);
        ll_mine.setSelected(false);
        linearLayout.setSelected(true);
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
