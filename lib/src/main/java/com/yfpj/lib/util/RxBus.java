package com.yfpj.lib.util;

import android.support.annotation.NonNull;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by fire on 2017/7/14  10:10.
 */

public class RxBus {
    private volatile static RxBus sInstance;

    private static void log(String msg) {
        BaseUtils.logh(RxBus.class.getSimpleName(), msg);
    }

    static {
        RxJavaPlugins.setErrorHandler(throwable -> {
            if (throwable instanceof InterruptedException) {
                log("Thread interrupted");
            } else if (throwable instanceof InterruptedIOException) {
                log("Io interrupted");
            } else if (throwable instanceof SocketException) {
                log("Socket error");
            }
        });
    }

    private RxBus() {
    }

    public static RxBus get() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    /**
     * 存储某个标签的Subject集合
     */
    private ConcurrentMap<Object, List<Subject>> mSubjectMapper = new ConcurrentHashMap<>();

    /**
     * 注册事件
     *
     * @param tag   标签
     * @param clazz 类
     * @param <T>   类型
     * @return 被观察者
     */
    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> clazz) {
        List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            mSubjectMapper.put(tag, subjectList);
        }

        Subject<T> subject;
        // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
        subjectList.add(subject = PublishSubject.create());
//        KLog.e("{register}subjectMapper: " + mSubjectMapper);
        return subject;
    }

    /**
     * 取消注册事件
     *
     * @param tag        标签
     * @param observable 被观察者
     */
    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        final List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null != subjectList) {
            subjectList.remove(observable);
            if (subjectList.isEmpty()) {
                // 集合数据为0的时候移map除掉tag
                mSubjectMapper.remove(tag);
            }
        }
//        KLog.e("{unregister}subjectMapper: " + mSubjectMapper);
    }

    /**
     * 发送事件
     *
     * @param tag     标签
     * @param content 发送的内容
     */
    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object content) {
        final List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null != subjectList && !subjectList.isEmpty()) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
    }
}
