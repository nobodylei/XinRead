package com.rjwl.reginet.xinread;

import android.support.multidex.MultiDexApplication;
import com.vondear.rxtools.RxTool;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatformConfig;
import cn.jpush.android.api.JPushInterface;


/**
 * Created by Administrator on 2018/5/3.
 */

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        RxTool.init(this);
        //Fresco.initialize(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }
}
