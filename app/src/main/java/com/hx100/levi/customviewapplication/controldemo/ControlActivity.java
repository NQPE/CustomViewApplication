package com.hx100.levi.customviewapplication.controldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.controldemo.autotest.utils.ShellUtils;
import com.hx100.levi.customviewapplication.utils.LogUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *
 * 代码模拟控制demo
 *
 */
public class ControlActivity extends Activity{
    TextView tv_start;
    TextView tv_test;
    InputEventsUtil inputEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        init();
    }

    private void init() {
        String path= Environment.getExternalStorageDirectory()+"/customuiautomator/";
        File file=new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        inputEvents=new InputEventsUtil();
        tv_start= (TextView) findViewById(R.id.tv_start);
        tv_test= (TextView) findViewById(R.id.tv_test);
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                control();
            }
        });
        tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"test",Toast.LENGTH_SHORT).show();
            }
        });
//        ShellUtils.suShell("chmod 777 "+Environment.getExternalStorageDirectory()+"/customuiautomator");
    }

    private void control() {
//        String readLine="tap 565 255";
//        String readLine="touch move 200 200";
//        if (readLine.startsWith("tap")) {
//            String[] split2 = readLine.substring(4).split(" ");
//            inputEvents.a(0, SystemClock.uptimeMillis(), Float.parseFloat(split2[0]), Float.parseFloat(split2[1]));
//            inputEvents.a(1, SystemClock.uptimeMillis(), Float.parseFloat(split2[0]), Float.parseFloat(split2[1]));
//        }
//        if (readLine.startsWith("touch move ")) {
//            String[] r5 = readLine.substring(11).split(" ");
//            inputEvents.a(2, SystemClock.uptimeMillis(), Float.parseFloat(r5[0]), Float.parseFloat(r5[1]));
//        }

//        String path= Environment.getExternalStorageDirectory()+"/customuiautomator/uidump.xml";
//        ShellUtils.suShell("chmod 777 "+Environment.getExternalStorageDirectory()+"/customuiautomator");
//        ShellUtils.suShell("uiautomator dump "+path);
//        sleep(2000);
        startService(new Intent(this.getApplicationContext(), ControlService.class));
    }
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
