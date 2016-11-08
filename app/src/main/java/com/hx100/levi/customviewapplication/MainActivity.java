package com.hx100.levi.customviewapplication;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hx100.levi.customviewapplication.activity.CutomView1Activity;
import com.hx100.levi.customviewapplication.activity.CutomView2Activity;
import com.hx100.levi.customviewapplication.utils.SimpleUtils;


public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        findViewById(R.id.btn_customview1).setOnClickListener(this);
        findViewById(R.id.btn_customview2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_customview1:
                SimpleUtils.skipActivity(this,CutomView1Activity.class);
                break;
            case R.id.btn_customview2:
                SimpleUtils.skipActivity(this,CutomView2Activity.class);
                break;

        }
    }
}
