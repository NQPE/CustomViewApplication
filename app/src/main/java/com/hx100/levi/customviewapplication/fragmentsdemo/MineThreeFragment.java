package com.hx100.levi.customviewapplication.fragmentsdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.hx100.levi.customviewapplication.R;

import butterknife.BindView;

/**
 * Created by geyifeng on 2017/5/12.
 */

public class MineThreeFragment extends BaseThreeFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), toolbar);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_one_mine;
    }

}
