package com.hx100.levi.customviewapplication.controldemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.hx100.levi.customviewapplication.MainActivity;
import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.PendingIntent.getActivity;

/**
 * Created by jun on 2017/11/9.
 */

public class ControlService extends Service{
    InputEventsUtil inputEvents;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i("ControlService--onCreate");
        init();
    }

    private void init() {
        inputEvents=new InputEventsUtil();

    }

    /**
     * 每次通过startService()方法启动Service时都会被回调。
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i("ControlService--onStartCommand");
        /**
         *创建Notification
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.icon_delete);
        builder.setContentTitle("前台服务");
        builder.setContentText("这是前台服务");
        Intent intent1 = new Intent(this, ControlActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity
                (this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
//启动到前台
        startForeground(1, notification);

        startControl();
        return START_STICKY;
    }

    private void startControl() {
        Observable.interval(5, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        LogUtil.i("long=="+aLong);
                        inputEvents.a(0, SystemClock.uptimeMillis(), 565, 200);
                        inputEvents.a(2, SystemClock.uptimeMillis(), 565, 210);
                        inputEvents.a(2, SystemClock.uptimeMillis(), 565, 220);
                        inputEvents.a(2, SystemClock.uptimeMillis(), 565, 230);
                        inputEvents.a(2, SystemClock.uptimeMillis(), 565, 240);
                        inputEvents.a(2, SystemClock.uptimeMillis(), 565, 250);
                        inputEvents.a(2, SystemClock.uptimeMillis(), 565, 260);
                        inputEvents.a(1, SystemClock.uptimeMillis(), 565, 260);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
