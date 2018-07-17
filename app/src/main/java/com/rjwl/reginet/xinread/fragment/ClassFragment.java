package com.rjwl.reginet.xinread.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.adapter.ClassAdapter;
import com.rjwl.reginet.xinread.model.MyClass;
import com.rjwl.reginet.xinread.presenter.SearchClassPresenter;
import com.rjwl.reginet.xinread.presenter.MyMainView;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 * 班级页面
 */

public class ClassFragment extends Fragment implements MyMainView {
    private Context mContext;
    private View view;
    private SearchView searchClass;
    private ListView lvClass;
    private List<MyClass> mMyClassList;
    private ClassAdapter classAdapter;
    private MyClass mySearchClass;
    private TextView tvNoneClass;

    private SearchClassPresenter mainPresenter;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class, null);
        this.mContext = getActivity();
        initView();
        Log.d("TAG1", "ClassFragment onCreateViwe");
        return view;
    }

    private void initView() {
        mainPresenter = new SearchClassPresenter(ClassFragment.this);

        tvNoneClass = view.findViewById(R.id.tv_none_class);
        searchClass = view.findViewById(R.id.sv_class_serch);
        lvClass = view.findViewById(R.id.lv_class);
        //初始化搜索框
        //searchClass.setBackgroundResource(R.drawable.search_view_line);
        mMyClassList = new ArrayList<>();

        Log.d("TAG1", "ClassFragment initView");
        initClass();
        classAdapter = new ClassAdapter(getActivity(), mMyClassList, mainPresenter);
        lvClass.setAdapter(classAdapter);

        setSearchView();
        searchClass.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("TAG1", "搜索班级：" + query);
                mainPresenter.searchClass(query, SaveOrDeletePrefrence.lookInt(getActivity().getApplicationContext(), "id") + "");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("TAG1", "班级输入搜索：" + newText);
                return true;
            }
        });

    }

    //设置搜索框
    private void setSearchView() {
        if (searchClass != null) {
            //获取到TextView的ID
            /*int id = searchClass.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            //获取到TextView的控件
            TextView textView = searchClass.findViewById(id);
            //设置字体大小为14sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);//14sp
            android.widget.LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.bottomMargin = -8;
            textView.setLayoutParams(layoutParams);*/


            int search_mag_icon_id = searchClass.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
            ImageView mSearchViewIcon = searchClass.findViewById(search_mag_icon_id);
            android.widget.LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) mSearchViewIcon.getLayoutParams();

            int id = searchClass.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchTextView = searchClass.findViewById(id);
            //获取出SearchView中间文本的布局参数，跟上面的图标布局文件进行对比
            android.widget.LinearLayout.LayoutParams textLayoutParams = (android.widget.LinearLayout.LayoutParams) searchTextView.getLayoutParams();

            //发现textLayoutParams中的高度是固定108的，而图标的布局文件的高度是-2也就是WrapContent，将文本的高度也改成WrapContent就可以了
            textLayoutParams.height = textLayoutParams.WRAP_CONTENT;
            searchTextView.setLayoutParams(textLayoutParams);
            searchTextView.setTextSize(15);

            try {        //--拿到字节码
                Class<?> argClass = searchClass.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchClass);
                //--设置背景
                mView.setBackgroundResource(R.drawable.search_view_line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        searchClass.clearFocus();
        searchClass.setFocusable(false);

        searchClass.setFocusableInTouchMode(false);

        searchClass.requestFocus();
        classAdapter.notifyDataSetChanged();
    }

    private void initClass() {
        /*Log.d("TAG1", "ClassFragment iniClass");
        MyClass myClass = new MyClass();
        myClass.setClassname("复仇者联盟");
        myClass.setCount(27);
        myClass.setId(22);*/
       /* MyClass myClass1 = new MyClass();
        myClass1.setClassName("正义联盟");
        myClass1.setClassNumber(21);
        myClass1.setClassId(8823);*/
        mainPresenter.searchClassList(SaveOrDeletePrefrence.lookInt(
                getActivity().getApplicationContext(), "id") + "");
        if (mMyClassList.size() == 0) {
            tvNoneClass.setVisibility(View.VISIBLE);
        } else {
            tvNoneClass.setVisibility(View.GONE);
        }
        //mMyClassList = mainPresenter.getClassList();
    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setTitle("");
            progressDialog.setMessage("请稍后...");
            progressDialog.show();
            //progressDialog = ProgressDialog.show(getActivity(), "", "正在加载...");
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle("");
            progressDialog.setMessage("正在加载...");
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
        Toast.makeText(mContext,  "" + msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void skipView() {

    }

    @Override
    public void updateView() {
        mySearchClass = mainPresenter.getMyClass();
        //Log.d("TAG1", "拿到的MyClass;" + mySearchClass.toString());
        mMyClassList.clear();
        if (mySearchClass != null) {
            mMyClassList.add(mySearchClass);
            Log.d("TAG1", "拿到的集合：" + mMyClassList.size());
            lvClass.setVisibility(View.VISIBLE);
            tvNoneClass.setVisibility(View.GONE);
        } else {
            lvClass.setVisibility(View.GONE);
            tvNoneClass.setVisibility(View.VISIBLE);
        }
        classAdapter.notifyDataSetChanged();
    }

    @Override
    public void upDataBtn() {
        mMyClassList.get(0).setResult("等待通过");
        classAdapter.notifyDataSetChanged();
    }
}
