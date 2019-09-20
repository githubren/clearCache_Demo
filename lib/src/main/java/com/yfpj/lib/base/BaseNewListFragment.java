package com.yfpj.lib.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yfpj.lib.BaseFragment;
import com.yfpj.lib.R;
import com.yfpj.lib.base.presenter.BaseNewListPresenter;
import com.yfpj.lib.base.view.BaseNewListView;
import com.yfpj.lib.refreshlayout.BaseRecyclerViewAdapter;
import com.yfpj.lib.refreshlayout.OnRefreshListener;
import com.yfpj.lib.refreshlayout.RefreshView;
import com.yfpj.lib.refreshlayout.type.InitDataType;
import com.yfpj.lib.util.SwipeInit;

import java.util.List;

/**
 * Created by fire on 2017/10/12 0012.
 * V 服务器返回的数据类型
 * K list item 的数据类型
 */
public abstract class BaseNewListFragment<T extends BaseNewListPresenter, V, K> extends BaseFragment<T>
        implements BaseNewListView<V, K>/*, SwipeRefreshLayout.OnRefreshListener*/, OnRefreshListener {

    //    @BindView(R2.id.recycler_view)
    protected RecyclerView mRecyclerView;
    //    @BindView(R2.id.swipe_refresh_layout)
//    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected BaseRecyclerViewAdapter<K> adapter;
    private boolean isFirstRefresh = true;

    @Override
    public void updateRecycleView(List<K> list, String msg, @InitDataType.InitDataTypeChecker int type) {
        updateRecycleView(list, msg, type, false);
    }

    /**
     * 请求数据达到了默认的请求条目数，
     * 设置是否自动刷新
     * 达到了设置成true
     * 没有达到设置成false
     */
    protected void autoLoadMore(boolean autoLoadMoreController) {
        if (adapter == null) return;
        adapter.setAutoLoadMoreController(autoLoadMoreController);
    }

    @Override
    public void updateRecycleView(List<K> list, String msg,
                                  @InitDataType.InitDataTypeChecker int type, boolean mustIsNull) {
//        if (mSwipeRefreshLayout != null)
//            mSwipeRefreshLayout.setRefreshing(false);
        updateDealView();
        isFirstRefresh = false;
        adapter.onShowInitView(false);
        adapter.onShowEmptyView(false, msg);
        switch (type) {
            case InitDataType.TYPE_REFRESH_SUCCESS://刷新成功
                if (mustIsNull && (list == null || list.isEmpty())) {//请求到的数据为空
                    //加载空布局 清空数据 更新
                    adapter.updateNull();
                    //显示空布局 提示语
                    adapter.onShowEmptyView(true, msg);
                } else {
                    //否则加载请求到的数据   清空原有的数据 更新新数据
                    adapter.update(list);
                }
                break;
            case InitDataType.TYPE_LOAD_MORE_SUCCESS://加载更多成功
//                    adapter.setShowFooter(false);
                //清除item
                adapter.onLoadMoreSuccess();
                //添加data
                adapter.addTail(list);
                break;
            case InitDataType.TYPE_REFRESH_FAIL://刷新失败
//                if (adapter.getItemCount() == 0) {
//                    //显示空布局
//                    adapter.onShowEmptyView(true, msg);
//                }
                if (adapter.getAdapterData() == null || adapter.getAdapterData().isEmpty()) {
                    //数据为空时 加载空布局
                    adapter.onShowEmptyView(true, msg);
//                    adapter.notifyItemChanged();
                    adapter.notifyDataSetChanged();
                }
                break;
            case InitDataType.TYPE_LOAD_MORE_FAIL://加载更多失败
                //itemCount-1 底部放置失败重试提示语
                adapter.onLoadMoreFail(msg);
                break;
        }
    }


    /**
     * 用于创建自己的fragment布局
     *
     * @return
     */
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    /**
     * 更新数据的处理
     */
    protected abstract void updateDealView();


    /**
     * 不是第一次刷新数据
     */
    protected abstract void noFirstRefresh(RefreshView refreshView);

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, container, savedInstanceState);
        if (view == null) {
            throw new RuntimeException("base new list fragment view is null");
//            view = inflater.inflate(R.layout.frag_record_base, container, false);
//            mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
//            mRecyclerView = view.findViewById(R.id.recycler_view);
        }
//        binder = ButterKnife.bind(this, view);
//        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.setOnRefreshListener(this);
//            SwipeInit.init(mSwipeRefreshLayout);
//        }
//        initPullViewAndRecycler();
        if (adapter == null) {
            initNewsList();
        }
        adapter.onShowInitView(true);
        if (autoAsyn())
            onRefresh(null);
        return view;
    }

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

        //recyclerview中item的点击事件
        adapter.setOnItemClickListener((View view, int position) -> {
            BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) mRecyclerView.getAdapter();
            List data = adapter.getAdapterData();
            //item的实际position要减去头部
            int realPosition = position - adapter.getHeaderCount();
            if (data != null && !data.isEmpty() && realPosition < data.size() && realPosition >= 0) {
                setListener(data, realPosition);
            }
        });

        adapter.setOnEmptyClickListener(view -> {
            if (mPresenter != null) {
                //点击空布局按钮刷新界面，显示加载界面
                adapter.showInitView();
                //延迟500毫秒加载空布局
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
        //对recyclerview的配置
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(getLayoutManager());
        //初始化item装饰
        initRecyclerViewItemDecoration(mRecyclerView);
//        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(adapter);

    }

    protected boolean autoAsyn() {
        return true;
    }

    @Override
    public void onRefresh(RefreshView refreshView) {
        if (mPresenter != null) {
            if (!isFirstRefresh /*&& mSwipeRefreshLayout != null*/)
//                mSwipeRefreshLayout.setRefreshing(true);
                noFirstRefresh(refreshView);
            mPresenter.onRefresh();
    }
    }
}
