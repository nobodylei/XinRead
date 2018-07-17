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
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.activity.BookIntroActivity;
import com.rjwl.reginet.xinread.activity.ClassTaskActivity;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.presenter.BooksPresenter;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8.
 * 书单适配器
 */

public class BooksAdapter extends BaseAdapter {
    private Context mContext;
    private List<Book> mBookList;
    private String btnTitle;
    private BooksPresenter booksPresenter;

    public BooksAdapter(Context context, List<Book> bookList, String btnTitle, BooksPresenter booksPresenter) {
        this.mContext = context;
        this.mBookList = bookList;
        this.btnTitle = btnTitle;
        this.booksPresenter = booksPresenter;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        //返回项的View
        //持有者
        Wrapper wrapper = null;
        //解析成view
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(mContext).inflate(R.layout.item_books, parent, false);
            //将布局文件解析成View的过程，需要资源
            wrapper = new Wrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (Wrapper) row.getTag();
        }
        SimpleDraweeView imgBook = wrapper.getImgBook();
        TextView bookName = wrapper.getBookName();
        TextView bookAuthor = wrapper.getBookAuthor();
        TextView bookNumber = wrapper.getBookNumber();
        TextView bookPrice = wrapper.getBookPrice();
        final Button btnBooks = wrapper.getBtnBooks();
        //通过position决定内容
        final Book book = mBookList.get(position);

        imgBook.setImageURI(Uri.parse(Constant.IMG + book.getHeadPic()));
        bookName.setText(book.getName());
        bookAuthor.setText("作者:" + book.getAuthor());
        bookNumber.setText(book.getNum() + "");
        bookPrice.setText(book.getPrice() + "元/本");
        btnBooks.setText(btnTitle);

        btnBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("移出书架".equals(btnBooks.getText() + "")) {
                    Log.d("TAG1", "移出书架");
                    booksPresenter.deleteBook(book.getId() + "",
                            SaveOrDeletePrefrence.lookInt(mContext.getApplicationContext(), "id") + "");
                    if (mBookList.size() > 0) {
                        //加入或移除书架，更新界面
                        mBookList.remove(book);
                        notifyDataSetChanged();
                    }
                    Log.d("TAG1", "bookId:" + book.getId());
                    Log.d("TAG1", "StudentId:" + SaveOrDeletePrefrence.lookInt(mContext.getApplicationContext(), "id") + "");
                } else {
                    booksPresenter.addBookList(SaveOrDeletePrefrence.lookInt(mContext.getApplicationContext(), "id")
                            + "", book.getId() + "");
                    Log.d("TAG1", "加入书架");
                    //btnBooks.setText("已加入");
                    btnBooks.setEnabled(false);
                }

            }
        });

        imgBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BookIntroActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", mBookList.get(position));
                bundle.putString("btnTitle", btnTitle);
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
        TextView bookNumber;
        TextView bookPrice;
        Button btnBooks;

        View row;

        public Wrapper(View row) {
            this.row = row;
        }

        public SimpleDraweeView getImgBook() {
            if (imgBook == null) {
                imgBook = row.findViewById(R.id.img_item_book);
            }
            return imgBook;
        }

        public Button getBtnBooks() {
            if (btnBooks == null) {
                btnBooks = row.findViewById(R.id.btn_add_book);
            }
            return btnBooks;
        }

        public TextView getBookName() {
            if (bookName == null) {
                //查找
                bookName = row.findViewById(R.id.tv_book_name);
            }
            return bookName;
        }

        public TextView getBookAuthor() {
            if (bookAuthor == null) {
                //查找
                bookAuthor = row.findViewById(R.id.tv_book_author);
            }
            return bookAuthor;
        }

        public TextView getBookNumber() {
            if (bookNumber == null) {
                //查找
                bookNumber = row.findViewById(R.id.tv_book_number);
            }
            return bookNumber;
        }

        public TextView getBookPrice() {
            if (bookPrice == null) {
                //查找
                bookPrice = row.findViewById(R.id.tv_book_price);
            }
            return bookPrice;
        }
    }
}
