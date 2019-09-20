package com.yfpj.lib.http;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class HostType {
    @IntDef({BASE_URL_HOST, FILE_URL_HOST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HostTypeChecker {
    }

    /**
     * 基本的外网
     */
    @HostType.HostTypeChecker
    public static final int BASE_URL_HOST = 1;
    /**
     * 上传文件的外网
     */
    @HostType.HostTypeChecker
    public static final int FILE_URL_HOST = 2;


}
