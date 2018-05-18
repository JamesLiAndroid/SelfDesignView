package com.practice.jamesliandroid.selfdesignview.learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.practice.jamesliandroid.selfdesignview.R;

/**
 * 自定义View学习过程
 * 1. 单纯用画笔绘制View
 */
public class LearningActivity extends AppCompatActivity implements SelfView2.AnimationIsComplete{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        SelfView2 selfView2 = findViewById(R.id.self_view_2);
        selfView2.setIsComplete(this);
    }

    @Override
    public void isComplete() {
        Log.d("TAG", "任务终结！");
       // Toast.makeText(this, "任务终结！", Toast.LENGTH_SHORT).show();
    }
}
