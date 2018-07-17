package com.rjwl.reginet.xinread.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.model.Game;
import com.rjwl.reginet.xinread.model.Student;
import com.rjwl.reginet.xinread.utils.MyHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/25.
 */

public class RankPresenter extends BasePresenter {

    private MyBaseView myBaseView;
    private List<Student> studentList;

    public RankPresenter(MyBaseView myBaseView) {
        this.myBaseView = myBaseView;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = msg.obj + "";
            myBaseView.hidenLoading();
            Log.d("TAG1", "BookPresenter层：" + message);
            if ("".equals(message)) {
                return;
            }
            switch (msg.what) {
                case 0:
                    myBaseView.showToast(message);
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
                        studentList = parseJsonList(data);
                        myBaseView.skipView();
                    } else {
                        myBaseView.showToast(myMsg);
                        Log.d("TAG1", "BookPresenter层:" + myMsg);
                    }
                    break;
            }
        }
    };

    public void getRank() {
        myBaseView.showLoading();
        MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL + Constant.RANK);
    }



    public List<Student> parseJsonList(String json) {
        Log.d("TAG1", "得到集合：" + json);
        return JSONObject.parseArray(json, Student.class);
    }


}
