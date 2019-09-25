package com.example.yfsl.clearcache_demo;

import android.app.Application;

import com.example.yfsl.clearcache_demo.utils.DataCleanUtil;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //每次启动应用清空数据库
        DataCleanUtil.cleanDatabases(this);
    }
}
