package com.rjwl.reginet.xinread.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.model.Game;
import com.rjwl.reginet.xinread.presenter.GamePresenter;
import com.rjwl.reginet.xinread.presenter.MyGameView;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;
import com.rjwl.reginet.xinread.utils.StrUtils;
import com.vondear.rxtools.RxActivityTool;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/11.
 * 闯关游戏
 */

public class GameActivity extends BaseActivity implements View.OnClickListener, MyGameView {
    //弹框
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private GamePresenter gamePresenter;
    private List<Game> gameList;
    private int currItem = 0;
    private static int score = 0;
    private String answer = "";

    private TextView tvGameBigNumber;
    private TextView tvGameNumber;
    private TextView tvGameIssue;
    private TextView tvGameA;
    private TextView tvGameB;
    private TextView tvGameC;
    private TextView tvGameD;
    private ImageView imgA;
    private ImageView imgB;
    private ImageView imgC;

    private Book book;

    @Override
    void initView() {

        gamePresenter = new GamePresenter(GameActivity.this);
        gameList = new ArrayList<>();
        book = (Book) getIntent().getBundleExtra("bookBundle").get("book");
        Log.d("TAG1", "bookId" + book.getId());
        gamePresenter.getGame(book.getId() + "",
                SaveOrDeletePrefrence.lookInt(getApplicationContext(), "id") + "");

        title.setText("闯关游戏");
        tvGameBigNumber = findViewById(R.id.tv_game_big_number);
        tvGameNumber = findViewById(R.id.tv_game_number);
        tvGameIssue = findViewById(R.id.tv_game_issue);
        tvGameA = findViewById(R.id.tv_game_a);
        tvGameB = findViewById(R.id.tv_game_b);
        tvGameC = findViewById(R.id.tv_game_c);
        tvGameD = findViewById(R.id.tv_game_d);
        imgA = findViewById(R.id.img_game_a);
        imgB = findViewById(R.id.img_game_b);
        imgC = findViewById(R.id.img_game_c);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "font/new_fonts.ttf");
        tvGameBigNumber.setTypeface(iconfont);

        alertDialogBuilder = new AlertDialog.Builder(GameActivity.this, R.style.dialog);
        alertDialog = alertDialogBuilder.create();

        initIssue();

        tvGameA.setOnClickListener(this);
        tvGameB.setOnClickListener(this);
        tvGameC.setOnClickListener(this);
        tvGameD.setOnClickListener(this);
        imgA.setOnClickListener(this);
        imgB.setOnClickListener(this);
        imgC.setOnClickListener(this);

    }

    public void test1() {
        /*Game game1 = new Game();
        game1.setIssue("哈哈哈哈哈哈哈");
        game1.setAnswerA("哈喽");
        game1.setAnswerB("你好");
        game1.setAnswerC("嗨嗨");
        game1.setAnswer("嗨嗨");
        Game game2 = new Game();
        game2.setIssue("1+1");
        game2.setAnswerA("8");
        game2.setAnswerB("5");
        game2.setAnswerC("9");
        game2.setAnswer("5");
        Game game3 = new Game();
        game3.setIssue("aaaaaaaaa");
        game3.setAnswerA("j");
        game3.setAnswerB("f");
        game3.setAnswerC("n");
        game3.setAnswer("n");
        gameList.add(game1);
        gameList.add(game3);
        gameList.add(game2);
        gameList.add(game1);
        gameList.add(game3);
        gameList.add(game2);
        gameList.add(game3);
        gameList.add(game1);
        gameList.add(game3);
        gameList.add(game1);*/
    }

    @Override
    int getResId() {
        return R.layout.activity_game;
    }

    @Override
    public void onClick(View v) {
        if (gameList.size() == 0) return;
        String answerRight = gameList.get(currItem).getTru();
        boolean isDialog = false;
        currItem++;
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            //initIssue();
            currItem = 0;
        }
        if (currItem >= gameList.size()) {
            currItem = 0;
            isDialog = true;
            //score = 0;
        }

        switch (v.getId()) {
            case R.id.tv_game_a:
            case R.id.img_game_a:
                //Log.d("game", "选项A" + ((TextView) v).getText());
                answer = tvGameA.getText() + "";
                initIssue();
                break;
            case R.id.tv_game_b:
            case R.id.img_game_b:
                //Log.d("game", "选项B" + ((TextView) v).getText());
                answer = tvGameB.getText() + "";
                initIssue();
                break;
            case R.id.tv_game_c:
            case R.id.img_game_c:
                //Log.d("game", "选项C" + ((TextView) v).getText());
                answer = tvGameC.getText() + "";
                initIssue();
                break;
            case R.id.tv_game_d:
                Log.d("game", "选项D" + ((TextView) v).getText());
                initIssue();
                break;
            case R.id.btn_game_next:
                Log.d("game", "完成闯关");
                score = 0;
                /*finish();
                Intent intent = new Intent(this, GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", book);
                intent.putExtra("bookBundle", bundle);
                startActivity(intent);*/
                onBackPressed();
                finish();
                break;
            case R.id.btn_game_new:
                Log.d("game", "重新闯关");
                score = 0;
                finish();
                Intent intent2 = new Intent(this, GameActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("book", book);
                intent2.putExtra("bookBundle", bundle2);
                startActivity(intent2);
                break;
            default:
                break;
        }
        //Log.d("TAG1", "得到的答案：" + answer + " 正确答案：" + answerRight);
        if (answer.equals(answerRight)) {
            //answer = "";
            Log.d("TAG1", "得到的答案：" + answer + " 正确答案：" + answerRight);
            score++;
            Log.d("TAG1", "分数" + score);
        }
        if (isDialog) {
            gamePresenter.sendScore(score, book, SaveOrDeletePrefrence.lookInt(getApplicationContext(), "id"));
            isDialog = false;
            View view = LayoutInflater.from(GameActivity.this).inflate(R.layout.dialog_game, null);
            //初始化Dialog布局
            initDialog(view);
            alertDialog.setView(view);
            alertDialog.show();

        }


    }

    private TextView tvGameOver;
    private TextView tvGameCurrPro;
    private ProgressBar pbGame;
    private Button btnNext;
    private Button btnNew;

    private void initDialog(View view) {
        tvGameOver = view.findViewById(R.id.tv_game_over);//成功失败
        tvGameCurrPro = view.findViewById(R.id.tv_game_curr_pro);//当前进度百分比
        pbGame = view.findViewById(R.id.pb_game);//进度条
        btnNext = view.findViewById(R.id.btn_game_next);
        btnNew = view.findViewById(R.id.btn_game_new);

        //int right = score * 10;
        Log.d("TAG1", "得分" + (score * 10) + ";" + score);
        pbGame.setMax(100);
        //创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后1位
        numberFormat.setMaximumFractionDigits(1);
        String result = numberFormat.format((float) score / (float) gameList.size() * 100);
        Log.d("TAG1", "获得分数result" + result);
        int s = (int) (((double) score / (double) gameList.size()) * 100);
        if (score == gameList.size()) {
            pbGame.setProgress(100);
        } else {
            pbGame.setProgress(s);
        }
        Log.d("TAG1", "获得分数s" + s);
        tvGameCurrPro.setText(result + "%");
        if (s >= 60) {
            tvGameOver.setText("闯关成功");
        } else {
            tvGameOver.setText("闯关失败");
        }
        score = 0;

        btnNew.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        score = 0;
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            return;
        }
    }

    @Override
    public void onBackPressed() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            return;
        }
        finish();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hidenLoading() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void skipView() {

    }

    @Override
    public void initGame() {
        List<Game> list = gamePresenter.getGameList();
        if (list != null) {
            gameList.addAll(list);
        }
        initIssue();
    }

    public void initIssue() {
        if (gameList.size() == 0) {
            return;
        }
        tvGameBigNumber.setText("第" + StrUtils.intTOStr(currItem + 1) + "题");
        tvGameNumber.setText((currItem + 1) + "/" + gameList.size());
        tvGameIssue.setText((currItem + 1) + ". " + gameList.get(currItem).getText() + "");
        tvGameA.setText(gameList.get(currItem).getA() + "");
        tvGameB.setText(gameList.get(currItem).getB() + "");
        tvGameC.setText(gameList.get(currItem).getC() + "");
    }
}
