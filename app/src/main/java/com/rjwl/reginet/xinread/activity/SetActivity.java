package com.rjwl.reginet.xinread.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.utils.GreenDaoUtil;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;
import com.vondear.rxtools.RxActivityTool;

/**
 * Created by yanle on 2018/5/15.
 * 设置页面
 */

public class SetActivity extends BaseActivity implements View.OnClickListener{
    private Button btnOut;
    private GreenDaoUtil greenDaoUtil;
    @Override
    void initView() {
        greenDaoUtil = GreenDaoUtil.getDBUtil(getApplicationContext());

        title.setText("设置");
        btnOut = findViewById(R.id.btn_out);

        btnOut.setOnClickListener(this);
    }

    @Override
    int getResId() {
        return R.layout.activity_set;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_out:
                Log.d("TAG1", "退出登录");
                greenDaoUtil.delAll();
                SaveOrDeletePrefrence.deleteAll(getApplicationContext());
                /* SaveOrDeletePrefrence.delete(mContext.getApplicationContext(), "username");
                SaveOrDeletePrefrence.delete(mContext.getApplicationContext(), "password");*/
                Intent intent = new Intent(SetActivity.this, LoginActivity.class);
                startActivity(intent);
                RxActivityTool.finishAllActivity();
                break;
        }
    }
}
