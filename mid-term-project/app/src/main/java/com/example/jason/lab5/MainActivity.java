package com.example.jason.lab5;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //data用于存放recyclerview中的数据
    private List<Map<String, Object>> data = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("RUNNING", "MAIN");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setMainPageData(); // initial the data of recyclerview

        final CommonAdapter adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_list, data) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> s) {
                TextView name = holder.getView(R.id.name);
                name.setText(s.get("name").toString());
                ImageView first = holder.getView(R.id.first);
                ImageView kingdom_belongs = holder.getView(R.id.kingdom);
                String picture = s.get("key").toString();
                String kingdom = s.get("kingdom").toString();
                int pic_id = getResources().getIdentifier(picture, "drawable", getPackageName());
                int kingdom_num = getResources().getIdentifier(kingdom, "drawable", getPackageName());
                Drawable pic_show = getResources().getDrawable(pic_id);
                Drawable kingdom_pic = getResources().getDrawable(kingdom_num);
                first.setImageDrawable(pic_show);
                kingdom_belongs.setImageDrawable(kingdom_pic);
            }
        };
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, Infor.class);
                Bundle bundle = new Bundle(); // 携带数据
                Map<String, Object> item = data.get(position);
                String item_name = item.get("name").toString();
                String item_key = item.get("key").toString();
                bundle.putString("name", item_name);
                bundle.putString("key", item_key);
                intent.putExtras(bundle); // 附带上额外数据
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
                Toast.makeText(getApplication(), "移除第" + String.valueOf(position) + "个人物",
                        Toast.LENGTH_SHORT).show();
                data.remove(position);
                adapter.notifyItemRemoved(position);//刷新被删除的地方
                if (position != adapter.getItemCount() - 1) {
                    adapter.notifyItemRangeChanged(position, adapter.getItemCount() - position);
                }
            }
        });

//        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(adapter);
//        animationAdapter.setDuration(1000);
        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());

        ImageButton search = (ImageButton)findViewById(R.id.search_button);
        ImageButton add = (ImageButton)findViewById(R.id.add_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("RUNNING", "SEARCH");
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("RUNNING", "ADD");
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setMainPageData() {
        dbHelper = new DBHelper(this);
        dbHelper.init();
        Cursor cursor = dbHelper.query();
        int count = 0;
        try {
            while (cursor.moveToNext() && count < 10) {
                // initial the data of rectclerview
                Map<String, Object> temp = new LinkedHashMap<>();
                temp.put("name", cursor.getString(1));
                temp.put("kingdom", cursor.getString(2));
                temp.put("key", cursor.getString(3));
                data.add(temp);
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}