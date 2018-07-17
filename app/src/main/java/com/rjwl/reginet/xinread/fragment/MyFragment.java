package com.rjwl.reginet.xinread.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.activity.BooksActivity;
import com.rjwl.reginet.xinread.activity.CommentActivity;
import com.rjwl.reginet.xinread.activity.RankActivity;
import com.rjwl.reginet.xinread.activity.ReadActivity;
import com.rjwl.reginet.xinread.activity.SetActivity;
import com.rjwl.reginet.xinread.activity.SetMyInfoActivity;
import com.rjwl.reginet.xinread.activity.ShareActivity;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.model.Student;
import com.rjwl.reginet.xinread.utils.GreenDaoUtil;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;
import com.rjwl.reginet.xinread.utils.StrUtils;

/**
 * Created by Administrator on 2018/5/3.
 * 个人中心
 */

public class MyFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;

    private SimpleDraweeView imgMyHead;
    private TextView tvMyName;
    private TextView tvMyClass;
    private NavigationView navView;
    private View view;
    private Student student;
    private int id;
    private GreenDaoUtil greenDaoUtil;

    public static final String TAG = "TAG1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, null);
        this.mContext = getActivity();
        initView(view);
        return view;
    }

    private void initView(View view) {
        id = SaveOrDeletePrefrence.lookInt(mContext.getApplicationContext(), "id");
        Log.d("TAG1", id + "");
        greenDaoUtil = GreenDaoUtil.getDBUtil(mContext.getApplicationContext());
        student = greenDaoUtil.getStudetnById(id);


        imgMyHead = view.findViewById(R.id.img_my_head);
        tvMyName = view.findViewById(R.id.tv_my_name);
        tvMyClass = view.findViewById(R.id.tv_my_class);
        navView = view.findViewById(R.id.nav_view);

        if (student != null) {
            Log.d("TAG1", "myFragment:" + student);
            if (student.getName() == null || "null".equals(student.getName())) {
                String phone = SaveOrDeletePrefrence.look(getActivity().getApplicationContext(), "username") + "";
                tvMyName.setText(StrUtils.setPhone(phone));
            } else {
                tvMyName.setText(student.getName());
            }
            tvMyClass.setText(StrUtils.intTOStr(student.getGradeID()) + "年"
                    + StrUtils.intTOStr(student.getYesorno()) + "班");
            imgMyHead.setImageURI(Uri.parse(Constant.IMG + student.getHeadPic() + ""));
        } else {
            //tvMyName.setText(SaveOrDeletePrefrence.look(getActivity().getApplicationContext(), "username"));
            tvMyName.setText("未设置");
            tvMyClass.setText("一年一班");
        }

        imgMyHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SetMyInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("student", student);
                bundle.putBoolean("isHome", false);
                intent.putExtra("stuBundle", bundle);
                startActivity(intent);
            }
        });

        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.my_books:
                intent = new Intent(mContext, BooksActivity.class);
                intent.putExtra("title", "我的书架");
                intent.putExtra("btn", "移出书架");
                startActivity(intent);
                Log.d(TAG, "菜单:我的书架");
                break;
            case R.id.get_read:
                intent = new Intent(mContext, ReadActivity.class);

                if (student != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("student", student);
                    intent.putExtra("stuBundle", bundle);
                }
                startActivity(intent);
                Log.d(TAG, "菜单:阅读成就");
                break;
            case R.id.read_notes:
                Log.d(TAG, "菜单:读书笔记");
                intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
            case R.id.rank_list:
                intent = new Intent(mContext, RankActivity.class);
                startActivity(intent);
                Log.d(TAG, "菜单:排行榜");
                break;
            case R.id.menu_set:
                Log.d(TAG, "菜单:设置");
                intent = new Intent(mContext, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.share:
                Log.d(TAG, "菜单：分享");
                intent = new Intent(mContext, ShareActivity.class);
                startActivity(intent);
            /*case R.id.help:
                Toast.makeText(mContext, "暂未开通", Toast.LENGTH_SHORT).show();
                break;*/
            default:
                break;
        }
        return false;
    }
}
