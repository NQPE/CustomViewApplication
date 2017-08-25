package com.hx100.levi.customviewapplication.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.hx100.levi.customviewapplication.R;
import com.hx100.levi.customviewapplication.customview.MentionEditText;
import com.hx100.levi.customviewapplication.utils.SimpleUtils;

import java.util.List;

/**
 * 新手自定义view练习实例之（一） 泡泡弹窗
 *
 * Created by Levi on 2016/11/7.
 */
public class CutomView10Activity extends Activity{
    MentionEditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customview10);
        init();
    }

    private void init() {
        editText= (MentionEditText) findViewById(R.id.met);
//        editText.setHorizontallyScrolling(false);
        List<String> mentionList = editText.getMentionList(true); //get a list of mention string
        editText.setMentionTextColor(Color.RED); //optional, set highlight color of mention string
        editText.setPattern("#[^#]+#"); //optional, set regularExpression
        editText.setOnMentionInputListener(new MentionEditText.OnMentionInputListener() {
            @Override
            public void onMentionCharacterInput() {
                //call when '@' character is inserted into EditText
                SimpleUtils.simpleToast(CutomView10Activity.this,"#");
            }
        });
    }
}
