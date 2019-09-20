package com.yfpj.lib.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;

public abstract class BaseUtils {

    public static boolean LOG_H = false;

    public static void logh(String tag, String msg) {
        if (LOG_H) {
            Log.d(tag, msg);
        }
    }

    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String formatSize(long size) {
        String hrSize;
        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);
        DecimalFormat dec = new DecimalFormat("0.00");
        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" B");
        }
        return hrSize;
    }

    /**
     * 去掉开始与结尾的冒号
     */
    public static String format(String des) {
        if (TextUtils.isEmpty(des))
            return des;
        if (des.startsWith("\""))
            des = des.substring(1);
        if (des.endsWith("\""))
            des = des.substring(0, des.length() - 1);
        return des;
    }

    public static String format(int time) {
        //以秒为单位
        StringBuffer sb = new StringBuffer();
        int hour = time / 3600;
        int newTime = time % 3600;
        if (hour > 0) {
            sb.append(hour);
            sb.append("h");
        }
        int min = newTime / 60;
        int sec = newTime % 60;
        if (min > 0) {
            sb.append(min);
            sb.append("m");
        }
        sb.append(sec);
        sb.append("s");
        return sb.toString();
    }

    public static boolean isBelowAndroidVersion(int version) {
        return Build.VERSION.SDK_INT < version;
    }

    public static void removeOnGlobleListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    public static String getCurrentVersion(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getCurrentVersionNum(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 打开当前应用的设置中心
     */
    public static void openAppSettingActivity(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(localIntent);
    }

    public static void callPhone(Activity context, String num) {
        if (TextUtils.isEmpty(num)) return;
//        Intent intent = new Intent(Intent.ACTION_CALL);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + num));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            BaseUtils.toastShort(context, "没有拨打电话应用");
        }
    }

    public static List<String> splitStrToList(String questionsid) {
        String[] ids = questionsid.split(",");
        List<String> arr = new ArrayList<>();
        if (ids != null || ids.length > 0) {
            for (String id : ids) {
                arr.add(id);
            }
        }
        return arr;
    }

    public static String getEditTextString(EditText et) {
        Editable text = et.getText();
        return text != null && text.toString().trim().length() != 0 ? text.toString().trim() : null;
    }

    public static void toastShort(Context context, int id) {
        if (context != null)
            makeText(context, context.getString(id), Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, String msg) {
        if (context != null)
            makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, String msg, int time) {
        if (context != null) {
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setDuration(time);
            toast.show();
        }
    }

    public static void setGone(View... views) {
        if (views != null && views.length > 0) {
            View[] v = views;
            int size = views.length;

            for (int i = 0; i < size; ++i) {
                View view = v[i];
                if (view != null && view.getVisibility() != View.GONE) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    public static void setGoneVisible(View view, View... views) {
        if (view != null)
            view.setVisibility(View.GONE);
        setVisible(views);
    }

    public static void setVisible(View... views) {
        if (views != null && views.length > 0) {
            View[] v = views;
            int size = views.length;

            for (int i = 0; i < size; ++i) {
                View view = v[i];
                if (view != null && view.getVisibility() != View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public static void setInvisible(View... views) {
        if (views != null && views.length > 0) {
            View[] var4 = views;
            int var3 = views.length;

            for (int var2 = 0; var2 < var3; ++var2) {
                View view = var4[var2];
                if (view != null && view.getVisibility() != View.INVISIBLE) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }

    }

    public static String split(String url) {
        if (TextUtils.isEmpty(url))
            return "";
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        return url;
    }


    public static void keyBordHideFromWindow(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    public static void keyBordShowFromWindow(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 格式化后数据：  00:00
     */
    public static String formatTime(long time) {
        StringBuffer sb = new StringBuffer();
        long as = time / 1000;
        as += time % 1000 > 0 ? 1 : 0;
        long min = as / 60;
        long s = as % 60;
        sb.append(addZero(min));
        sb.append(":");
        sb.append(addZero(s));
        return sb.toString();
    }

    private static String addZero(long time) {
        if (time < 10) {
            return TextUtils.concat("0", String.valueOf(time)).toString();
        }
        return String.valueOf(time);
    }

    public static void controlTvFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    public static String[] splitOption(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return new String[]{};
        }
//        if (!txt.contains("\\r\\n")) {
//            return new String[]{txt};
//        }
        return txt.split("\\r\\n");
    }

    public static String[] splitOptionThroughN(String txt) {
        if (TextUtils.isEmpty(txt)) {
            return new String[]{};
        }
//        if (!txt.contains("\\r\\n")) {
//            return new String[]{txt};
//        }
        return txt.split("\\n");
    }

    public static String removerepeatedchar(String target) {
        if (TextUtils.isEmpty(target)) return target;
        if (target.length() == 1) return target;
        StringBuilder sb = new StringBuilder();
        List<Character> ls = new ArrayList<>();
        int i = 0, len = target.length();
        while (i < len) {
            char c = target.charAt(i);
            i++;
            if (ls.contains(c))
                continue;
            ls.add(c);
        }
        for (Character c : ls) {
            sb.append(c);
        }
        return sb.toString();
    }

}
