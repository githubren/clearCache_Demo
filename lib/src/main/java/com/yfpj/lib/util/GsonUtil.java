package com.yfpj.lib.util;

import android.util.Base64;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
public class GsonUtil {

    private static final Gson gson = new Gson();

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public static String toBase64(String str) {
        String value = "";
        try {
            value = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return value;
    }
}
