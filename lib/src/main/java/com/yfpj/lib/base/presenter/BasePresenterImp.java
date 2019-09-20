package com.yfpj.lib.base.presenter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.yfpj.lib.base.view.BaseView;
import com.yfpj.lib.http.RequestCallback;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by fire on 2017/9/18 0018.
 */

public abstract class BasePresenterImp<T extends BaseView, V> implements BasePresenter, RequestCallback<V> {

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    protected T mView;
    private boolean showProgress;

    public BasePresenterImp(T mView) {
        this(mView, true);
    }

    /**
     * 如果在含有自动刷新的view中使用，则必须使用这个构造器
     *
     * @param mView
     * @param showProgress true 显示，false 不显示
     */
    public BasePresenterImp(T mView, boolean showProgress) {
        this.showProgress = showProgress;
        this.mView = mView;
    }

    /**
     * 需要显示加载中，对话框
     */
    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    @Override
    public void onResume() {

    }

    protected void addToCompositeDis(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onDestory() {
        if (!mCompositeDisposable.isDisposed())
            mCompositeDisposable.dispose();
    }

    @Override
    public void beforeRequest(Disposable disposable) {
        mCompositeDisposable.add(disposable);
        showProgress();
    }

    protected void showProgress() {
        if (mView != null && showProgress)
            mView.showProgress();
    }

    protected void hideProgress() {
        if (mView != null)
            mView.hideProgress();
    }

    @Override
    public void requestError(@Nullable String msg, int type) {
        Log.e("TAG", msg);
        if (mView == null) return;
        if (!TextUtils.isEmpty(msg))
            mView.toast(msg);
        hideProgress();
    }

    @Override
    public void requestComplete() {
        hideProgress();
    }
}
