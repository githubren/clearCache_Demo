package com.yfpj.lib.base.presenter;

/**
 * Created by fire on 2017/10/11 0011.
 */

public interface BaseNewListPresenter extends BasePresenter {
    /**
     * 下拉刷新
     */
    void onRefresh();

    /**
     * 加载更多
     */
    void loadMoreData();

    /**
     * 空布局加载
     */
    void emptyLoad();
}