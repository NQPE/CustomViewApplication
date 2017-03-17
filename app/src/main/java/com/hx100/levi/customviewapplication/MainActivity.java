package com.hx100.levi.customviewapplication;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hx100.levi.customviewapplication.activity.CutomView1Activity;
import com.hx100.levi.customviewapplication.activity.CutomView2Activity;
import com.hx100.levi.customviewapplication.activity.CutomView3Activity;
import com.hx100.levi.customviewapplication.activity.CutomView4Activity;
import com.hx100.levi.customviewapplication.activity.CutomView5Activity;
import com.hx100.levi.customviewapplication.activity.CutomView6Activity;
import com.hx100.levi.customviewapplication.activity.CutomView7Activity;
import com.hx100.levi.customviewapplication.activity.CutomView8Activity;
import com.hx100.levi.customviewapplication.activity.ScrollingActivity;
import com.hx100.levi.customviewapplication.dragphotodemo.DragPhotoActivity1;
import com.hx100.levi.customviewapplication.dragphotoviewdemo.DragPhotoDemoActivity;
import com.hx100.levi.customviewapplication.itemtouchhelperdemo.ItemTouchHelperDemoActivity;
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
        findViewById(R.id.btn_customview3).setOnClickListener(this);
        findViewById(R.id.btn_customview4).setOnClickListener(this);
        findViewById(R.id.btn_customview5).setOnClickListener(this);
        findViewById(R.id.btn_customview6).setOnClickListener(this);
        findViewById(R.id.btn_customview7).setOnClickListener(this);
        findViewById(R.id.btn_customview8).setOnClickListener(this);
        findViewById(R.id.btn_customview9).setOnClickListener(this);
        findViewById(R.id.btn_customview10).setOnClickListener(this);
        findViewById(R.id.btn_customview11).setOnClickListener(this);
        findViewById(R.id.btn_customview12).setOnClickListener(this);
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
            case R.id.btn_customview3:
                SimpleUtils.skipActivity(this,CutomView3Activity.class);
                break;
            case R.id.btn_customview4:
                SimpleUtils.skipActivity(this,CutomView4Activity.class);
                break;
            case R.id.btn_customview5:
                SimpleUtils.skipActivity(this,CutomView5Activity.class);
                break;
            case R.id.btn_customview6:
                SimpleUtils.skipActivity(this,ItemTouchHelperDemoActivity.class);
                break;
            case R.id.btn_customview7:
                SimpleUtils.skipActivity(this,DragPhotoDemoActivity.class);
                break;
            case R.id.btn_customview8:
                SimpleUtils.skipActivity(this,DragPhotoActivity1.class);
                break;
            case R.id.btn_customview9:
                SimpleUtils.skipActivity(this,CutomView6Activity.class);
                break;
            case R.id.btn_customview10:
                SimpleUtils.skipActivity(this,CutomView7Activity.class);
                break;
            case R.id.btn_customview11:
                SimpleUtils.skipActivity(this,CutomView8Activity.class);
                break;
            case R.id.btn_customview12:
                SimpleUtils.skipActivity(this,ScrollingActivity.class);
                break;

        }
    }
}
