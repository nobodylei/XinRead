package com.rjwl.reginet.xinread.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.adapter.BooksAdapter;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.presenter.BooksPresenter;
import com.rjwl.reginet.xinread.presenter.MyBooksView;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;
import com.vondear.rxtools.RxActivityTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8.
 * 书单推荐和我的书架
 */

public class BooksActivity extends BaseActivity implements MyBooksView {
    private ListView lvBooks;
    private List<Book> bookList;
    private BooksAdapter mBookAdapter;
    private String currTitle = "";
    private String btnTitle = "";
    private BooksPresenter booksPresenter;


    @Override
    public void initView() {
        booksPresenter = new BooksPresenter(BooksActivity.this);

        Intent intent = getIntent();
        currTitle = intent.getStringExtra("title");
        btnTitle = intent.getStringExtra("btn");

        Bundle bundle = intent.getBundleExtra("books");
        if (bundle != null) {//书单推荐
            bookList = (List<Book>) bundle.getSerializable("booklist");
            if (bookList == null) {
                bookList = new ArrayList<>();
            }
        } else {//我的书架
            bookList = new ArrayList<>();
            //initBook();
            booksPresenter.showMyBooks(SaveOrDeletePrefrence.lookInt(getApplicationContext(), "id") + "");
        }
        lvBooks = findViewById(R.id.lv_books);
        rlBase.setVisibility(View.VISIBLE);
        title.setText(currTitle);

        mBookAdapter = new BooksAdapter(BooksActivity.this, bookList, btnTitle, booksPresenter);
        lvBooks.setAdapter(mBookAdapter);
    }

    @Override
    public int getResId() {
        return R.layout.activity_books;
    }

    //初始化书籍信息
    private void initBook() {
        Book book1 = new Book();
        book1.setName("天涯明月刀");
        book1.setAuthor("古龙");
        book1.setPrice("10.04");
        book1.setNum("22.8");
        book1.setDetail("《天涯明月刀》的故事是傅红雪拯救自我、拯救他人最终达到自我升华的过程，" +
                "天涯、明月、刀三个词是傅红雪给人的三个意向，" +
                "天涯般辽阔的胸襟，明月般剔透的心灵加上一把“无敌于天下”的快刀。" +
                "代表着古龙在低谷期时挣扎、痛苦过但依然热爱着生活的一种温暖乐观的心态。");
        Book book2 = new Book();
        book2.setName("三体");
        book2.setAuthor("刘慈欣");
        book2.setPrice("25.8");
        book2.setNum("10.2");
        book2.setDetail(" 《三体》是刘慈欣创作的系列长篇科幻小说，由《三体》、《三体Ⅱ·黑暗森林》、" +
                "《三体Ⅲ·死神永生》组成，作品讲述了地球人类文明和三体文明的信息交流、" +
                "生死搏杀及两个文明在宇宙中的兴衰历程。");
        Book book3 = new Book();
        book3.setName("流星蝴蝶剑");
        book3.setAuthor("古龙");
        book3.setPrice("15.04");
        book3.setNum("20.3");
        book3.setDetail(" 主要讲述了“老伯”孙玉伯的孙府与万鹏王的十二飞鹏帮相互对峙，" +
                "以及孟星魂、小蝶、孙玉伯、律香川、高老大等人各怀心思，" +
                "相互算计，恩恩怨怨，爱恨情仇的故事。");
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
    }


    @Override
    public void showLoading() {
        if (progressDialog == null) {
            //progressDialog = ProgressDialog.show(this, "", "请稍后...");
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("");
            progressDialog.setMessage("请稍后...");
            progressDialog.show();
        } else if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog.setTitle("");
            progressDialog.setMessage("请稍后...");
        }
        progressDialog.show();
    }

    @Override
    public void hidenLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Log.d("TAG1", "我的书架Toast信息：" + msg);
        Toast.makeText(BooksActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void skipView() {

    }

    @Override
    public void showBooks() {
        List<Book> list = booksPresenter.getBookList();
        if (list != null) {
            bookList.addAll(list);
        }
        Log.d("TAG1", "我的书架集合：" + bookList);
        mBookAdapter.notifyDataSetChanged();
    }

    @Override
    public void bookShop() {

    }

}
