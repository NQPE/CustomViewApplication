package com.hx100.levi.customviewapplication.controldemo;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by levi on 2017/11/20.
 */
public class FastInputIME extends InputMethodService {
    private static FastInputIME fastInputIME = null;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i("FastInputIME onCreate");
        fastInputIME = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i("FastInputIME onStartCommand");
        fastInputIME = this;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public View onCreateInputView() {
        // 装载keyboard.xml文件
//        View view = getLayoutInflater().inflate(R.layout.test_input, null);
//        view.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendText("中文测试111");
//            }
//        });
//        return view;
        return null;
    }

    public static void sendText(final String msg) {
        if (fastInputIME == null) {
            LogUtil.i("fastInputIME==null");
            return;
        }
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String aLong) {
                        // 获得InputConnection对象
                        InputConnection inputConnection = fastInputIME.getCurrentInputConnection();
                        LogUtil.i("fastInputIME sendText msg=="+msg);
                        if (inputConnection==null){
                            LogUtil.i("fastInputIME inputConnection==null");
                            return;
                        }
                        inputConnection.commitText(msg, 1);
                    }
                });

    }
}
