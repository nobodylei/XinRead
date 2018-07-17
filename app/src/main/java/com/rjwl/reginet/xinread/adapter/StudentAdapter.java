package com.rjwl.reginet.xinread.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.model.Student;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 * 我的同学适配器
 */

public class StudentAdapter extends BaseAdapter {
    private Context mContext;
    private List<Student> mStudentList;

    public StudentAdapter(Context context, List<Student> studentList) {
        this.mContext = context;
        this.mStudentList = studentList;
    }

    @Override
    public int getCount() {
        return mStudentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStudentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //返回项的View
        //持有者
        Wrapper wrapper = null;
        //解析成view
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(mContext).inflate(R.layout.item_student, parent, false);
            //将布局文件解析成View的过程，需要资源
            wrapper = new Wrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (Wrapper) row.getTag();
        }
        SimpleDraweeView imgStudentHead = wrapper.getImgStudentHead();
        TextView tvStudentName = wrapper.getTvStudentName();

        //通过position决定内容
        Student student = mStudentList.get(position);
        imgStudentHead.setImageURI(Uri.parse(Constant.IMG + student.getHeadPic()));
        tvStudentName.setText(student.getName());

        return row;
    }

    class Wrapper {
        SimpleDraweeView imgStudentHead;
        //ImageView imgStudentHead;
        TextView tvStudentName;


        View row;

        public Wrapper(View row) {
            this.row = row;
        }

        public SimpleDraweeView getImgStudentHead() {
            if (imgStudentHead == null) {
                imgStudentHead = row.findViewById(R.id.img_student_head);
            }
            return imgStudentHead;
        }

        public TextView getTvStudentName() {
            if (tvStudentName == null) {
                //查找
                tvStudentName = row.findViewById(R.id.tv_mystudent_name);
            }
            return tvStudentName;
        }
    }
}
