package com.rjwl.reginet.xinread.activity;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.adapter.BookStackAdapter;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.BookStack;
import com.rjwl.reginet.xinread.presenter.BooksStackPresenter;
import com.rjwl.reginet.xinread.presenter.MyBooksView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanle on 2018/5/15.
 * 书库
 */

public class BookStackActivity extends BaseActivity implements MyBooksView {

    //测试
    private ListView lvBookStack;
    private List<Book> bookList;
    private List<BookStack> bookStackList;
    private BookStackAdapter bookStackAdapter;

    private BooksStackPresenter booksStackPresenter;

    @Override
    void initView() {
        title.setText("书库");
        booksStackPresenter = new BooksStackPresenter(BookStackActivity.this);
        //用假数据初始化集合
        initList();

        lvBookStack = findViewById(R.id.lv_book_stack);
        bookStackAdapter = new BookStackAdapter(bookStackList, BookStackActivity.this);
        lvBookStack.setAdapter(bookStackAdapter);


    }

    @Override
    int getResId() {
        return R.layout.activity_book_stack;
    }

    private void initList() {
        //测试数据
       // bookList = new ArrayList<>();
        bookStackList = new ArrayList<>();
        booksStackPresenter.getBooksStack();
        /*Book book1 = new Book();
        book1.setBookname("天涯明月刀");
        book1.setAuthor("古龙");
        book1.setPrice("10.04");
        book1.setNum("22.8");
        book1.setImg("http://pic2.52pk.com/files/141115/1283496_110744_1.jpg");
        book1.setDetail("《天涯明月刀》的故事是傅红雪拯救自我、拯救他人最终达到自我升华的过程，" +
                "天涯、明月、刀三个词是傅红雪给人的三个意向，" +
                "天涯般辽阔的胸襟，明月般剔透的心灵加上一把“无敌于天下”的快刀。" +
                "代表着古龙在低谷期时挣扎、痛苦过但依然热爱着生活的一种温暖乐观的心态。");
        Book book2 = new Book();
        book2.setBookname("三体");
        book2.setAuthor("刘慈欣");
        book2.setPrice("25.8");
        book2.setNum("10.2");
        book2.setImg("http://www.artsbj.com/UploadFiles/2014-06/wq/2014060414234167686.jpg");
        book2.setDetail("《三体》是刘慈欣创作的系列长篇科幻小说，由《三体》、《三体Ⅱ·黑暗森林》、" +
                "《三体Ⅲ·死神永生》组成，作品讲述了地球人类文明和三体文明的信息交流、" +
                "生死搏杀及两个文明在宇宙中的兴衰历程。");
        Book book3 = new Book();
        book3.setBookname("流星蝴蝶剑");
        book3.setAuthor("古龙");
        book3.setPrice("15.04");
        book3.setNum("20.3");
        book3.setImg("http://y2.ifengimg.com/haina/2015_39/2dbbfd12c465c50_w559_h800.jpg");
        book3.setDetail(" 主要讲述了“老伯”孙玉伯的孙府与万鹏王的十二飞鹏帮相互对峙，" +
                "以及孟星魂、小蝶、孙玉伯、律香川、高老大等人各怀心思，" +
                "相互算计，恩恩怨怨，爱恨情仇的故事。");
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
        BookStack bookStack1 = new BookStack("一年级", bookList);
        BookStack bookStack2 = new BookStack("二年级", bookList);
        BookStack bookStack3 = new BookStack("三年级", bookList);
        BookStack bookStack4 = new BookStack("四年级", bookList);
        bookStackList.add(bookStack1);
        bookStackList.add(bookStack2);
        bookStackList.add(bookStack3);
        bookStackList.add(bookStack4);*/
    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("");
            progressDialog.setMessage("请稍后...");
            progressDialog.show();
            //progressDialog = ProgressDialog.show(this, "", "请稍后...");
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
        Toast.makeText(BookStackActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void skipView() {

    }

    @Override
    public void showBooks() {
        List<BookStack> list = booksStackPresenter.getBookStackList();
        if (list != null) {
            bookStackList.addAll(list);
        }
        Log.d("TAG1", "我的书架集合：" + bookList);
        bookStackAdapter.notifyDataSetChanged();
    }

    @Override
    public void bookShop() {

    }
}
