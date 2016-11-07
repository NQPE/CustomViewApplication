package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.os.Bundle;

import com.hx100.levi.customviewapplication.R;

/**
 * 新手自定义view练习实例之（一） 泡泡弹窗
 *
 * Created by Levi on 2016/11/7.
 */
public class CutomView1Activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview1);
        init();
    }

    private void init() {

    }
}
