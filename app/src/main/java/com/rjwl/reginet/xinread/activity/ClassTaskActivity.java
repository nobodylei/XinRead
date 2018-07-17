package com.rjwl.reginet.xinread.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.adapter.GameAdapter;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.model.MyClass;
import com.rjwl.reginet.xinread.model.Student;
import com.rjwl.reginet.xinread.model.Teacher;
import com.rjwl.reginet.xinread.presenter.ClassPresenter;
import com.rjwl.reginet.xinread.presenter.MyMainView;
import com.rjwl.reginet.xinread.utils.StrUtils;
import com.vondear.rxtools.RxActivityTool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 * 班级详情
 */

public class ClassTaskActivity extends BaseActivity implements View.OnClickListener, MyMainView{
    private TextView tvClassName;
    private TextView tvClassId;
    private TextView tvClassNumber;
    private LinearLayout llTeacher;//老师
    private LinearLayout llStudent;//学生
    private MyClass myClass;
    private Teacher teacher;
    private List<Student> studentList;
    private List<Book> bookList;

    private GameAdapter gameAdapter;
    private GridView gvGame;

    //闯关任务
    //private TextView tvGameTime;//时间
    /*private ImageView imgOne;
    private ImageView imgTwo;
    private ImageView imgThree;
    private TextView tvOneName;
    private TextView tvTwoName;
    private TextView tvThreeName;*/

    private boolean isTeacher = false;

    private ClassPresenter classPresenter;

    @Override
    void initView() {

        classPresenter = new ClassPresenter(this);

        title.setText("班级详情");
        tvClassName = findViewById(R.id.tv_class_mas_name);
        tvClassId = findViewById(R.id.tv_class_msg_id);
        tvClassNumber = findViewById(R.id.tv_class_msg_number);
        llTeacher = findViewById(R.id.ll_teacher);
        llStudent = findViewById(R.id.ll_student);

        gvGame = findViewById(R.id.gv_game_list);
        //tvGameTime = findViewById(R.id.tv_task_time);
        /*imgOne = findViewById(R.id.img_task_book_one);
        imgTwo = findViewById(R.id.img_task_book_two);
        imgThree = findViewById(R.id.img_task_book_three);
        tvOneName = findViewById(R.id.tv_task_book_one_name);
        tvTwoName = findViewById(R.id.tv_task_book_two_name);
        tvThreeName = findViewById(R.id.tv_task_book_three_name);
*/
        llTeacher.setOnClickListener(this);
        llStudent.setOnClickListener(this);

        /*imgOne.setOnClickListener(this);
        imgTwo.setOnClickListener(this);
        imgThree.setOnClickListener(this);*/

        //得到传递的班级对象
        Bundle bundle = getIntent().getBundleExtra("classBundle");
        myClass = (MyClass) bundle.get("myClass");

        tvClassName.setText(myClass.getClassname());
        tvClassId.setText("班级ID:" + myClass.getClassID());
        tvClassNumber.setText("班级人数:" + myClass.getCount());

        classPresenter.getTask(StrUtils.intTOStr(myClass.getGrade()) + "年级");
        bookList = new ArrayList<>();

        gameAdapter = new GameAdapter(bookList, this, myClass.getClassID() + "");
        gvGame.setAdapter(gameAdapter);
        //initBook();
    }

    @Override
    int getResId() {
        return R.layout.activity_class;
    }

    @Override
    public void onClick(View v) {

       /* Intent intent1 = new Intent(ClassTaskActivity.this, GameActivity.class);
        Bundle bundle = new Bundle();*/
        switch(v.getId()) {
            case R.id.ll_teacher:
                Log.d("TAG1", "老师");
                classPresenter.searchTeacher(myClass.getClassID() + "");
                isTeacher = true;
                break;
            case R.id.ll_student:
                Log.d("TAG1", "学生");
                classPresenter.searchStudent(myClass.getClassID() + "");
                isTeacher = false;
                break;
            /*case R.id.img_task_book_one:
                if(bookList.size() >= 1){
                    bundle.putSerializable("book", bookList.get(0));
                    intent1.putExtra("bookBundle", bundle);
                    startActivity(intent1);
                }
                break;
            case R.id.img_task_book_two:
                if(bookList.size() >= 2){
                    bundle.putSerializable("book", bookList.get(1));
                    intent1.putExtra("bookBundle", bundle);
                    startActivity(intent1);
                }
                break;
            case R.id.img_task_book_three:
                if(bookList.size() >= 3){
                    bundle.putSerializable("book", bookList.get(2));
                    intent1.putExtra("bookBundle", bundle);
                    startActivity(intent1);
                }
                break;*/
            default:
                break;
        }

    }

    @Override
    public void showLoading() {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("");
            progressDialog.setMessage("请稍后...");
            progressDialog.show();
           // progressDialog = ProgressDialog.show(this, "", "正在加载...");
        } else if(progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog.setTitle("");
            progressDialog.setMessage("正在加载...");
        }
        progressDialog.show();
    }

    @Override
    public void hidenLoading() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(ClassTaskActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void skipView() {
        Intent intent = new Intent(ClassTaskActivity.this, TeacherAndStudent.class);

        if (isTeacher) {
            teacher = classPresenter.getTeacher();
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable("teacher", teacher);
            intent.putExtra("teacherBundle", bundle1);
            intent.putExtra("isTeacher", true);
        } else {
            Bundle bundle2 = new Bundle();
            studentList = classPresenter.getStuList();
            bundle2.putSerializable("studentList", (Serializable) studentList);
            intent.putExtra("studentBundle", bundle2);
        }

        intent.putExtra("classId", myClass.getClassID());
        startActivity(intent);
    }


    //测试数据
    public void initBook() {
        Book book = new Book();
        book.setId(1);
        book.setName("任务1");
        book.setHeadPic("0cb93ffd-dbd9-488b-9eb6-f6950ffa33fb94&fm=27&gp=0.jpg");
        bookList.add(book);
        book.setId(2);
        book.setName("任务2");
        book.setHeadPic("0cb93ffd-dbd9-488b-9eb6-f6950ffa33fb94&fm=27&gp=0.jpg");
        bookList.add(book);
        book.setId(3);
        book.setName("任务3");
        book.setHeadPic("0cb93ffd-dbd9-488b-9eb6-f6950ffa33fb94&fm=27&gp=0.jpg");
        bookList.add(book);
        /*if(bookList.size() >= 1) {
            imgOne.setImageURI(Uri.parse(Constant.IMG + bookList.get(0).getHeadPic()));
            tvOneName.setText(bookList.get(0).getName());
        }
        if(bookList.size() >= 2) {
            imgTwo.setImageURI(Uri.parse(Constant.IMG + bookList.get(1).getHeadPic()));
            tvTwoName.setText(bookList.get(1).getName());
        }
        if(bookList.size() >= 3) {
            imgThree.setImageURI(Uri.parse(Constant.IMG + bookList.get(2).getHeadPic()));
            tvThreeName.setText(bookList.get(2).getName());
        }*/
    }

    @Override
    public void updateView() {
        List<Book> list = classPresenter.getTask();
        if (list != null) {
            bookList.addAll(list);
        }
        gameAdapter.notifyDataSetChanged();
        /*if(bookList.size() >= 1) {
            imgOne.setImageURI(Uri.parse(Constant.IMG + bookList.get(0).getHeadPic()));
            tvOneName.setText(bookList.get(0).getName());
        }
        if(bookList.size() >= 2) {
            imgTwo.setImageURI(Uri.parse(Constant.IMG + bookList.get(1).getHeadPic()));
            tvTwoName.setText(bookList.get(1).getName());
        }
        if(bookList.size() >= 3) {
            imgThree.setImageURI(Uri.parse(Constant.IMG + bookList.get(2).getHeadPic()));
            tvThreeName.setText(bookList.get(2).getName());
        }*/
    }

    @Override
    public void upDataBtn() {

    }
}
