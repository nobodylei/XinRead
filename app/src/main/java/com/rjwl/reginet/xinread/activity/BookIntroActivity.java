package com.rjwl.reginet.xinread.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.presenter.BooksPresenter;
import com.rjwl.reginet.xinread.presenter.MyBooksView;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;
import com.vondear.rxtools.RxActivityTool;

/**
 * Created by Administrator on 2018/5/10.
 */

public class BookIntroActivity extends BaseActivity implements View.OnClickListener, MyBooksView {
    private Button btnBook;
    //private ImageView imgBook;
    private SimpleDraweeView imgBook;

    private TextView bookName;
    private TextView bookAuthor;
    private TextView bookNumber;
    private TextView bookPrice;

    private TextView tvBookIntro;
    private Button btnGame;
    private Button btnNote;
    private Button btnMindMap;
    private Button btnBuyBook;
    private View view;

    private Book book;//得到的书对象
    private BooksPresenter booksPresenter;
    private String id;


    @Override
    void initView() {
        booksPresenter = new BooksPresenter(BookIntroActivity.this);
        id = SaveOrDeletePrefrence.lookInt(getApplicationContext(), "id") + "";

        title.setText("书籍介绍");
        view = findViewById(R.id.in_book_item);

        imgBook = view.findViewById(R.id.img_item_book);
        btnBook = view.findViewById(R.id.btn_add_book);
        bookName = view.findViewById(R.id.tv_book_name);
        bookAuthor = view.findViewById(R.id.tv_book_author);
        bookNumber = view.findViewById(R.id.tv_book_number);
        bookPrice = view.findViewById(R.id.tv_book_price);

        tvBookIntro = findViewById(R.id.tv_book_intro);
        btnGame = findViewById(R.id.btn_book_game);
        btnNote = findViewById(R.id.btn_book_note);
        btnMindMap = findViewById(R.id.btn_mind_map);
        btnBuyBook = findViewById(R.id.bnt_buy_book);

        btnBook.setOnClickListener(this);
        btnGame.setOnClickListener(this);
        btnNote.setOnClickListener(this);
        btnMindMap.setOnClickListener(this);
        btnBuyBook.setOnClickListener(this);

        Bundle bundle = getIntent().getBundleExtra("bookBundle");
        String btnTitle = bundle.getString("btnTitle");
        book = (Book) bundle.get("book");

        Log.d("TAG1", "书籍路径：" + book.getHeadPic());
        Log.d("TAG1", "书籍id：" + book.getId());
        if ("移出书架".equals(btnTitle)) {
            btnBook.setVisibility(View.GONE);
        }

        imgBook.setImageURI(Uri.parse(Constant.IMG + book.getHeadPic()));
        bookName.setText(book.getName());
        bookAuthor.setText("作者:" + book.getAuthor());
        bookNumber.setText(book.getNum() + "");
        bookPrice.setText(book.getPrice() + "元/本");
        tvBookIntro.setText("\t\t" + book.getDetail());

        tvBookIntro.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    int getResId() {
        return R.layout.activity_book_intro;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_add_book:
                Log.d("TAG1", "加入书架");
                if ("移出书架".equals(btnBook.getText() + "")) {
                    booksPresenter.deleteBook(book.getId() + "",
                            SaveOrDeletePrefrence.lookInt(getApplicationContext(), "id") + "");
                    //btnBook.setText("+加入书架");
                } else {
                    booksPresenter.addBookList(SaveOrDeletePrefrence.lookInt(getApplicationContext(),
                            "id") + "", book.getId() + "");
                }
                break;
            case R.id.btn_book_game:
                intent = new Intent(BookIntroActivity.this, GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", book);
                intent.putExtra("bookBundle", bundle);
                startActivity(intent);
                Log.d("TAG1", "闯关游戏");
                break;
            case R.id.btn_book_note:
                Log.d("TAG1", "读书笔记");
                intent = new Intent(BookIntroActivity.this, CommentActivity.class);
                intent.putExtra("bookId", book.getId() + "");
                intent.putExtra("id", id);
                intent.putExtra("isBook", true);
                startActivity(intent);
                break;
            case R.id.btn_mind_map:
                Intent intent1 = new Intent(BookIntroActivity.this, MindActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("book", book);
                intent1.putExtra("bookBundle", bundle1);
                startActivity(intent1);
                Log.d("TAG1", "思维导图");
                break;
            case R.id.bnt_buy_book:
                Log.d("TAG1", "购买此书");
                String bookUri = book.getTvc();
                if (bookUri == null || "null".equals(bookUri)) {
                    Toast.makeText(BookIntroActivity.this, "链接失效", Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri uri = Uri.parse(bookUri.trim() + "");
                Log.d("TAG1", "书籍链接:" + uri.toString() + " ;" + bookUri);
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
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
        Toast.makeText(BookIntroActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void skipView() {

    }

    @Override
    public void showBooks() {
        btnBook.setText("已加入");
        btnBook.setEnabled(false);
    }

    @Override
    public void bookShop() {

    }
}
