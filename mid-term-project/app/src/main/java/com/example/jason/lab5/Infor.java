package com.example.jason.lab5;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Infor extends AppCompatActivity {
    private boolean flag = false;
    private ImageView back;
    private ImageView charactor;    //储存人物图片
    private Button edit;      //编辑按钮
    private Button delete;  //删除按钮
    private Button save;
    private Button cancel;
    private EditText charactor_name;      //人物名字
    private EditText gender;    //人物性别
    private EditText life;      //人物生年卒月
    private EditText place;     //人物籍贯
    private EditText belong;    //人物主效势力

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infor);
        findView();
        setListener();
        // Get the Intent that started this activity and extract the string
        Bundle bundle = this.getIntent().getExtras();
        final String name = bundle.getString("name");
        final String key = bundle.getString("key");

        Resources resources = getResources();
        // 设置人物名称
        charactor_name.setText(name);
        // 获得人物图片的id
        int ImageId = resources.getIdentifier(key, "drawable", getPackageName());
        Drawable drawable = resources.getDrawable(ImageId);
        charactor.setImageDrawable(drawable);
    }

    private void findView() {
        back = (ImageView)findViewById(R.id.back);
        charactor = (ImageView) findViewById(R.id.charactor);
        edit = (Button) findViewById(R.id.edit);
        delete = (Button) findViewById(R.id.delete);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        charactor_name = (EditText) findViewById(R.id.charactor_name);
        gender = (EditText) findViewById(R.id.charactor_gender);
        life = (EditText) findViewById(R.id.charactor_life);
        place = (EditText) findViewById(R.id.charactor_place);
        belong = (EditText) findViewById(R.id.charactor_belong);
    }

    private void setListener() {

        // 返回图标的点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Infor.this.finish();     // 关闭Activity
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Infor.this.finish();     // 关闭Activity
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeEditStatu(charactor_name);
                ChangeEditStatu(gender);
                ChangeEditStatu(life);
                ChangeEditStatu(place);
                ChangeEditStatu(belong);
                v.setVisibility(View.GONE);
                v.setEnabled(false);
                delete.setVisibility(View.GONE);
                delete.setEnabled(false);
                save.setVisibility(View.VISIBLE);
                save.setEnabled(true);
                cancel.setVisibility(View.VISIBLE);
                cancel.setEnabled(true);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeEditStatu(charactor_name);
                ChangeEditStatu(gender);
                ChangeEditStatu(life);
                ChangeEditStatu(place);
                ChangeEditStatu(belong);
                v.setVisibility(View.GONE);
                v.setEnabled(false);
                cancel.setVisibility(View.GONE);
                cancel.setEnabled(false);
                edit.setVisibility(View.VISIBLE);
                edit.setEnabled(true);
                delete.setVisibility(View.VISIBLE);
                delete.setEnabled(true);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeEditStatu(charactor_name);
                ChangeEditStatu(gender);
                ChangeEditStatu(life);
                ChangeEditStatu(place);
                ChangeEditStatu(belong);
                v.setVisibility(View.GONE);
                v.setEnabled(false);
                save.setVisibility(View.GONE);
                save.setEnabled(false);
                edit.setVisibility(View.VISIBLE);
                edit.setEnabled(true);
                delete.setVisibility(View.VISIBLE);
                delete.setEnabled(true);
            }
        });

    }
    private void  ChangeEditStatu(EditText et) {
        if(et.hasFocusable()) {
            et.setFocusable(false);
            et.setFocusableInTouchMode(false);
        } else {
            et.setFocusableInTouchMode(true);
            et.setFocusable(true);
            et.requestFocus();
        }
    }
}
