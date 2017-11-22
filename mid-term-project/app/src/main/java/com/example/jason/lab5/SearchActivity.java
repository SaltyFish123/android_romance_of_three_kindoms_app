package com.example.jason.lab5;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    //data用于存放所有的数据
    private List<Map<String, Object>> data = new ArrayList<>();
    //tempdata用于存放每次的查询结果
    private List<Map<String, Object>> tempdata = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_search);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //初始化data的数据
        initialSearchPageData();
        final CommonAdapter adapter =
                new CommonAdapter<Map<String, Object>>(this, R.layout.item_list, tempdata) {
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
                Intent intent = new Intent(SearchActivity.this, Infor.class);
                Bundle bundle = new Bundle(); // 携带数据
                Map<String, Object> item = tempdata.get(position);
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
        mRecyclerView.setAdapter(adapter);

        // 返回图标的点击事件
        ImageButton image_back = (ImageButton) findViewById(R.id.back_button);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();     // 关闭Activity
            }
        });

        ImageButton image_search = (ImageButton)findViewById(R.id.search_show_btn);
        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchPageData(adapter); // set the data of recyclerview accotding to the text
                if (tempdata.size() > 0) {
                    adapter.notifyItemRangeInserted(0, tempdata.size());
                }
        }
        });
    }

    public void initialSearchPageData() {
        String[] charactor_name = new String[] { "曹操", "刘备", "孙权",
                "董卓", "袁绍","张角", "周瑜", "诸葛亮", "司马懿" , "华佗"  };
        String[] key = new String[] { "caocao", "liubei", "sunquan",
                "dongzhuo", "yuanshao", "zhangjiao", "zhouyu", "zhugeliang",
                "simayi", "huatuo"};
        String[] kingdom = new String[] { "wei", "shu", "wu", "qun", "qun",
                "qun", "wu", "shu", "wei", "qun"};
        // initial the data of rectclerview
        for (int i = 0; i < 10; i++) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("name", charactor_name[i]);
            temp.put("key", key[i]);
            temp.put("kingdom", kingdom[i]);
            data.add(temp);
        }
    }

    public void setSearchPageData(CommonAdapter adapter) {
        EditText editText = (EditText)findViewById(R.id.edit_query);
        String text = editText.getText().toString();
        int count = tempdata.size();
        tempdata.clear();
        adapter.notifyItemRangeRemoved(0, count);
        if (text.equals("蜀") || text.equals("蜀国")) {
            for (Map<String, Object> item : data) {
                if (item.get("kingdom").toString().equals("shu")) {
                    tempdata.add(item);
                }
            }
        } else if (text.equals("魏") || text.equals("魏国")) {
            for (Map<String, Object> item : data) {
                if (item.get("kingdom").toString().equals("wei")) {
                    tempdata.add(item);
                }
            }
        } else if (text.equals("吴") || text.equals("吴国")) {
            for (Map<String, Object> item : data) {
                if (item.get("kingdom").toString().equals("wu")) {
                    tempdata.add(item);
                }
            }
        } else if (text.equals("群") || text.equals("群雄")) {
            for (Map<String, Object> item : data) {
                if (item.get("kingdom").toString().equals("qun")) {
                    tempdata.add(item);
                }
            }
        } else {
            for (Map<String, Object> item : data) {
                if (item.get("name").toString().equals(text)) {
                    tempdata.add(item);
                }
            }
        }
    }
}
