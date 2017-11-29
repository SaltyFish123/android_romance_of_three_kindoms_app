package com.example.jason.lab5;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("添加新人物");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.  
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final EditText name = (EditText)findViewById(R.id.name_input);
        final EditText pingyin = (EditText)findViewById(R.id.pingyin_input);
        final EditText anotherName = (EditText)findViewById(R.id.anothername_input);
        final EditText birthDate = (EditText)findViewById(R.id.birth_input);
        final EditText birthPlace = (EditText)findViewById(R.id.birthplace_input);
        final RadioGroup sex = (RadioGroup)findViewById(R.id.sex_check);
        final RadioGroup kingdom = (RadioGroup)findViewById(R.id.kingdom_check);
        switch(item.getItemId()){
            case R.id.edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("确认添加吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("RUNNING", "add:save the information");
                        String[] values = new String[] {
                                name.getText().toString(), pingyin.getText().toString(),
                                anotherName.getText().toString(), birthDate.getText().toString(),
                                birthPlace.getText().toString()};
                        for (String s : values) {
                            if (s.isEmpty()) {
                                Toast.makeText(getApplicationContext(),
                                        "输入不能为空", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        int sex_type;
                        String kingdom_name;
                        switch (sex.getCheckedRadioButtonId()) {
                            case R.id.sex_man:
                                sex_type = 1;
                                break;
                            case  R.id.sex_woman:
                                sex_type = 0;
                                break;
                            default:
                                Toast.makeText(getApplicationContext(),
                                        "必须选择人物性别！", Toast.LENGTH_SHORT).show();
                                return;
                        }

                        switch (kingdom.getCheckedRadioButtonId()) {
                            case R.id.kingdom_wei:
                                kingdom_name = "wei";
                                break;
                            case R.id.kingdom_shu:
                                kingdom_name = "shu";
                                break;
                            case R.id.kingdom_wu:
                                kingdom_name = "wu";
                                break;
                            case  R.id.kingdom_qun:
                                kingdom_name = "qun";
                                break;
                            default:
                                Toast.makeText(getApplicationContext(),
                                        "必须选择人物所属势力！", Toast.LENGTH_SHORT).show();
                                return;
                        }
                        DBHelper dbHelper = new DBHelper(AddActivity.this);
                        ContentValues contentValues = new ContentValues();
                        try {
                            contentValues.put("name", values[0]);
                            contentValues.put("picture", values[1]);
                            contentValues.put("anotherName", values[2]);
                            contentValues.put("birthDate", values[3]);
                            contentValues.put("bornPlace", values[4]);
                            contentValues.put("sex", sex_type);
                            contentValues.put("kingdom", kingdom_name);
                            dbHelper.insert(contentValues);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(),
                                "提交成功！", Toast.LENGTH_SHORT).show();
                        Log.i("RUNNING", "Insert data into datanase");
                        AddActivity.this.finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "您取消了提交！", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
                break;
            case android.R.id.home:
                AddActivity.this.finish();
                break;
        }
        return true;
    }
}
