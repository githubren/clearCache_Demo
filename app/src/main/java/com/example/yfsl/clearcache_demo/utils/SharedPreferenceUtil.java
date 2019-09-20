package com.example.yfsl.clearcache_demo.utils;

import android.content.Context;

import com.yfpj.lib.util.BaseSharedPre;

public class SharedPreferenceUtil extends BaseSharedPre {
    //姓名
    private final static String SHARED_NAME_TAG = "name";
    //年龄
    private final static String SHARED_OLD_TAG = "old";
    //是否已婚
    private final static String SHARED_MARRIED_TAG = "married";

    public static void saveName(Context c,String value){
        setString(SHARED_NAME_TAG,value,c);
    }

    public static String getName(Context c){
        return getString(SHARED_NAME_TAG,c);
    }

    public static void saveOld(Context c,int value){
        setInt(SHARED_OLD_TAG,value,c);
    }

    public static int getOld(Context c){
        return getInt(SHARED_OLD_TAG,c);
    }

    public static void saveMarried(Context c,boolean value){
        setBoolean(SHARED_MARRIED_TAG,value,c);
    }

    public static boolean isMarried(Context c){
        return getBoolean(SHARED_MARRIED_TAG,false,c);
    }
}
