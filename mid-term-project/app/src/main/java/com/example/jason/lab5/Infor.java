package com.example.jason.lab5;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Infor extends AppCompatActivity {
    private int id;
    private EditText anotherName_edit;
    private EditText birthDate_edit;
    private EditText bornPlace_edit;
    private Button check;
    private Button back;
    private String anotherName;
    private String birthDate;
    private String bornPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infor);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("人物详情");

        // Get the Intent that started this activity and extract the string
        Bundle bundle = this.getIntent().getExtras();
        final String name = bundle.getString("name");
        final String key = bundle.getString("key");
        final String kingdom = bundle.getString("kingdom");

        Resources resources = getResources();

        ImageView imageView = (ImageView) findViewById(R.id.charactor);
        // 获得人物图片的id
        int ImageId = 0;
        try {
            ImageId = getResources().getIdentifier(key, "drawable", getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ImageId == 0) {
            Log.i("RUNNING", "warning: cannot find the image.");
            ImageId = getResources().getIdentifier("unknown", "drawable", getPackageName());
        }
        Drawable drawable = resources.getDrawable(ImageId);
        imageView.setImageDrawable(drawable);

        imageView = (ImageView) findViewById(R.id.kingdom);
        ImageId = resources.getIdentifier(kingdom, "drawable", getPackageName());
        drawable = resources.getDrawable(ImageId);
        imageView.setImageDrawable(drawable);

        TextView textView = (TextView) findViewById(R.id.charactor_name);
        textView.setText(name);

        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.query_name(new String[]{name});
        textView = (TextView) findViewById(R.id.sex);
        try {
            while (cursor.moveToNext()) {
                id = cursor.getInt(0);
                int sex = cursor.getInt(cursor.getColumnIndex("sex"));
                if (sex == 0) {
                    textView.setText("女");
                } else {
                    textView.setText("男");
                }

                anotherName_edit = (EditText)findViewById(R.id.anothername);
                anotherName = cursor.getString(cursor.getColumnIndex("anotherName"));
                anotherName_edit.setText(anotherName);

                birthDate_edit = (EditText)findViewById(R.id.birthdate);
                birthDate = cursor.getString(cursor.getColumnIndex("birthDate"));
                birthDate_edit.setText(birthDate);

                bornPlace_edit = (EditText)findViewById(R.id.bornplace);
                bornPlace = cursor.getString(cursor.getColumnIndex("bornPlace"));
                bornPlace_edit.setText(bornPlace);
            }
        } catch (Exception e) {
            Log.i("RUNNING", "error occurs when read data from database");
            e.printStackTrace();
        }

        check = (Button)findViewById(R.id.check);
        back = (Button)findViewById(R.id.back);
        back.setVisibility(View.GONE);
        check.setVisibility(View.GONE);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(Infor.this);
                try {
                    ContentValues values = new ContentValues();
                    values.put("anotherName", anotherName_edit.getText().toString());
                    values.put("birthDate", birthDate_edit.getText().toString());
                    values.put("bornPlace", bornPlace_edit.getText().toString());
                    dbHelper.update(values, "name=?", new String[] {name});
                    Log.i("RUNNING", " modify data in database");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("RUNNING", "error occurs when update");
                    return;
                }
                anotherName_edit.setBackground(null);
                anotherName_edit.setEnabled(false);
                birthDate_edit.setBackground(null);
                birthDate_edit.setEnabled(false);
                bornPlace_edit.setBackground(null);
                bornPlace_edit.setEnabled(false);
                Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                v.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anotherName_edit.setBackground(null);
                anotherName_edit.setEnabled(false);
                anotherName_edit.setText(anotherName);
                birthDate_edit.setBackground(null);
                birthDate_edit.setEnabled(false);
                birthDate_edit.setText(birthDate);
                bornPlace_edit.setBackground(null);
                bornPlace_edit.setEnabled(false);
                bornPlace_edit.setText(bornPlace);
                Toast.makeText(getApplicationContext(), "您取消了修改！", Toast.LENGTH_SHORT).show();
                v.setVisibility(View.GONE);
                check.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.  
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.infor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify:
                anotherName_edit.setEnabled(true);
                birthDate_edit.setEnabled(true);
                bornPlace_edit.setEnabled(true);
                anotherName_edit.setBackgroundResource(R.drawable.unknown);
                birthDate_edit.setBackgroundResource(R.drawable.unknown);
                bornPlace_edit.setBackgroundResource(R.drawable.unknown);
                check.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                break;
            case android.R.id.home:
                Infor.this.finish();
                break;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("确认删除吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dbHelper = new DBHelper(Infor.this);
                        dbHelper.delete(id);
                        Toast.makeText(getApplicationContext(),
                                "删除成功！", Toast.LENGTH_SHORT).show();
                        Log.i("RUNNING", "delete data in database");
                        Intent intent = new Intent(Infor.this, MainActivity.class);
                        startActivity(intent);
                        Infor.this.finish();
                    }
                 });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                            "您取消了删除！", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
                break;
        }
        return true;
    }
}
