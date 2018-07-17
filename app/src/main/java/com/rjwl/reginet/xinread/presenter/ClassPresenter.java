package com.rjwl.reginet.xinread.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.model.Student;
import com.rjwl.reginet.xinread.model.Teacher;
import com.rjwl.reginet.xinread.utils.MyHttpUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 * 班级中老师和学生相关
 */

public class ClassPresenter extends BasePresenter {
    private MyMainView myMainView;
    private Teacher teacher;
    private boolean isTeacher = false;//是否是老师
    private List<Student> stuList;//学生列表
    private List<Book> book;// 闯关任务

    public ClassPresenter(MyMainView myMainView) {
        this.myMainView = myMainView;
    }

    public List<Book> getTask() {
        return book;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public List<Student> getStuList() {
        return stuList;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            myMainView.hidenLoading();
            String message = msg.obj + "";
            switch (msg.what) {
                case 0:
                    Log.d("TAG1", message + "");
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
                    if (code == 200) {
                        if (isTeacher) {
                            teacher = parseJson(data);
                        } else {
                            stuList = paseStudentJson(data);
                            Log.d("TAG1", "解析后：" + stuList);
                        }
                        myMainView.skipView();
                    } else {
                        myMainView.showToast(myMsg);
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
                        myMsg2 = jsonObject.getString("desc");
                        Log.d("TAG1", "返回代码" + code2);
                        Log.d("TAG1", data2 + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code2 == 200) {
                        book = parseBookList(data2);
                        myMainView.updateView();
                    } else {
                        myMainView.showToast(myMsg2);
                    }
                    break;
            }
        }
    };

    public void getTask(String classId) {
        MyHttpUtils.okHttpGet(handler, 2, 0, Constant.URL
                + Constant.TASK + "?grade=" + classId);
    }

    public void searchTeacher(String classId) {
        isTeacher = true;
        MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL
                + Constant.TEACHER_URL + "?classID=" + classId);
        myMainView.showLoading();
    }

    public void searchStudent(String classId) {
        isTeacher = false;
        MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL
                + Constant.STUDENT_LIST_URL + "?classID=" + classId);
        myMainView.showLoading();
    }

    public Teacher parseJson(String json) {
        return JSONObject.parseObject(json, Teacher.class);
    }

    public List<Student> paseStudentJson(String json) {
        Log.d("TAG1", "转换前：" + json);
       /* JSONArray arr=JSONObject.parseArray(json);
        Log.d("TAG1", "转换数组后：" + json);
        String js = JSONObject.toJSONString(arr, SerializerFeature.WriteClassName);
        Log.d("TAG1", "数组转字符串：" + js);*/
        return JSONObject.parseArray(json, Student.class);

    }

    public List<Book> parseBookList(String json) {
        return JSONObject.parseArray(json, Book.class);
    }
}


