package com.example.jason.lab5;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class MainActivity extends AppCompatActivity {
    //data用于存放recyclerview中的数据
    private List<Map<String, Object>> data = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("RUNNING", "MAIN");

        SharedPreferences sp = getSharedPreferences("first", Context.MODE_PRIVATE);
        String flag = sp.getString("flag", "firstStarted");
        if (flag.equals("firstStarted")) {
            sp.edit().putString("flag", "alreadyStarted").commit();
            DBHelper dbHelper = new DBHelper(this);
            dbHelper.init();
        }
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
        // 添加动画
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        alphaAdapter.setDuration(1000);
        mRecyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().
                        getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    Toast.makeText(getApplicationContext(), "滑动到底了", Toast.LENGTH_SHORT).show();
                    recyclerView.smoothScrollBy(-50, 0);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.  
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_button:
                Log.i("RUNNING", "go to add activity");
                Intent intent_add = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent_add);
                break;
            case R.id.search_button:
                Log.i("RUNNING", "go to search activity");
                Intent intent_sh = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent_sh);
                break;
        }
        return true;
    }

    public void setMainPageData() {
        Log.i("RUNNING", "initial datalist");
        DBHelper dbHelper = new DBHelper(this);
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
            Log.i("RUNNING", "error occurs when read data from database");
            e.printStackTrace();
        }
        dbHelper.close();
    }
}