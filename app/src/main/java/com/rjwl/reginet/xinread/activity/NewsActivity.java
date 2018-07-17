package com.rjwl.reginet.xinread.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rjwl.reginet.xinread.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/28.
 * 小区公告
 */

public class NewsActivity extends BaseActivity {
    private SimpleDraweeView simpleDraweeView;
    private ListView lvNews;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private TextView tvNews;

    @Override
    public void initView() {
        //创建SimpleDraweeView对象
        title.setText("公告");
        simpleDraweeView = findViewById(R.id.img_news);
        lvNews = findViewById(R.id.lv_news);
        tvNews = findViewById(R.id.tv_news);
        list = new ArrayList<>();
        initData();

    }

    @Override
    int getResId() {
        return R.layout.activity_news;
    }

    private void initData() {

        list.add("明天放假");
        list.add("天天放假");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        lvNews.setAdapter(adapter);
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("news", "公告点击事件：" + list.get(position) + " ; " + "" + " ; " + position);
                //Toast.makeText(NewsActivity.this, "消息不存在", Toast.LENGTH_SHORT).show();
                tvNews.setVisibility(View.VISIBLE);
                tvNews.setText("公告消息:" + list.get(position));
                lvNews.setVisibility(View.GONE);
            }
        });
        //创建将要下载的图片的URI
        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUrl"));
        //开始下载
        simpleDraweeView.setImageURI(imageUri);
    }

    @Override
    public void onBackPressed() {
        if(tvNews.getVisibility() == View.VISIBLE) {
            tvNews.setVisibility(View.GONE);
            lvNews.setVisibility(View.VISIBLE);
            return;
        }
        super.onBackPressed();
    }
}
