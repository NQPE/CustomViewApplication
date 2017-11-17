package com.hx100.levi.customviewapplication.controldemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.hx100.levi.customviewapplication.MainActivity;
import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.controldemo.autotest.AdbDevice;
import com.hx100.levi.customviewapplication.controldemo.autotest.element.Element;
import com.hx100.levi.customviewapplication.controldemo.autotest.element.Position;
import com.hx100.levi.customviewapplication.controldemo.autotest.utils.ShellUtils;
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

        startControl();
        return START_STICKY;
    }

    private void startControl() {
//        Observable.interval(5, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        LogUtil.i("long=="+aLong);
//                        String path= Environment.getExternalStorageDirectory()+"/customuiautomator/uidump.xml";
////        ShellUtils.suShell("chmod 777 "+Environment.getExternalStorageDirectory()+"/customuiautomator");
//                        ShellUtils.suShell("uiautomator dump "+path);
//                        sleep(2000);
//                        AdbDevice adb = new AdbDevice();
////                        adb.tap(565 ,255);
//                        Position position = new Position();
////                        Element element=position.findElementByText("测试按钮");
////                        adb.tap(element);
//                    }
//                });
        Intent intent = new Intent();
        ComponentName cmp=new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        startActivity(intent);
        sleep(2000);
        try {
            AdbDevice adb = new AdbDevice();
            String path= Environment.getExternalStorageDirectory()+"/customuiautomator/uidump.xml";
//        ShellUtils.suShell("chmod 777 "+Environment.getExternalStorageDirectory()+"/customuiautomator");
//            ShellUtils.suShell("uiautomator dump "+path);
//            sleep(4000);
//            Position position = new Position();
//            Element element=position.findElementByContentdesc("搜索");
//            adb.tap(element);
//            sleep(200);
            adb.tap(300,150);
            sleep(2000);
//            adb.shell("input text wey");
            CommonUtils.execRootCmd("input text wey");
            sleep(2000);
//            ShellUtils.suShell("uiautomator dump "+path);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.i(e.getMessage());
        }

    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
