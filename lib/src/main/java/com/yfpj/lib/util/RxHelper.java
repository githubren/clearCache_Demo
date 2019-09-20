package com.yfpj.lib.util;


import com.yfpj.lib.http.SchedulerTransformer;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import butterknife.internal.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by fire on 2017/9/7  17:08.
 */

public class RxHelper {

    private static void log(String msg) {
        BaseUtils.logh(RxHelper.class.getSimpleName(), msg);
    }

    static {
        RxJavaPlugins.setErrorHandler(throwable -> {
            if (throwable instanceof InterruptedException) {
                log("Thread interrupted");
            } else if (throwable instanceof InterruptedIOException) {
                log("Io interrupted");
            } else if (throwable instanceof SocketException) {
                log("Socket error");
            } else {
                throwable.printStackTrace();
            }
        });
    }


    /**
     * 时间间隔
     */
    public static Observable<Long> interval() {
        return interval(7);
    }

    public static Observable<Long> interval(int time) {
        return Observable
                .interval(time, TimeUnit.SECONDS)
                .compose(new SchedulerTransformer<>());
    }

    /**
     * 倒计时
     */
    public static Observable<Integer> countDown(final int time) {
        return Observable
                .interval(1, TimeUnit.SECONDS)
                .take(time + 1)
                .flatMap(aLong -> Observable.just(time - aLong.intValue()))
                .compose(new SchedulerTransformer<>());
    }

    /**
     * 延时
     */
    public static Observable<Integer> delay(final int time) {
        return Observable
                .just(time)
                .delay(time, TimeUnit.SECONDS)
                .compose(new SchedulerTransformer<>());
    }

    /**
     * 延时
     */
    public static Observable<Integer> delayMs(final int time) {
        return Observable
                .just(time)
                .delay(time, TimeUnit.MILLISECONDS)
                .compose(new SchedulerTransformer<>());
    }
}
