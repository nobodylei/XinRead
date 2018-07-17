package com.rjwl.reginet.xinread.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.model.MyClass;
import com.rjwl.reginet.xinread.utils.MyHttpUtils;

import java.util.List;


/**
 * Created by Administrator on 2018/5/8.
 * 班级搜索相关
 */

public class SearchClassPresenter extends BasePresenter {
    private MyMainView myMainView;
    private MyClass myClass;

    //private List<MyClass> classList;

    private boolean isAdd = false;

    public SearchClassPresenter(MyMainView myMainView) {
        this.myMainView = myMainView;
    }

    public MyClass getMyClass() {
        return myClass;
    }

   /* public List<MyClass> getClassList() {
        return classList;
    }*/

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = msg.obj + "";
            myMainView.hidenLoading();
            switch (msg.what) {
                case 0:
                    Log.d("TAG1", message + "SerchP层" + myMainView);
                    myMainView.showToast(message);
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
                        if (isAdd) {
                            myMainView.showToast(myMsg);
                            myMainView.upDataBtn();
                        } else {
                            myClass = parseJson(data);
                            myMainView.updateView();
                        }
                    } else {
                        myClass = null;
                        if (!isAdd)
                            myMainView.updateView();
                        myMainView.showToast(myMsg);
                    }
                    break;

            }
        }
    };

    public void addClass(String classId, String studentId) {
        isAdd = true;
        myMainView.showLoading();
        MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL
                + Constant.ADD_CLASS + "?classId=" + classId + "&studentId=" + studentId);
    }

    public void searchClass(String classId, String studentId) {
        if ("".equals(classId)) {
            myMainView.showToast("ID不能为空");
            return;
        }
        isAdd = false;
        myMainView.showLoading();
        MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL
                + Constant.SEARCH_CLASS_URL + "?classID=" + classId + "&studentId=" + studentId);
    }

    public void searchClassList(String studentid) {
        if ("".equals(studentid)) {
            myMainView.showToast("暂无信息");
            return;
        }
        isAdd = false;
        myMainView.showLoading();
        MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL
                + Constant.CLASS_LIST + "?studentId=" + studentid);
    }

    public MyClass parseJson(String json) {
        return JSONObject.parseObject(json, MyClass.class);
    }

    /*public List<MyClass> parseJsonList(String json) {
        return JSONObject.parseArray(json, MyClass.class);
    }*/
}
