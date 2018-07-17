package com.rjwl.reginet.xinread.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rjwl.reginet.xinread.model.Comment;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.utils.MyHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/11.
 */

public class CommentPresenter extends BasePresenter {
    private MyCommentView myCommentView;
    private List<Comment> commentList;

    public CommentPresenter(MyCommentView myCommentView) {
        this.myCommentView = myCommentView;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = msg.obj + "";
            Log.d("TAG1", "BookPresenter层：" + message);
            if ("".equals(message)) {
                return;
            }
            switch (msg.what) {
                case 0:
                    myCommentView.showToast(message);
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
                        commentList = parseJsonList(data);
                        myCommentView.showComment();
                    } else {
                        myCommentView.showToast(myMsg);
                        Log.d("TAG1", "BookPresenter层:" + myMsg);
                    }
                    break;
                case 2:
                    int code2 = 0;
                    String data2 = "";
                    String myMsg2 = "";
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(message);
                        code2 = jsonObject.getInteger("code");
                        data2 = jsonObject.getString("data");
                        myMsg = jsonObject.getString("desc");
                        Log.d("TAG1", "返回代码:" + code2);
                        Log.d("TAG1", "得到集合：" + data2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code2 == 200) {
                        //commentList = parseJsonList(data2);
                        myCommentView.showToast(data2);
                    } else {
                        myCommentView.showToast(myMsg2);
                        Log.d("TAG1", "BookPresenter层:" + myMsg2);
                    }
                    break;
            }
        }
    };

    public void getComment(String bookId, boolean isBook) {
        if (isBook) {
            MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL + Constant.COMMENT
                    + "?bookId=" + bookId);
        } else {
            MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL + Constant.NOTES
                    + "?studentId=" + bookId);
        }
    }

    public void sendComment(String bookId, String studentId, String speech) {
        MyHttpUtils.okHttpGet(handler, 2, 0, Constant.URL +
                Constant.ADD_SPEECH + "?studentId=" + studentId + "&bookId=" + bookId
                + "&speech=" + speech);
    }


    public List<Comment> parseJsonList(String json) {
        return JSONObject.parseArray(json, Comment.class);
    }
}
