package com.yfpj.lib.http;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerTransformer<T> implements ObservableTransformer<T, T> {

    @Override
    public ObservableSource apply(Observable upstream) {
        return upstream.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread());
    }
}
