package com.rjwl.reginet.xinread.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rjwl.reginet.xinread.R;

/**
 * Created by Administrator on 2018/5/3.
 * 名师导读
 */

public class CourseFragment extends Fragment {
    private View view;
    private TextView tvCourse;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course, null);

        initView();

        return view;
    }

    private void initView() {
        tvCourse = view.findViewById(R.id.tv_course);

        Typeface iconfont = Typeface.createFromAsset(getActivity().getAssets(), "font/new_fonts.ttf");
        tvCourse.setTypeface(iconfont);
    }
}
