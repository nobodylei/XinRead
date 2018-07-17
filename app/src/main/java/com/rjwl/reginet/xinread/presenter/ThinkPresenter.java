package com.rjwl.reginet.xinread.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.model.MyThinkMap;
import com.rjwl.reginet.xinread.utils.MyHttpUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/7/4.
 */

public class ThinkPresenter extends BasePresenter {
    private ThinkMapView thinkMapView;
    private List<MyThinkMap> myThinkMapList;

    public ThinkPresenter(ThinkMapView thinkMapView) {
        this.thinkMapView = thinkMapView;
    }

    public List<MyThinkMap> getMyThinkMapList() {
        return myThinkMapList;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = msg.obj + "";
            switch (msg.what) {
                case 0:
                    Log.d("TAG1", message + " SerchP层");
                    thinkMapView.showToast(message);
                    break;
                case 1:
                    Log.d("TAG1", message + "当前线程" + Thread.currentThread());
                    int code = 0;
                    String data = "";
                    String myMsg = "";
                    try {
                        JSONObject jsonObject = JSONObject.parseObject(message);
                        code = jsonObject.getInteger("code");
                        data = jsonObject.getString("data");
                        myMsg = jsonObject.getString("desc");
                        Log.d("TAG1", "返回代码" + code);
                        Log.d("TAG1", data + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code == 200 || code == 201) {
                        myThinkMapList = parseJsonList(data);
                        thinkMapView.showThink();
                    } else {
                        thinkMapView.showToast(myMsg);
                    }
                    break;

            }
        }
    };

    public void getThinkMap(String studentId, String bookId) {
        MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL + Constant.THINK_MAP
                + "?studentId=" + studentId + "&bookId=" + bookId);
    }

    public List<MyThinkMap> parseJsonList(String json) {
        Log.d("TAG1", "得到集合：" + json);
        return JSONObject.parseArray(json, MyThinkMap.class);
    }
}
