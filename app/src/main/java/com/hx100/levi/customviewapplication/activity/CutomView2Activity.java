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
public class CutomView2Activity extends Activity implements View.OnClickListener{
    Button btn_change;
    Button btn_change1;
    CirclePercentView circle_percent_view;
    CirclePercent1View circle_percent_view1;
    Random random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview2);
        init();
    }

    private void init() {
        random=new Random();
        btn_change= (Button) findViewById(R.id.btn_change);
        btn_change.setOnClickListener(this);
        btn_change1= (Button) findViewById(R.id.btn_change1);
        btn_change1.setOnClickListener(this);
        circle_percent_view= (CirclePercentView) findViewById(R.id.circle_percent_view);
        circle_percent_view1= (CirclePercent1View) findViewById(R.id.circle_percent_view1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_change:
                circle_percent_view.setPercentIndexByAnimation(random.nextInt(100));
                break;
            case R.id.btn_change1:
                circle_percent_view1.setPercentIndexByAnimation(random.nextInt(100));
                break;

        }
    }
}
