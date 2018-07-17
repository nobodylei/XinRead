package com.rjwl.reginet.xinread.activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.nativecode.JpegTranscoder;
import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.fragment.ClassFragment;
import com.rjwl.reginet.xinread.fragment.CourseFragment;
import com.rjwl.reginet.xinread.fragment.HomeFragment;
import com.rjwl.reginet.xinread.fragment.MyFragment;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxBarTool;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    //导航栏单选按钮
    private RadioGroup rgHome;

    private RadioButton[] rbs;
    private Drawable[] drawables;

    Rect rect;

    private long fistPressedTime;

    private List<Fragment> fragmentList;

    @SuppressLint("WrongViewCast")
    @Override
    void initView() {
        RxBarTool.setTransparentStatusBar(this);//透明通知栏
        Fresco.initialize(this);
        rlBase.setVisibility(View.GONE);

        rbs = new RadioButton[4];

        rgHome = findViewById(R.id.rg_home);
        rbs[0] = findViewById(R.id.rb_home);
        rbs[1] = findViewById(R.id.rb_class);
        rbs[2] = findViewById(R.id.rb_course);
        rbs[3] = findViewById(R.id.rb_my);

        //for循环对每一个RadioButton图片进行缩放
        for (int i = 0; i < rbs.length; i++) {
            //挨着给每个RadioButton加入drawable限制边距以控制显示大小
            drawables = rbs[i].getCompoundDrawables();
            //获取drawables，2/5表示图片要缩小的比例
            rect = new Rect(0, 0, (int) (drawables[1].getMinimumWidth() * 1.5),
                    (int) (drawables[1].getMinimumHeight() * 1.5));
            //定义一个Rect边界
            drawables[1].setBounds(rect);
            //给每一个RadioButton设置图片大小
            rbs[i].setCompoundDrawables(null, drawables[1], null, null);
            //给drawable设置边界
        }

        /*rect = new Rect(0, 0, rbHome.getCompoundDrawables()[1].getMinimumWidth() * 2,
                 rbHome.getCompoundDrawables()[1].getMinimumHeight() * 2);*/

        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new ClassFragment());
        fragmentList.add(new CourseFragment());
        fragmentList.add(new MyFragment());

        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.content_layout, fragmentList.get(0));
        transaction.commit();
        title.setText("主页");

        rgHome.setOnCheckedChangeListener(this);
    }

    @Override
    int getResId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        transaction = manager.beginTransaction();
        int home = R.drawable.home;
        int myClass = R.drawable.my_class2;
        int course = R.drawable.course2;
        int my = R.drawable.my2;

        int i = 0;
        switch (checkedId) {
            case R.id.rb_home:
                home = R.drawable.home;
                myClass = R.drawable.my_class2;
                course = R.drawable.course2;
                my = R.drawable.my2;
                i = 0;
                break;
            case R.id.rb_class:
                home = R.drawable.home2;
                myClass = R.drawable.my_class;
                course = R.drawable.course2;
                my = R.drawable.my2;
                i = 1;
                break;
            case R.id.rb_course:
                home = R.drawable.home2;
                myClass = R.drawable.my_class2;
                course = R.drawable.course;
                my = R.drawable.my2;
                i = 2;
                break;
            case R.id.rb_my:
                home = R.drawable.home2;
                myClass = R.drawable.my_class2;
                course = R.drawable.course2;
                my = R.drawable.my;
                i = 3;
                break;
        }
        //修改按钮图片
        Drawable drawableHome = this.getResources().getDrawable(home);
        drawableHome.setBounds(rect);
        rbs[0].setCompoundDrawables(null, drawableHome, null, null);
        Drawable drawableClass = this.getResources().getDrawable(myClass);
        drawableClass.setBounds(rect);
        rbs[1].setCompoundDrawables(null, drawableClass, null, null);
        Drawable drawableCourse = this.getResources().getDrawable(course);
        drawableCourse.setBounds(rect);
        rbs[2].setCompoundDrawables(null, drawableCourse, null, null);
        Drawable drawableMy = this.getResources().getDrawable(my);
        drawableMy.setBounds(rect);
        rbs[3].setCompoundDrawables(null, drawableMy, null, null);


        transaction.replace(R.id.content_layout, fragmentList.get(i));
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            return;
        }
        if (System.currentTimeMillis() - fistPressedTime < 2000) {
            super.onBackPressed();
            RxActivityTool.finishAllActivity();
        } else {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
        }
        fistPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
}
