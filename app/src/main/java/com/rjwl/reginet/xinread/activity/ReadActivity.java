package com.rjwl.reginet.xinread.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rjwl.reginet.xinread.R;

import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.model.Student;
import com.rjwl.reginet.xinread.utils.MyHttpUtils;
import com.rjwl.reginet.xinread.utils.StrUtils;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxBarTool;

/**
 * Created by yanle on 2018/5/9.
 * 阅读成就
 */

public class ReadActivity extends BaseActivity {
    private ImageView imgBack;
    //private MyImageView imgReadHead;
    private SimpleDraweeView imgReadHead;

    private Student student;//传过来的学生对象

    private TextView tvReadName;
    private TextView tvReadClass;
    private TextView tvReadSchool;
    private TextView tvReadOne;
    private TextView tvReadTwo;
    private TextView tvReadThrree;
    @Override
    void initView() {
        RxBarTool.setTransparentStatusBar(this);

        Bundle bundle = getIntent().getBundleExtra("stuBundle");
        if (bundle != null) {
            student = (Student) bundle.getSerializable("student");
        }

        rlBase.setVisibility(View.GONE);
        imgBack = findViewById(R.id.img_read_back);

        imgReadHead = findViewById(R.id.img_read_head);//头像
        tvReadName = findViewById(R.id.tv_read_name);//姓名
        tvReadClass = findViewById(R.id.tv_read_class);//班级
        tvReadSchool = findViewById(R.id.tv_read_school);//学校
        tvReadOne = findViewById(R.id.tv_get_one); //成就1
        tvReadTwo = findViewById(R.id.tv_get_two); // 成就2
        tvReadThrree = findViewById(R.id.tv_get_three); //成就3

        initStudent();

        getRead();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initStudent() {
        if (student == null) {
            return;
        }
        String myName = student.getName();
        String schoolName = student.getSchoolname();
        String myClass = StrUtils.intTOStr(student.getGradeID()) + "年" +
                StrUtils.intTOStr(student.getYesorno()) + "班";
        tvReadClass.setText(myClass + "");
        imgReadHead.setImageURI(Uri.parse(Constant.IMG + student.getHeadPic()));
        if (myName != null && !"null".equals(myName)) {
            tvReadName.setText(myName + "");
        } else {
            tvReadName.setText(StrUtils.setPhone(student.getUsername()));
        }
        if (schoolName != null) {
            tvReadSchool.setText(schoolName + "");
        }
    }

    @Override
    int getResId() {
        return R.layout.activity_get_read;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String message = msg.obj + "";
            switch (msg.what) {
                case 0:
                    Log.d("TAG1", message + "");
                    Toast.makeText(ReadActivity.this, message, Toast.LENGTH_SHORT).show();
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
                        JSONObject json = JSONObject.parseObject(data);
                        tvReadOne.setText(json.getString("modifyTime") + "\n"
                                + "我加入了馨阅读这个大家庭");
                        tvReadTwo.setText("在这里阅读了" + json.getInteger("count") + "本书");
                       // tvReadThrree.setText(json.getString("modifyTime"));
                       //Toast.makeText(ReadActivity.this, myMsg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReadActivity.this, myMsg, Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };

    private void getRead() {
        if (student == null) {
            return;
        }
        MyHttpUtils.okHttpGet(handler, 1, 0, Constant.URL
                + Constant.READ_GET + "?studentId=" + student.getId());
    }

}
