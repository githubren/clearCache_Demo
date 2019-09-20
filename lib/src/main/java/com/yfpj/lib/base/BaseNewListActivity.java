package com.yfpj.lib.base;

import android.support.v7.widget.RecyclerView;

import com.yfpj.lib.BaseActivity;
import com.yfpj.lib.base.presenter.BaseNewListPresenter;
import com.yfpj.lib.base.view.BaseNewListView;
import com.yfpj.lib.refreshlayout.BaseRecyclerViewAdapter;
import com.yfpj.lib.refreshlayout.OnRefreshListener;
import com.yfpj.lib.refreshlayout.RefreshView;
import com.yfpj.lib.refreshlayout.type.InitDataType;

import java.util.List;

/**
 * Created by fire on 2017/10/13 0013.
 */

public abstract class BaseNewListActivity<T extends BaseNewListPresenter, V, K> extends BaseActivity<T>
        implements BaseNewListView<V, K>/*, SwipeRefreshLayout.OnRefreshListener*/, OnRefreshListener {

    protected BaseRecyclerViewAdapter<K> adapter;
    protected RecyclerView mRecyclerView;
    //    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isFirstRefresh = true;

    /**
     * 这个方法应该在initListInfoData之前调用,用于初始化recycler和swipe控件
     */
    protected abstract void initRecyclerAndSwipe();

    //    {@IdRes int recyclerResId, @IdRes int swipeResId
//        mRecyclerView = (RecyclerView) findViewById(recyclerResId);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(swipeResId);
//    }
    protected abstract T getPresenter();

    /**
     * 继承自这个类的act，如果需要重写这个方法，必须调用super。
     */
    @Override
    protected void initListInfoData() {
        mPresenter = getPresenter();
        initRecyclerAndSwipe();
//        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.setOnRefreshListener(this);
//            SwipeInit.init(mSwipeRefreshLayout);
//        }
        if (adapter == null) {
            initNewsList();
        }
        adapter.onShowInitView(true);
        if (autoAsyn())
            onRefresh(null);
    }

    /**
     * 控制是否加载更多
     */
    protected boolean canLoadMore() {
        return true;
    }

    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {

    }

    protected abstract BaseRecyclerViewAdapter<K> getAdapter();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    /**
     * item listener
     */
    protected void setListener(List<K> data, int position) {
    }

    private void initNewsList() {
        adapter = getAdapter();

        adapter.setOnItemClickListener((view, position) -> {
            BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) mRecyclerView.getAdapter();
            List data = adapter.getAdapterData();
            int realPosition = position - adapter.getHeaderCount();
            if (data != null && !data.isEmpty() && realPosition < data.size() && realPosition >= 0) {
                setListener(data, realPosition);
            }
        });

        adapter.setOnEmptyClickListener(view -> {
            if (mPresenter != null) {
                adapter.showInitView();
                mPresenter.emptyLoad();
            }
        });

        if (canLoadMore()) {
            adapter.setOnLoadMoreListener(() -> {
                //加载更多
                if (mPresenter != null)
                    mPresenter.loadMoreData();
            });
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(getLayoutManager());
        initRecyclerViewItemDecoration(mRecyclerView);
//        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(),
//        LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(adapter);

    }

    /**
     * 控制进入页面是否自动加载
     */
    protected boolean autoAsyn() {
        return true;
    }

    @Override
    public void updateRecycleView(List<K> list, String msg, @InitDataType.InitDataTypeChecker int type) {
        updateRecycleView(list, msg, type, false);
    }

    @Override
    public void updateRecycleView(List<K> list, String msg, @InitDataType.InitDataTypeChecker int type, boolean mustIsNull) {
//        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
        updateDealView();
        isFirstRefresh = false;
        adapter.onShowInitView(false);
        adapter.onShowEmptyView(false, msg);
        switch (type) {
            case InitDataType.TYPE_REFRESH_SUCCESS:
                if (mustIsNull && (list == null || list.isEmpty())) {
                    adapter.updateNull();
                    adapter.onShowEmptyView(true, msg);
                } else {
                    adapter.update(list);
                }
                break;
            case InitDataType.TYPE_LOAD_MORE_SUCCESS:
//                    adapter.setShowFooter(false);
                adapter.onLoadMoreSuccess();
                adapter.addTail(list);
                break;
            case InitDataType.TYPE_REFRESH_FAIL:
//                if (adapter.getItemCount() == 0) {
//                    //显示空布局
//                    adapter.onShowEmptyView(true, msg);
//                }
                if (adapter.getAdapterData() == null || adapter.getAdapterData().isEmpty()) {
                    adapter.onShowEmptyView(true, msg);
//                    adapter.notifyItemChanged();
                    adapter.notifyDataSetChanged();
                }
                break;
            case InitDataType.TYPE_LOAD_MORE_FAIL:
                adapter.onLoadMoreFail(msg);
                break;
        }
    }

    /**
     * 更新数据的处理
     */
    protected abstract void updateDealView();

    @Override
    public void onRefresh(RefreshView refreshView) {
        if (mPresenter != null) {
            if (!isFirstRefresh /*&& mSwipeRefreshLayout != null*/)
                noFirstRefresh(refreshView);
//                mSwipeRefreshLayout.setRefreshing(true);
            mPresenter.onRefresh();
        }
    }

    /**
     * 不是第一次刷新数据
     */
    protected abstract void noFirstRefresh(RefreshView refreshView);
}
