package com.example.jason.lab5;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Infor extends AppCompatActivity {
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infor);
        // Get the Intent that started this activity and extract the string
        Bundle bundle = this.getIntent().getExtras();
        final String name = bundle.getString("name");
        final String key = bundle.getString("key");

        Resources resources = getResources();
        // 设置人物名称
        TextView textView = (TextView) findViewById(R.id.charactor_name);
        textView.setText(name);

        ImageView imageView = (ImageView) findViewById(R.id.charactor);
        // 获得人物图片的id
        int ImageId = resources.getIdentifier(key, "drawable", getPackageName());
        Drawable drawable = resources.getDrawable(ImageId);
        imageView.setImageDrawable(drawable);

        // 返回图标的点击事件
        ImageView image_back = (ImageView)findViewById(R.id.back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Infor.this.finish();     // 关闭Activity
            }
        });

    }
}
