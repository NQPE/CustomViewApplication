package com.hx100.levi.customviewapplication.controldemo;

import android.hardware.input.InputManager;
import android.os.SystemClock;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jun on 2017/11/9.
 */

public class InputEventsUtil {
    Method a;
    InputManager b ;

    public InputEventsUtil(){
        try {
            MotionEvent.class.getDeclaredMethod("obtain", new Class[0]).setAccessible(true);
            this.a = InputManager.class.getMethod("injectInputEvent", new Class[] { InputEvent.class, Integer.TYPE });
            b=(InputManager)InputManager.class.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void a(KeyEvent paramKeyEvent)
    {
        try {
            this.a.invoke(this.b, new Object[] { paramKeyEvent, Integer.valueOf(0) });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public final void a(int paramInt)
    {
        long l = SystemClock.uptimeMillis();
        a(new KeyEvent(l, l, 0, paramInt, 0, 0, -1, 0, 0, 257));
        a(new KeyEvent(l, l, 1, paramInt, 0, 0, -1, 0, 0, 257));
    }

    public final void a(int paramInt1, int paramInt2)
    {
        long l = SystemClock.uptimeMillis();
        a(new KeyEvent(l, l, paramInt2, paramInt1, 0, 0, -1, 0, 0, 257));
    }

    public final void a(int paramInt, long paramLong, float paramFloat1, float paramFloat2)
    {
        MotionEvent localMotionEvent = MotionEvent.obtain(paramLong, paramLong, paramInt, paramFloat1, paramFloat2, 1.0F, 1.0F, 0, 1.0F, 1.0F, 0, 0);
        localMotionEvent.setSource(4098);
        try {
            this.a.invoke(this.b, new Object[] { localMotionEvent, Integer.valueOf(0) });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
