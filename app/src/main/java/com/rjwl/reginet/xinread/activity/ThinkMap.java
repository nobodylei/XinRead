package com.rjwl.reginet.xinread.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.model.Book;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;

/**
 * Created by Administrator on 2018/6/23.
 */

public class ThinkMap extends AppCompatActivity {
    private WebView wvThinkMap;
    //private ProgressBar progressBar;
    private Book book;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.think_map);

        initView();
    }

    private void initView() {
        //title.setText("思维导图");
        //rlBase.setVisibility(View.GONE);

        book = (Book) getIntent().getBundleExtra("bookBundle").get("book");

        wvThinkMap = findViewById(R.id.web_think);
        //progressBar= findViewById(R.id.progressbar);//进度条

        wvThinkMap.loadUrl("http://47.93.24.30/ReadingBooks/mindMap.html?studentId="
                + SaveOrDeletePrefrence.lookInt(getApplicationContext(), "id") + "&bookId=" + book.getId());
        //wvThinkMap.loadUrl("www.baidu.com");

       /*wvThinkMap.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });*/

        wvThinkMap.setWebViewClient(webViewClient);

        WebSettings webSettings = wvThinkMap.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        webSettings.setDomStorageEnabled(true);

        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        //不显示webview缩放按钮
        //webSettings.setDisplayZoomControls(false);

        webSettings.setUseWideViewPort(true);
        wvThinkMap.setInitialScale(200);
    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            //progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
           /* Log.i("ansen","拦截url:"+url);
            if(url.equals("http://www.google.com/")){
                Toast.makeText(ThinkMap.this,"国内不能访问google,拦截该url",Toast.LENGTH_LONG).show();
                return true;//表示我已经处理过了
            }*/
            view.loadUrl(url);
            return true;
        }

    };


}
