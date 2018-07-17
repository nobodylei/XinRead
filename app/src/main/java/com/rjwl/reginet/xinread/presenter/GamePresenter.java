package com.rjwl.reginet.xinread.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.model.Game;
import com.rjwl.reginet.xinread.utils.MyHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/18.
 */

public class GamePresenter extends BasePresenter {
    private MyGameView myGameView;
    private List<Game> gameList;


    public GamePresenter(MyGameView myGameView) {
        this.myGameView = myGameView;
    }

    public List<Game> getGameList() {
        return gameList;
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
                    myGameView.showToast(message);
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
                        gameList = parseJsonList(data);
                        myGameView.initGame();
                    } else {
                        myGameView.showToast(myMsg);
                        Log.d("TAG1", "BookPresenter层:" + myMsg);
                    }
                    break;
            }
        }
    };

    public void getGame(String bookId, String studentId) {
        MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL + Constant.GAME
                + "?bookId=" + bookId + "&studentId=" + studentId);
    }

    public void sendScore(int score, Book book, int studentId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("score", score);
        map.put("studentId", studentId);
        map.put("bookId", book.getId());
        MyHttpUtils.okHttpGet(handler, 2, 0, Constant.URL + Constant.SCORE
        + "?score=" + score + "&studentId=" + studentId + "&bookId=" + book.getId());
        //MyHttpUtils.okHttpUtils(map, handler, 2, 0, Constant.URL + Constant.SCORE);
    }

    public List<Game> parseJsonList(String json) {
        return JSONObject.parseArray(json, Game.class);
    }
}
