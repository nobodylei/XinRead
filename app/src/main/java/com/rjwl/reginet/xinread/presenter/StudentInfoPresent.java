package com.rjwl.reginet.xinread.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.utils.MyHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/19.
 */

public class StudentInfoPresent extends BasePresenter {
    private StudentInfoView studentInfoView;
    private String headPicture;

    public StudentInfoPresent(StudentInfoView studentInfoView) {
        this.studentInfoView = studentInfoView;
    }

    public String getHeadPicture() {
        return headPicture;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            studentInfoView.hidenLoading();
            String message = msg.obj + "";
            Log.d("TAG1", "修改资料返回值：" + message);
            if ("".equals(message)) {
                return;
            }
            switch (msg.what) {
                case 0:
                    Log.d("TAG1", "修改资料code==0：" + message);
                    studentInfoView.showToast(message);
                    //mMyLoginView.skipView();
                    break;
                case 1:
                    Log.d("TAG1", message + "code==1;当前线程" + Thread.currentThread());
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
                    if (code == 200) {
                        studentInfoView.skipView();
                        studentInfoView.showToast(myMsg);
                    } else if (code == 201) {
                        JSONObject head = JSONObject.parseObject(data);
                        headPicture = head.getString("headPicture");
                        studentInfoView.showToast(myMsg);
                        studentInfoView.getHead();
                    } else {
                        studentInfoView.showToast(myMsg);
                    }

                    break;
            }
        }
    };

    public void updataStudent(Map<String, String> map) {

        Log.d("TAG1", "发送的集合:" + map);
        studentInfoView.showLoading();
        MyHttpUtils.okHttpUtils(map, handler, 1, 0, Constant.URL + Constant.STUDENT_INFO);
    }

    public void updataHead(Map<String, String> map, Map<String, File> fileMap) {
        studentInfoView.showLoading();
        MyHttpUtils.uploadFile(handler, map, fileMap, 1, 0, Constant.URL + Constant.UPDATA_HEAD);
    }


    /*public void login(String user, String pass) {
        Log.d("TAG1", "用户名密码:" + user + pass);
        Map<String, String> map = new HashMap<>();
        map.put("username", user);
        map.put("password", pass);

        //mMyLoginView.showLoading();
        MyHttpUtils.okHttpUtils(map, handler, 1, 0, Constant.URL + Constant.LOGIN_URL);
    }*/
}
