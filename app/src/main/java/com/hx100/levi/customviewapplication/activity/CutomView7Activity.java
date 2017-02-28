package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.customview.SmoothLineChart;
import com.hx100.levi.customviewapplication.customview.SmoothLineChartEquallySpaced;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Levi on 2016/11/7.
 */
public class CutomView7Activity extends Activity {
    Button btn_hook1;
    Button btn_hook2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview7);
        init();
    }

    private void init() {
        SmoothLineChart chart = (SmoothLineChart) findViewById(R.id.smoothChart);
        chart.setData(new PointF[] {
                new PointF(15, 39), // {x, y}
                new PointF(20, 21),
                new PointF(28, 9),
                new PointF(37, 21),
                new PointF(40, 25),
                new PointF(50, 31),
                new PointF(62, 24),
                new PointF(80, 28)
        }, true);

        SmoothLineChartEquallySpaced chartES = (SmoothLineChartEquallySpaced) findViewById(R.id.smoothChartES);
        chartES.setData(new float[] {
                15,
                21,
                9,
                21,
                25,
                35,
                24,
                28
        }, false);
    }

}
