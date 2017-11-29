package com.example.jason.lab5;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class SearchActivity extends AppCompatActivity {
    //tempdata用于存放每次的查询结果
    private List<Map<String, Object>> tempdata = new ArrayList<>();
    private DBHelper dbHelper;
    private CommonAdapter adapter;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("人物信息搜索");

        relativeLayout = (RelativeLayout)findViewById(R.id.hint);
        relativeLayout.setVisibility(View.VISIBLE);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<Map<String, Object>>(this, R.layout.item_list, tempdata) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> s) {
                TextView name = holder.getView(R.id.name);
                name.setText(s.get("name").toString());
                ImageView first = holder.getView(R.id.first);
                ImageView kingdom_belongs = holder.getView(R.id.kingdom);
                String picture = s.get("key").toString();
                String kingdom = s.get("kingdom").toString();
                int pic_id = 0;
                try {
                    pic_id = getResources().getIdentifier(picture, "drawable", getPackageName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (pic_id == 0) {
                    Log.i("RUNNING", "warning: cannot find the image.");
                    pic_id = getResources().getIdentifier("unknown", "drawable", getPackageName());
                }
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
                Intent intent = new Intent(SearchActivity.this, Infor.class);
                Bundle bundle = new Bundle(); // 携带数据
                Map<String, Object> item = tempdata.get(position);
                String item_name = item.get("name").toString();
                String item_key = item.get("key").toString();
                bundle.putString("name", item_name);
                bundle.putString("key", item_key);
                bundle.putString("kingdom", item.get("kingdom").toString());
                intent.putExtras(bundle); // 附带上额外数据
                startActivity(intent);
            }
            @Override
            public void onLongClick(int position) {
                Toast.makeText(getApplication(), "移除第" + String.valueOf(position) + "个人物",
                        Toast.LENGTH_SHORT).show();
                tempdata.remove(position);
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
                if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    Toast.makeText(getApplicationContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    recyclerView.smoothScrollBy(-50, 0);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

    public void setSearchPageData(CommonAdapter adapter, String text) {
        int count = tempdata.size();
        tempdata.clear();
        adapter.notifyItemRangeRemoved(0, count);
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor;
        if (text.equals("蜀") || text.equals("蜀国")) {
            cursor = dbHelper.query_kingdom(new String[] {"shu"});
        } else if (text.equals("魏") || text.equals("魏国")) {
            cursor = dbHelper.query_kingdom(new String[] {"wei"});
        } else if (text.equals("吴") || text.equals("吴国")) {
            cursor = dbHelper.query_kingdom(new String[] {"wu"});
        } else if (text.equals("群") || text.equals("群雄")) {
            cursor = dbHelper.query_kingdom(new String[] {"qun"});
        } else {
            cursor = dbHelper.query_name(new String[] {text});
        }
        try {
            while (cursor.moveToNext()) {
                // initial the data of rectclerview
                Map<String, Object> temp = new LinkedHashMap<>();
                temp.put("name", cursor.getString(1));
                temp.put("kingdom", cursor.getString(2));
                temp.put("key", cursor.getString(3));
                tempdata.add(temp);
            }
        } catch (Exception e) {
            Log.i("RUNNING", "error occurs when read data from database");
            e.printStackTrace();
        }
        dbHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.  
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search_show_btn);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("请输入角色名称或所属势力");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String text = searchView.getQuery().toString();
                Log.i("RUNNING", "search activity");
                relativeLayout.setVisibility(View.GONE);
                setSearchPageData(adapter, text); // set the data of recyclerview accotding to the text
                if (tempdata.size() > 0) {
                    adapter.notifyItemRangeInserted(0, tempdata.size());
                } else {
                    Toast.makeText(getApplicationContext(), "抱歉，没有找到相关结果！",
                            Toast.LENGTH_SHORT).show();
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Log.i("RUNNING", "back to Main activity");
                SearchActivity.this.finish();
                break;
        }
        return true;
    }
}
