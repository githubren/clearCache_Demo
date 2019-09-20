package com.yfpj.lib.http;

import android.support.annotation.Nullable;

import io.reactivex.disposables.Disposable;

/**
 * Created by fire on 2017/9/18 0018.
 * 网络请求监听基类
 */

public interface RequestCallback<T> {

    /**
     * 请求之前调用
     */
    void beforeRequest(Disposable disposable);

    /**
     * 请求错误调用，多个请求，确定是那一个请求失败了
     */
    void requestError(@Nullable String msg, int type);

    /**
     * 请求完成调用
     */
    void requestComplete();

    /**
     * 请求成功调用,type类型 作用于一个页面，多个请求。
     */
    void requestSuccess(T data, String msg, int type);

}
