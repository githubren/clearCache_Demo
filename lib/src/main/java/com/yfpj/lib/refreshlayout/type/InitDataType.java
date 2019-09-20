package com.yfpj.lib.refreshlayout.type;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class InitDataType {
    @IntDef({TYPE_REFRESH_SUCCESS, TYPE_REFRESH_FAIL,TYPE_LOAD_MORE_SUCCESS,TYPE_LOAD_MORE_FAIL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface InitDataTypeChecker {
    }

    /**
     * 刷新成功
     */
    @InitDataTypeChecker
    public static final int TYPE_REFRESH_SUCCESS = 1;

    /**
     * 刷新失败
     */
    @InitDataTypeChecker
    public static final int TYPE_REFRESH_FAIL = 2;

    /**
     * 加载更多成功
     */
    @InitDataTypeChecker
    public static final int TYPE_LOAD_MORE_SUCCESS = 3;

    /**
     * 加载更多失败
     */
    @InitDataTypeChecker
    public static final int TYPE_LOAD_MORE_FAIL = 4;
}
