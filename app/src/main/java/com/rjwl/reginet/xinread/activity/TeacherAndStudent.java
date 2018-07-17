package com.rjwl.reginet.xinread.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.adapter.StudentAdapter;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.Student;
import com.rjwl.reginet.xinread.model.Teacher;
import com.rjwl.reginet.xinread.utils.StrUtils;
import com.vondear.rxtools.RxActivityTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 * 老师和学生信息
 */

public class TeacherAndStudent extends BaseActivity{
    //老师信息
    private LinearLayout llTeacher;
    private TextView tvTeacherName;
    private TextView tvTeacherSex;
    private TextView tvTeacherSchool;

    //学生信息
    private LinearLayout rlStudentClass;
    private TextView tvStudentClass;
    private ListView lvStudent;
    private StudentAdapter studentAdapter;
    private List<Student> studentList;

    @Override
    void initView() {
        llTeacher = findViewById(R.id.ll_teacher_msg);
        rlStudentClass = findViewById(R.id.rl_student_class);
        lvStudent = findViewById(R.id.lv_mystudent);
        tvTeacherName = findViewById(R.id.tv_teacher_name);
        tvTeacherSex = findViewById(R.id.tv_teacher_sex);
        tvTeacherSchool = findViewById(R.id.tv_teacher_school);
        tvStudentClass = findViewById(R.id.tv_student_class);
        boolean isTeacher = getIntent().getBooleanExtra("isTeacher", false);

        if (isTeacher) {//老师详情
            title.setText("我的老师");
            Bundle bundle = getIntent().getBundleExtra("teacherBundle");
            Teacher teacher = (Teacher) bundle.getSerializable("teacher");
            tvTeacherName.setText(teacher.getName() + "");
            tvTeacherSex.setText(teacher.getGender() + "");
            tvTeacherSchool.setText(teacher.getSchoolname() + "");
            lvStudent.setVisibility(View.GONE);
            llTeacher.setVisibility(View.VISIBLE);
        } else {//同学列表
            title.setText("我的同学");
            Bundle bundle = getIntent().getBundleExtra("studentBundle");
            studentList = (List<Student>) bundle.getSerializable("studentList");
            llTeacher.setVisibility(View.GONE);
            lvStudent.setVisibility(View.VISIBLE);
            //initStudent();
            studentAdapter = new StudentAdapter(TeacherAndStudent.this, studentList);
            lvStudent.setAdapter(studentAdapter);
            lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    rlStudentClass.setVisibility(View.VISIBLE);
                    lvStudent.setVisibility(View.GONE);
                    llTeacher.setVisibility(View.VISIBLE);
                    Student student = studentList.get(position);
                    tvTeacherName.setText(student.getName());
                    tvTeacherSex.setText(student.getGender());
                    tvTeacherSchool.setText(student.getSchoolname());
                    tvStudentClass.setText(StrUtils.intTOStr(student.getGradeID()) + "年"
                            + StrUtils.intTOStr(student.getYesorno()) + "班");
                }
            });
        }
    }

    private void initStudent() {
        /*studentList = new ArrayList<>();
        Student s1 = new Student();
        s1.setName("张国民");
        s1.setGender("男");
        s1.setSchoolname("满族中学");
        s1.setGradeID(7);
        s1.setYesorno(2);
        Student s2 = new Student();
        s2.setName("李建军");
        s2.setGender("男");
        s2.setSchoolname("职教中心");
        s2.setGradeID(3);
        s2.setYesorno(2);
        Student s3 = new Student();
        s3.setName("宋明");
        s3.setGender("男");
        s3.setSchoolname("第三中学");
        s3.setGradeID(6);
        s3.setYesorno(3);
        studentList.add(s1);
        studentList.add(s2);
        studentList.add(s3);*/
    }

    @Override
    public void onBackPressed() {
        if (rlStudentClass.getVisibility() == View.VISIBLE) {
            rlStudentClass.setVisibility(View.GONE);
            llTeacher.setVisibility(View.GONE);
            lvStudent.setVisibility(View.VISIBLE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    int getResId() {
        return R.layout.activity_teacher_student;
    }

}
