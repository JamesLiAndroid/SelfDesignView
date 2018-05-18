package com.practice.jamesliandroid.selfdesignview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.practice.jamesliandroid.selfdesignview.learning.BeiSaierActivity;
import com.practice.jamesliandroid.selfdesignview.learning.LearningActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLearn = findViewById(R.id.btn_learn);
        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LearningActivity.class);
                startActivity(intent);
            }
        });

        Button btnLearn2 = findViewById(R.id.btn_learn_2);
        btnLearn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BeiSaierActivity.class);
                startActivity(intent);
            }
        });
    }
}
