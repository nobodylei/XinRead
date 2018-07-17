package com.rjwl.reginet.xinread.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.utils.MyHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 * 书籍相关
 */

public class BooksPresenter extends BasePresenter {
    private MyBooksView myBooksView;
    private List<Book> bookList;

    private String tvc = "";

    public BooksPresenter(MyBooksView myBooksView) {
        this.myBooksView = myBooksView;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public String getTvc() {
        return tvc;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = msg.obj + "";
            myBooksView.hidenLoading();
            Log.d("TAG1", "BookPresenter层：" + message);
            if ("".equals(message)) {
                return;
            }
            switch (msg.what) {
                case 0:
                    myBooksView.showToast(message);
                    Log.d("TAG1", "BookPresenter层case0：" + message);
                    break;
                case 1:
                    int code = 0;
                    String data = "";
                    String myMsg = "";
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(message);
                        code = jsonObject.getInteger("code");
                        data = jsonObject.getString("data");
                        myMsg = jsonObject.getString("desc");
                        Log.d("TAG1", "返回代码:" + code);
                        Log.d("TAG1", "得到集合：" + data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code == 200) {
                        bookList = parseJsonList(data);
                        myBooksView.showBooks();
                    } else {
                        myBooksView.showToast(myMsg);
                        Log.d("TAG1", "BookPresenter层:" + myMsg);
                    }
                    break;
                case 2:
                    int code2 = 0;
                    String data2 = "";
                    String myMsg2 = "";
                    Log.d("TAG1", "BookPresenter层case2：" + message);
                    if ("".equals(message)) {
                        myBooksView.showToast("操作失败");
                        return;
                    }
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(message + "");
                        code2 = jsonObject.getInteger("code");
                        data2 = jsonObject.getString("data");
                        myMsg2 = jsonObject.getString("desc");
                        Log.d("TAG1", "返回代码code2" + code2);
                        Log.d("TAG1", "data2" + data2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code2 == 200) {
                        myBooksView.showToast(data2);
                        //myBooksView.showBooks();
                        Log.d("TAG1", "加入或移除书架:code == 200" + data2);
                    } else {
                        myBooksView.showToast(myMsg2);
                        Log.d("TAG1", "加入或移除书架:code == 100" + data2);
                    }
                    break;
                case 3:
                    int code3 = 0;
                    String data3 = "";
                    String desc = "";
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(message);
                        code3 = jsonObject.getInteger("code");
                        data3 = jsonObject.getString("data");
                        desc = jsonObject.getString("desc");
                        Log.d("TAG1", "返回代码:" + code3);
                        Log.d("TAG1", "得到集合：" + data3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code3 == 200) {
                        JSONObject json = JSONObject.parseObject(data3);
                        tvc = json.getString("tvc");
                        myBooksView.bookShop();
                    } else {
                        myBooksView.showToast(desc);
                        Log.d("TAG1", "BookPresenter层:" + desc);
                    }
                    break;
                case 4:
                    int code4 = 0;
                    String data4 = "";
                    String desc4 = "";
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(message);
                        code4 = jsonObject.getInteger("code");
                        data4 = jsonObject.getString("data");
                        desc4 = jsonObject.getString("desc");
                        Log.d("TAG1", "返回代码:" + code4);
                        Log.d("TAG1", "得到集合：" + data4);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code4 == 200) {
                        Book book = JSONObject.parseObject(data4, Book.class);
                        bookList = new ArrayList<>();
                        bookList.clear();
                        bookList.add(book);
                        myBooksView.skipView();
                    } else {
                        myBooksView.showToast(desc4);
                        Log.d("TAG1", "BookPresenter层:" + desc4);
                    }
                    break;
            }
        }
    };

    public void showMyBooks(String studentId) {//我的书架

        MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL
                + Constant.MY_BOOK_LIST + "?studentId=" + studentId);
    }

    public void showBookList(String studentId) {//书单推荐
        MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL
                + Constant.BOOK_LIST + "?studentId=" + studentId);
    }

    public void addBookList(String studentId, String bookId) {//加入书架
        myBooksView.showLoading();
        MyHttpUtils.okHttpGet(handler, 2, 0, Constant.URL
                + Constant.ADD_BOOK_LIST + "?studentId=" + studentId + "&bookId=" + bookId);
    }

    public void deleteBook(String bookId, String studentId) {//移出书架
        myBooksView.showLoading();
        MyHttpUtils.okHttpGet(handler, 2, 0, Constant.URL
                + Constant.DELETE_BOOK + "?bookId=" + bookId + "&studentId=" + studentId);
    }

    public void bookShop() {
        MyHttpUtils.okHttpGet(handler, 3, 0, Constant.URL + Constant.BOOK_SHOP);
    }

    public void serchBook(String bookName) {
        MyHttpUtils.okHttpGet(handler, 4, 0, Constant.URL + Constant.SEARCH_BOOK
                + "?name=" + bookName);
    }

    public List<Book> parseJsonList(String json) {
        return JSONObject.parseArray(json, Book.class);
    }
}
