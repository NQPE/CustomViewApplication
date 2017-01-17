package com.hx100.levi.customviewapplication.dragphotodemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.utils.SimpleUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Levi on 2017/1/11.
 */
public class DragPhotoActivity2 extends AppCompatActivity{
    ArrayList<DragPhotoInfoVo> list;
    int index;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.actvity_dragphoto2);
        init();
    }

    private void init() {
        list= (ArrayList<DragPhotoInfoVo>) SimpleUtils.getVo(this,"0");
        index= (int) SimpleUtils.getVo(this,"1");
    }
}
