package com.hx100.levi.customviewapplication;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by levi on 2016/3/21.
 */
public class MyLogUtil {
    private static String TAG="tag";

    public static void print(){
        log(TAG,"debug");
    }

    public static void log(String msg){
        log(TAG,msg);
    }

    public static void log(String tag,String msg){
        Log.i(tag,msg);
    }

    public static void main(String[] arges){
        String table="一乙二十丁厂七卜人入八九几儿了力乃刀又三于干亏士工土才寸下大丈与万上小口巾山千乞川亿个勺久凡及夕丸么广亡门义之尸弓己已子卫也女飞刃习叉马乡丰王井开夫天无元专云扎艺木五支厅不太犬区历尤友匹车巨牙屯比互切瓦止少日中冈贝内水见午牛手毛气升长仁什片仆化仇币仍仅斤爪反介父从今凶分乏公仓月氏勿欠风丹匀乌凤勾文六方火为斗忆订计户认心尺引丑巴孔队办以允予劝双书幻玉刊示末未击打巧正扑扒功扔去甘世古节本术可丙左厉右石布龙平灭轧东卡北占业旧帅归且旦目叶甲申叮电号田由史只央兄叼叫另叨叹四生失禾丘付仗代仙们仪白仔他斥瓜乎丛令用甩印乐";
        String pwd="义弓么丸广之";
        String mypwd="581026";
//        table=getTableFromPic();
//        pwd=getPwdFromPic();
//        System.out.println("table==" + table) ;
//        System.out.println("pwd==" + pwd) ;
//        mypwd=new String(aliCodeToBytes(table,pwd));
//        System.out.println("mypwd==" + mypwd) ;
        String pw=null;
        try {
            byte[] data=mypwd.getBytes("utf-8");
//            System.out.println("data==" + data) ;
            pw=bytesToAliSmsCode(table,data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("pw==" + pw) ;

    }

    public static String bytesToAliSmsCode(String table, byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            System.out.println("b==" + b) ;
//            System.out.println("b & 255==" + (b & 255)) ;
            sb.append(table.charAt(b & 255));
        }
        return sb.toString();
    }

    public static byte[] aliCodeToBytes(String codeTable, String strCmd) {
        byte[] cmdBuffer = new byte[strCmd.length()];
        for (int i = 0; i < strCmd.length(); i++) {
            cmdBuffer[i] = (byte) codeTable.indexOf(strCmd.charAt(i));
        }
        return cmdBuffer;
    }



    public static String getTableFromPic() {
        InputStream is = null;
        String value = "";
        try {
            File f= new File("D:\\AndroidStudio\\workspace\\CustomViewApplication\\app\\src\\main\\res\\assets\\logo.png") ;    // 声明File对象
            is=new FileInputStream(f);
//            is = getResources().getAssets().open("logo.png");
            int lenght = is.available();
            byte[] b = new byte[lenght];
            is.read(b, 0, lenght);
            byte[] data = new byte[768];
            System.arraycopy(b, 89473, data, 0, 768);
            String value2 = new String(data, "utf-8");
            if (is == null) {
                return value2;
            }
            try {
                is.close();
                return value2;
            } catch (IOException e) {
                return value2;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (is == null) {
                return value;
            }
            try {
                is.close();
                return value;
            } catch (IOException e3) {
                return value;
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
        }
        return null;
    }

    public static String getPwdFromPic() {
        InputStream is = null;
        String value = "";
        try {
            File f= new File("D:\\AndroidStudio\\workspace\\CustomViewApplication\\app\\src\\main\\res\\assets\\logo.png") ;    // 声明File对象
            is=new FileInputStream(f);
//            is = getResources().getAssets().open("logo.png");
            int lenght = is.available();
            byte[] b = new byte[lenght];
            is.read(b, 0, lenght);
            byte[] data = new byte[18];
            System.arraycopy(b, 91265, data, 0, 18);
            String value2 = new String(data, "utf-8");
            if (is == null) {
                return value2;
            }
            try {
                is.close();
                return value2;
            } catch (IOException e) {
                return value2;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (is == null) {
                return value;
            }
            try {
                is.close();
                return value;
            } catch (IOException e3) {
                return value;
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
        }

        return null;
    }


}