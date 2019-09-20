package com.yfpj.lib.base.presenter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.yfpj.lib.base.view.BaseNewListView;
import com.yfpj.lib.refreshlayout.type.InitDataType;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/10/11 0011.
 */

public abstract class BaseNewListPresenterImp<T extends BaseNewListView, V> extends BasePresenterImp<T, V> implements BaseNewListPresenter {

    protected int initPage = 1;//默认第一页
    protected int page;//用于分页请求
    protected boolean isRefresh = true;//默认下拉刷新

    public BaseNewListPresenterImp(T mView) {
        super(mView, false);
    }

    /**
     * 控制加载更多传入的初始值
     *
     * @param mView
     * @param page
     */
    public BaseNewListPresenterImp(T mView, int page) {
        super(mView, false);
        this.page = page;
        initPage = page;
    }

    protected abstract void onRefresh(int page);

    protected abstract void onLoadMoreData(int page);

    @SuppressLint("CheckResult")
    @Override
    public void emptyLoad() {
        //延迟500毫秒加载
        Observable.just(1).delay(500, TimeUnit.MILLISECONDS)
                .subscribe(integer -> onRefresh(), throwable -> onRefresh());
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        page = initPage;
        onRefresh(page);
    }

    @Override
    public void loadMoreData() {
        isRefresh = false;
        onLoadMoreData(++page);
    }

    @Override
    public void requestError(@Nullable String msg, int type) {
        if (mView == null) return;
        hideProgress();
        if (page != 1) {
            page--;
        }
        refreshFail(msg);
    }

    private void refreshFail(@Nullable String msg) {
        if (mView == null) return;
        hideProgress();
        if (needSelfDeal()) {
            requestFail(msg, isRefresh);
            return;
        }
//        if (isRefresh)
        mView.dealListView(null, msg, isRefresh ? InitDataType.TYPE_REFRESH_FAIL : InitDataType.TYPE_LOAD_MORE_FAIL);
//        else
//            mView.dealListView(null, msg, InitDataType.TYPE_LOAD_MORE_FAIL);
    }


    @Override
    public void requestSuccess(V data, String msg, int type) {
        hideProgress();
        if (mView == null) return;
        if (needSelfDeal()) {
            moreDeal(data, msg, type, isRefresh);
            return;
        }
//        if (isRefresh)
        mView.dealListView(data, msg, isRefresh ? InitDataType.TYPE_REFRESH_SUCCESS : InitDataType.TYPE_LOAD_MORE_SUCCESS);
//        else
//            mView.dealListView(data, msg, InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    /**
     * 若返回true 必须重写 requestFail，moreDeal 这两个方法
     */
    protected boolean needSelfDeal() {
        return false;
    }


    /**
     * 请求失败，自己处理
     */
    protected void requestFail(String msg, boolean isRefresh) {
    }

    /**
     * 更多处理
     *
     * @return true 自己处理 requestSuccess的数据
     */
    protected void moreDeal(V data, String msg, int type, boolean isRefresh) {
    }
}
