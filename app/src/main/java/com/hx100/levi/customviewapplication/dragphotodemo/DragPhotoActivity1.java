package com.hx100.levi.customviewapplication.dragphotodemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.utils.LogUtil;
import com.hx100.levi.customviewapplication.utils.SimpleUtils;

import java.util.ArrayList;

/**
 * Created by Levi on 2017/1/11.
 */
public class DragPhotoActivity1 extends AppCompatActivity implements View.OnClickListener{
    ArrayList<DragPhotoInfoVo> list;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.actvity_dragphoto1);
        init();
    }

    private void init() {
        imageView1= (ImageView) findViewById(R.id.imageView1);
        imageView2= (ImageView) findViewById(R.id.imageView2);
        imageView3= (ImageView) findViewById(R.id.imageView3);
        findViewById(R.id.imageView1).setOnClickListener(this);
        findViewById(R.id.imageView2).setOnClickListener(this);
        findViewById(R.id.imageView3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView1:
                SimpleUtils.skipActivity(this,DragPhotoActivity2.class,getList(1),0);
                break;
            case R.id.imageView2:
                SimpleUtils.skipActivity(this,DragPhotoActivity2.class,getList(2),1);
                break;
            case R.id.imageView3:
                SimpleUtils.skipActivity(this,DragPhotoActivity2.class,getList(3),0);
                break;
        }
    }

    private ArrayList<DragPhotoInfoVo> getList(int type){
        list=new ArrayList<DragPhotoInfoVo>();
        switch (type){
            case 1:
            case 2:
                DragPhotoInfoVo vo1=new DragPhotoInfoVo();
                vo1.height=imageView1.getHeight();
                vo1.width=imageView1.getWidth();
                vo1.resId=R.drawable.wugeng;
                int location1[] = new int[2];
                imageView1.getLocationOnScreen(location1);
                vo1.left=location1[0];
                vo1.top=location1[1];
//                LogUtil.i("top1=="+vo1.top+"  left1=="+vo1.left);
                DragPhotoInfoVo vo2=new DragPhotoInfoVo();
                vo2.height=imageView2.getHeight();
                vo2.width=imageView2.getWidth();
                vo2.resId=R.drawable.pic2;
                int location2[] = new int[2];
                imageView2.getLocationOnScreen(location2);
                vo2.left=location2[0];
                vo2.top=location2[1];
//                LogUtil.i("top2=="+vo2.top+"  left2=="+vo2.left);
                list.add(vo1);
                list.add(vo2);
                break;
            case 3:
                DragPhotoInfoVo vo3=new DragPhotoInfoVo();
                vo3.height=imageView3.getHeight();
                vo3.width=imageView3.getWidth();
                vo3.resId=R.drawable.pic3;
                int location3[] = new int[2];
                imageView3.getLocationOnScreen(location3);
                vo3.left=location3[0];
                vo3.top=location3[1];
//                LogUtil.i("top3=="+vo3.top+"  left3=="+vo3.left);
                list.add(vo3);
                break;
        }


        return list;
    }
}
