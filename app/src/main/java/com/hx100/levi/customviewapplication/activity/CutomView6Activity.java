package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.customview.ScrollNumberView.MultiScrollNumberView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Levi on 2016/11/7.
 */
public class CutomView6Activity extends Activity {
    Button btn_hook1;
    Button btn_hook2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview6);
        init();
        hook(btn_hook2);
    }

    private void init() {
        btn_hook1= (Button) findViewById(R.id.btn_hook1);
        btn_hook2= (Button) findViewById(R.id.btn_hook2);
        btn_hook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"正常toast",Toast.LENGTH_SHORT).show();
            }
        });
        btn_hook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"正常toast",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hook(View view) {
        try {
            Class viewClazz=Class.forName("android.view.View");
            Method getListenerInfo=viewClazz.getDeclaredMethod("getListenerInfo");
            getListenerInfo.setAccessible(true);
            Object listenerInfo=getListenerInfo.invoke(view);

//            Class listenerInfoClazz=Class.forName("android.view.View$ListenerInfo");
            Field field=listenerInfo.getClass().getField("mOnClickListener");
            field.set(listenerInfo,new HookOnClickListener((View.OnClickListener) field.get(listenerInfo)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class HookOnClickListener implements View.OnClickListener{
        View.OnClickListener listener;
        public HookOnClickListener(View.OnClickListener listener){
            this.listener=listener;
        }
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.btn_hook2){
                Toast.makeText(v.getContext(),"hook成功",Toast.LENGTH_SHORT).show();
            }
            if (listener!=null){
                listener.onClick(v);
            }
        }
    }
}
