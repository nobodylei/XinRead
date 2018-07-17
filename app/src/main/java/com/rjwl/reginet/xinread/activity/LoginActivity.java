package com.rjwl.reginet.xinread.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.model.Student;
import com.rjwl.reginet.xinread.presenter.LoginPresenter;
import com.rjwl.reginet.xinread.presenter.MyLoginView;
import com.rjwl.reginet.xinread.utils.GreenDaoUtil;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxBarTool;

/**
 * Created by Administrator on 2018/5/2.
 * 登录
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, MyLoginView {
    private EditText etUserName;
    private EditText etPassWord;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvForget;

    private GreenDaoUtil greenDaoUtil;

    private LoginPresenter mLoginPresenter;
    private String userName;
    private String passWord;

    @Override
    void initView() {
        userName = SaveOrDeletePrefrence.look(getApplicationContext(), "username");
        passWord = SaveOrDeletePrefrence.look(getApplicationContext(), "password");
        if (!"".equals(userName) && !"".equals(passWord)){
            skipView();
        }

        greenDaoUtil = GreenDaoUtil.getDBUtil(getApplicationContext());

        mLoginPresenter = new LoginPresenter(this);

        rlBase.setVisibility(View.GONE);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tvForget = findViewById(R.id.tv_forget);
        etUserName = findViewById(R.id.et_username);
        etPassWord = findViewById(R.id.et_password);

        btnLogin.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    int getResId() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View v) {
        Intent intent1 = new Intent(LoginActivity.this, RegActivity.class);
        switch (v.getId()) {
            case R.id.btn_login:
                /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();*/
                userName = etUserName.getText() + "";
                passWord = etPassWord.getText() + "";
                mLoginPresenter.lookPhoneNumber(userName, passWord);
                break;
            case R.id.tv_register:
                //intent1.putExtra("title", "注册账号");
                intent1.putExtra("isReg", true);
                startActivity(intent1);
                break;
            case R.id.tv_forget:
                //intent1.putExtra("title", "忘记密码");
                startActivity(intent1);
                break;
            default:
                break;
        }

    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("");
            progressDialog.setMessage("请稍后...");
            progressDialog.show();
            //progressDialog = ProgressDialog.show(this, "", "请稍后...");
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
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void skipView() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
       /* Student student = new Student();
        student.setYesorno(4);
        student.setGradeID(8);
        student.setName("哈喽");
        greenDaoUtil.delAll();
        greenDaoUtil.insertStu(student);*/
    }

    @Override
    public void saveUser(Student user) {
        Log.d("TAG1", user.toString());
        SaveOrDeletePrefrence.saveInt(getApplicationContext(), "id", user.getId());
        SaveOrDeletePrefrence.save(getApplicationContext(), "username", user.getUsername());
        SaveOrDeletePrefrence.save(getApplicationContext(), "password", user.getPassword());

        if (greenDaoUtil.getStudetnById(user.getId()) == null) {
            greenDaoUtil.delAll();
            greenDaoUtil.insertStu(user);
        }
    }
}
