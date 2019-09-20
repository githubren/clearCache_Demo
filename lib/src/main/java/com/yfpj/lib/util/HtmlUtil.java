package com.yfpj.lib.util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public class HtmlUtil {
    public static Spanned fromHtml(String body) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Html.fromHtml(body);
        } else {
            return Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY);
        }
    }


}
