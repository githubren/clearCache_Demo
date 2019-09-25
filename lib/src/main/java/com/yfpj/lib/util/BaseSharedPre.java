package com.yfpj.lib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/**
 * Created by fire on 2017/9/20 0020.
 */

public abstract class BaseSharedPre {
    private static String PREFS_NAME = "sharedpref";
    protected static final String PREFS_STR_INVALID = "";

    public static boolean isInvalidPrefString(String value) {
        return value == null || "".equals(value);
    }

    public static void setBoolean(String key, boolean value, Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key,boolean defaultValue,Context c){
        return c.getSharedPreferences(PREFS_NAME,0).getBoolean(key,defaultValue);
    }

    public static boolean getBoolean(String key, Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getBoolean(key, false);
    }

    public static void setString(String key, String value, Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString(key, encryption(value));
        editor.commit();
    }

    public static String getString(String key, Context c) {
        return decrypted(c.getSharedPreferences(PREFS_NAME, 0).getString(key, ""));
    }

    public static void setInt(String key, int value, Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getInt(key, -1);
    }

    /**
     * 加密
     */
    private static String encryption(String str) {
        return String.valueOf(Base64.encodeToString(str.getBytes(), Base64.DEFAULT));
    }

    /**
     * 解密
     */
    private static String decrypted(String str) {
        if (isInvalidPrefString(str)) {
            return PREFS_STR_INVALID;
        }
        str = new String(Base64.decode(str.getBytes(),
                Base64.DEFAULT));
        return str;
    }

}
