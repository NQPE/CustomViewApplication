package com.hx100.levi.customviewapplication.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * Created by levi on 2016/3/21.
 */
public class LogUtil {
    private static boolean LOGFLAG=true;//总开关
    private static boolean LOGV = true;
    private static boolean LOGD = true;
    private static boolean LOGI = true;
    private static boolean LOGW = true;
    private static boolean LOGE = true;
    private static String TAG="tag";

    public static void v(String tag, String mess) {
        if (LOGFLAG&&LOGV) { Log.v(tag, mess); }
    }
    public static void d(String tag, String mess) {
        if (LOGFLAG&&LOGD) { Log.d(tag, mess); }
    }
    public static void i(String tag, String mess) {
        if (LOGFLAG&&LOGI) { Log.i(tag, mess); }
    }
    public static void w(String tag, String mess) {
        if (LOGFLAG&&LOGW) { Log.w(tag, mess); }
    }
    public static void e(String tag, String mess) {
        if (LOGFLAG&&LOGE) { Log.e(tag, mess); }
    }
    public static void v(String mess) {
        if (LOGFLAG&&LOGV) { Log.v(TAG, mess); }
    }
    public static void d(String mess) {
        if (LOGFLAG&&LOGD) { Log.d(TAG, mess); }
    }
    public static void i(String mess) {
        if (LOGFLAG&&LOGI) { logI(TAG, mess); }
    }
    public static void w(String mess) {
        if (LOGFLAG&&LOGW) { Log.w(TAG, mess); }
    }
    public static void e(String mess) {
        if (LOGFLAG&&LOGE) { Log.e(TAG, mess); }
    }

    //当需要打印的一条log信息太多，而IDE无法全部打印出来时 使用分段打印
    public static void logI(String tag, String content) {
        int p = 2000;
        long length = content.length();
        if (length < p || length == p)
            Log.i(tag, content);
        else {
            while (content.length() > p) {
                String logContent = content.substring(0, p);
                content = content.replace(logContent, "");
                Log.i(tag, logContent);
            }
            Log.i(tag, content);
        }
    }


    public static void init(Context context){
        LOGFLAG = isApkDebugable(context);
    }
    /**
     * 但是当我们没在AndroidManifest.xml中设置其debug属性时:
     * 使用Eclipse运行这种方式打包时其debug属性为true,使用Eclipse导出这种方式打包时其debug属性为法false.
     * 在使用ant打包时，其值就取决于ant的打包参数是release还是debug.
     * 因此在AndroidMainifest.xml中最好不设置android:debuggable属性置，而是由打包方式来决定其值.
     *
     * @param context
     * @return
     * @author SHANHY
     * @date   2015-8-7
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info= context.getApplicationInfo();
            return (info.flags& ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {

        }
        return false;
    }
}