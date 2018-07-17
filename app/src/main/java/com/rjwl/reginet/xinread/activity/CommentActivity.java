package com.rjwl.reginet.xinread.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.adapter.CommentAdapter;
import com.rjwl.reginet.xinread.model.Comment;
import com.rjwl.reginet.xinread.presenter.CommentPresenter;
import com.rjwl.reginet.xinread.presenter.MyCommentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/11.
 */

public class CommentActivity extends BaseActivity implements MyCommentView {
    private ListView lvComment;
    private TextView tvNoneComm;
    private LinearLayout llSendComm;
    private EditText etSend;
    private Button btnSend;
    private List<Comment> commentList;
    private CommentAdapter commentAdapter;
    private String bookId;
    private String studentId;
    private CommentPresenter commentPresenter;

    private boolean isBook;

    @Override
    void initView() {
        title.setText("读书笔记");
        Intent intent = getIntent();

        bookId = intent.getStringExtra("bookId");
        studentId = intent.getStringExtra("id");
        isBook = intent.getBooleanExtra("isBook", false);

        commentPresenter = new CommentPresenter(this);

        lvComment = findViewById(R.id.lv_comment);
        tvNoneComm = findViewById(R.id.tv_none_comment);
        llSendComm = findViewById(R.id.ll_send_comm);
        etSend = findViewById(R.id.sendText);
        btnSend = findViewById(R.id.send_btn);

        commentList = new ArrayList<>();
        //initList();
        commentAdapter = new CommentAdapter(this, commentList, isBook);

        lvComment.setAdapter(commentAdapter);

        if (isBook) {
            commentPresenter.getComment(bookId, isBook);
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str = etSend.getText() + "";
                    Log.d("TAG1", "发表评论:" + str);
                    commentPresenter.sendComment(bookId, studentId, str);
                    etSend.setText("");
                    //commentPresenter.getComment(bookId);
                }
            });
        } else {
            llSendComm.setVisibility(View.GONE);
            commentPresenter.getComment(studentId, isBook);
            //commentPresenter.
        }
    }

    private void initList() {
        Comment comment = new Comment();
        comment.setCreateTime("2018-06-09");
        comment.setName("哈哈");
        comment.setSpeech("不怎么样啊。。。。");
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
        commentList.add(comment);
    }

    @Override
    int getResId() {
        return R.layout.activity_comment;
    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
            //progressDialog = ProgressDialog.show(this, "", "请稍后...");
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("");
            progressDialog.setMessage("请稍后...");
            progressDialog.show();
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
        Toast.makeText(CommentActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void skipView() {

    }

    @Override
    public void showComment() {
        List<Comment> list = commentPresenter.getCommentList();
        commentList.clear();
        if (list != null) {
            commentList.addAll(list);
            tvNoneComm.setVisibility(View.GONE);
        }

        if (commentList.size() == 0) {
            tvNoneComm.setVisibility(View.VISIBLE);
        }
        commentAdapter.notifyDataSetChanged();
    }
}
