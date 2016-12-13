package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.hx100.levi.customviewapplication.R;

/**
 * Created by Levi on 2016/11/7.
 */
public class CutomView4Activity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview4);
        init();
    }

    private void init() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
