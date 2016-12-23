package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.customview.ScrollNumberView.MultiScrollNumberView;

/**
 * Created by Levi on 2016/11/7.
 */
public class CutomView5Activity extends Activity {
    EditText et1;
    EditText et2;
    Button submit;
    MultiScrollNumberView scrollnumview;
    int from;
    int to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview5);
        init();
    }

    private void init() {
        et1= (EditText) findViewById(R.id.et1);
        et2= (EditText) findViewById(R.id.et2);
        submit= (Button) findViewById(R.id.submit);
        scrollnumview= (MultiScrollNumberView) findViewById(R.id.scrollnumview);
        scrollnumview.setTextColors(new int[]{Color.BLACK});
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from=Integer.parseInt(et1.getText().toString());
                to=Integer.parseInt(et2.getText().toString());
                scrollnumview.setNumber(from,to);
            }
        });
    }

}
