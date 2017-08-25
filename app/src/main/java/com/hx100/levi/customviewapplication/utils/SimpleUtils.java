package com.hx100.levi.customviewapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.Serializable;

/**
 * Created by levi on 2016/7/26.
 */
public class SimpleUtils {

    public static void skipActivity(Activity source,Class<? extends Activity> cls, Serializable... serializ) {
        Intent intent = new Intent(source.getApplicationContext(), cls);
        Bundle extras = new Bundle();
        for (int i = 0; i < serializ.length; i++) {
            Serializable s = serializ[i];
            // 放对象的规则，以顺序为键
            extras.putSerializable(i + "", s);
        }
        intent.putExtras(extras);
        source.startActivity(intent);
    }

    public static Serializable getVo(Activity source,String key) {
        Intent myIntent = source.getIntent();
        Bundle bundle = myIntent.getExtras();
        if (bundle == null) {
            return "";
        }
        Serializable vo = bundle.getSerializable(key);
        return vo;
    }

    /**
     * 简单的无样式的toast
     * @param info
     */
    public static void simpleToast(Context context,String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}
