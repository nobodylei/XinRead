package com.rjwl.reginet.xinread.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.adapter.MapAdapter;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.MyThinkMap;
import com.rjwl.reginet.xinread.presenter.ThinkMapView;
import com.rjwl.reginet.xinread.presenter.ThinkPresenter;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxBarTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 */

public class MindActivity extends BaseActivity implements ThinkMapView{
    private ListView lvThinkMap;
    private Book book;
    private MapAdapter mapAdapter;
    private List<MyThinkMap> myThinkMapList;

    private ThinkPresenter thinkPresenter;

    @Override
    void initView() {
        //RxBarTool.setTransparentStatusBar(this);
        title.setText("思维导图");
        book = (Book) getIntent().getBundleExtra("bookBundle").get("book");
        thinkPresenter = new ThinkPresenter(this);

        lvThinkMap = findViewById(R.id.lv_think_map);
        myThinkMapList = new ArrayList<>();
        //initList();
        mapAdapter = new MapAdapter(this, myThinkMapList, book);

        lvThinkMap.setAdapter(mapAdapter);

        thinkPresenter.getThinkMap(SaveOrDeletePrefrence.lookInt(getApplicationContext(), "id") + "", book.getId() + "");
    }

    private void initList() {
        MyThinkMap myThinkMap1 = new MyThinkMap();
        myThinkMap1.setScore(20);
        myThinkMap1.setTitle(book.getName() + "1");
        MyThinkMap myThinkMap2 = new MyThinkMap();
        myThinkMap2.setScore(20);
        myThinkMap2.setTitle(book.getName() + "2");
        MyThinkMap myThinkMap3 = new MyThinkMap();
        myThinkMap3.setScore(40);
        myThinkMap3.setTitle(book.getName() + "3");
        MyThinkMap myThinkMap4 = new MyThinkMap();
        myThinkMap4.setScore(50);
        myThinkMap4.setTitle(book.getName() + "4");
        MyThinkMap myThinkMap5 = new MyThinkMap();
        myThinkMap5.setScore(30);
        myThinkMap5.setTitle(book.getName() + "5");
        myThinkMapList.add(myThinkMap1);
        myThinkMapList.add(myThinkMap2);
        myThinkMapList.add(myThinkMap3);
        myThinkMapList.add(myThinkMap4);
        myThinkMapList.add(myThinkMap5);

    }

    @Override
    int getResId() {
        return R.layout.activity_mind_map;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hidenLoading() {

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(MindActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void skipView() {

    }

    @Override
    public void showThink() {
        List<MyThinkMap> myThinkMaps = thinkPresenter.getMyThinkMapList();
        if (myThinkMaps != null) {
            myThinkMapList.addAll(myThinkMaps);
        }
        mapAdapter.notifyDataSetChanged();
    }
}
