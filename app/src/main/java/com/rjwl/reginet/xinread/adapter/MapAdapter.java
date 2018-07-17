package com.rjwl.reginet.xinread.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.activity.ThinkMap;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.MyThinkMap;

import java.util.List;

/**
 * Created by Administrator on 2018/7/4.
 */

public class MapAdapter extends BaseAdapter {
    private Context mContext;
    private List<MyThinkMap> myThinkMaps;
    private Book book;

    public MapAdapter(Context context, List<MyThinkMap> myThinkMaps, Book book) {
        this.mContext = context;
        this.myThinkMaps = myThinkMaps;
        this.book = book;
    }


    @Override
    public int getCount() {
        return myThinkMaps.size();
    }

    @Override
    public Object getItem(int position) {
        return myThinkMaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //返回项的View
        //持有者
        Wrapper wrapper = null;
        //解析成view
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(mContext).inflate(R.layout.item_map, parent, false);
            //将布局文件解析成View的过程，需要资源
            wrapper = new Wrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (Wrapper) row.getTag();
        }
        TextView bookName = wrapper.getBookName();
        TextView score = wrapper.getTask();
        RelativeLayout rlThink = wrapper.getRlThink();

        //通过position决定内容
        bookName.setText(myThinkMaps.get(position).getTitle());
        if (myThinkMaps.get(position).getScore() > 0) {
            score.setText(myThinkMaps.get(position).getScore() + "分");
        }

        rlThink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ThinkMap.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", book);
                intent.putExtra("bookBundle", bundle);
                mContext.startActivity(intent);
            }
        });

        return row;
    }

    class Wrapper {
        //ImageView imgBook;
        TextView bookName;
        TextView task;
        RelativeLayout rlThink;

        View row;

        public Wrapper(View row) {
            this.row = row;
        }


        public TextView getBookName() {
            if (bookName == null) {
                bookName = row.findViewById(R.id.tv_think_map);
            }
            return bookName;
        }

        public TextView getTask() {
            if (task == null) {
                task = row.findViewById(R.id.tv_score);
            }
            return task;
        }
        public RelativeLayout getRlThink() {
            if (rlThink == null) {
                rlThink = row.findViewById(R.id.rl_think);
            }
            return rlThink;
        }
    }
}
