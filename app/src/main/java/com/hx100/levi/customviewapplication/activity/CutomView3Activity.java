package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.customview.CirclePercent1View;
import com.hx100.levi.customviewapplication.customview.CirclePercentView;

import java.util.Random;

/**
 * Created by Levi on 2016/11/7.
 */
public class CutomView3Activity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview3);
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
