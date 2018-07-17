package com.rjwl.reginet.xinread.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.activity.BookIntroActivity;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;

import java.util.List;

/**
 * Created by Administrator on 2018/5/18.
 */

public class BookLibraryAdapter extends BaseAdapter {
    private List<Book> mBookList;
    private Context mContext;

    public BookLibraryAdapter(Context context, List<Book> bookList) {
        this.mContext = context;
        this.mBookList = bookList;
    }

    @Override
    public int getCount() {
        return mBookList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBookList.get(position);
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
            row = LayoutInflater.from(mContext).inflate(R.layout.item_book_library, parent, false);
            //将布局文件解析成View的过程，需要资源
            wrapper = new Wrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (Wrapper) row.getTag();
        }
        SimpleDraweeView imgBook = wrapper.getImgBook();
        TextView bookName = wrapper.getBookName();
        TextView bookAuthro = wrapper.getBookAuthor();

        //通过position决定内容
        final Book book = mBookList.get(position);

        imgBook.setImageURI(Uri.parse(Constant.IMG + book.getHeadPic()));//Constant.URL +
        bookName.setText(book.getName() + "");
        bookAuthro.setText(book.getAuthor() + "");


        imgBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BookIntroActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", book);
                bundle.putString("btnTitle", "+加入书架");
                intent.putExtra("bookBundle", bundle);
                mContext.startActivity(intent);
            }
        });

        return row;
    }

    class Wrapper {
        //ImageView imgBook;
        SimpleDraweeView imgBook;
        TextView bookName;
        TextView bookAuthor;

        View row;

        public Wrapper(View row) {
            this.row = row;
        }

        public TextView getBookAuthor() {
            if(bookAuthor == null) {
                bookAuthor = row.findViewById(R.id.tv_book_library_author);
            }
            return bookAuthor;
        }

        public SimpleDraweeView getImgBook() {
            if (imgBook == null) {
                imgBook = row.findViewById(R.id.img_book_library);
            }
            return imgBook;
        }

        public TextView getBookName() {
            if (bookName == null) {
                //查找
                bookName = row.findViewById(R.id.tv_book_library_name);
            }
            return bookName;
        }
    }
}
