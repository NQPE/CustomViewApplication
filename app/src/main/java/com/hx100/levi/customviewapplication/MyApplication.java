package com.hx100.levi.customviewapplication;

import android.app.Application;

/**
 * Created by lenovo on 2017/7/25.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        MyLogUtil.print();

        MyLogUtil.log("1");

        MyLogUtil.log("2");

        MyLogUtil.log("tag","test");

    }
}
