package com.example.jason.lab5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // 返回图标的点击事件
        ImageButton image_back = (ImageButton) findViewById(R.id.back_button_add);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity.this.finish();     // 关闭Activity
            }
        });


    }
}
