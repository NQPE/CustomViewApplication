package com.hx100.levi.customviewapplication.itemtouchhelperdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.itemtouchhelperdemo.demochannel.ChannelActivity;
import com.hx100.levi.customviewapplication.itemtouchhelperdemo.demodrag.DragActivity;

/**
 * Created by Levi on 2017/1/10.
 */
public class ItemTouchHelperDemoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemtouchhelper);

        Button mBtnDrag = (Button) findViewById(R.id.btn_drag);
        Button mBtnChannel = (Button) findViewById(R.id.btn_channl);
        mBtnDrag.setOnClickListener(this);
        mBtnChannel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_drag:
                startActivity(new Intent(ItemTouchHelperDemoActivity.this, DragActivity.class));
                break;
            case R.id.btn_channl:
                startActivity(new Intent(ItemTouchHelperDemoActivity.this, ChannelActivity.class));
                break;
        }
    }
}
