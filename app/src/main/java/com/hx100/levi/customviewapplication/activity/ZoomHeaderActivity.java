package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.hx100.levi.customviewapplication.R;

/**
 * Created by Levi on 2016/11/7.
 */
public class ZoomHeaderActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomheader);
        init();
    }

    private void init() {
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
