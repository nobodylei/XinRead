package com.rjwl.reginet.xinread.activity;

import android.app.ProgressDialog;
import android.widget.ListView;
import android.widget.Toast;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.adapter.RankAdapter;
import com.rjwl.reginet.xinread.model.Student;
import com.rjwl.reginet.xinread.presenter.MyBaseView;
import com.rjwl.reginet.xinread.presenter.RankPresenter;
import com.vondear.rxtools.RxActivityTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8.
 * 排行榜
 */

public class RankActivity extends BaseActivity implements MyBaseView{
    private ListView lvRank;
    private RankAdapter rankAdapter;
    private List<Student> userList;
    private RankPresenter rankPresenter;

    @Override
    void initView() {
        rankPresenter = new RankPresenter(this);

        title.setText("排行榜");
        lvRank = findViewById(R.id.lv_rank);

        userList = new ArrayList<>();
        //initUser();
        rankAdapter = new RankAdapter(RankActivity.this, userList);
        lvRank.setAdapter(rankAdapter);

        rankPresenter.getRank();
    }

    @Override
    int getResId() {
        return R.layout.activity_rank_list;
    }

    //测试数据
    private void initUser() {
        Student user1 = new Student();
        user1.setName("张三");
        Student user2 = new Student();
        user2.setName("李四");
        Student user3 = new Student();
        user3.setName("王五");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("");
            progressDialog.setMessage("请稍后...");
            progressDialog.show();
           // progressDialog = ProgressDialog.show(this, "", "请稍后...");
        } else if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog.setTitle("");
            progressDialog.setMessage("请稍后...");
        }
        progressDialog.show();
    }

    @Override
    public void hidenLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(RankActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void skipView() {
        List<Student> list = rankPresenter.getStudentList();
        if (list != null) {
            userList.addAll(list);
        }
        rankAdapter.notifyDataSetChanged();
    }
}
