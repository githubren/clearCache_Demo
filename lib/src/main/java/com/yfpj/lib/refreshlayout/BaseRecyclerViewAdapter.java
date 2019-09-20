package com.yfpj.lib.refreshlayout;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yfpj.lib.R;
import com.yfpj.lib.refreshlayout.callback.OnEmptyClickListener;
import com.yfpj.lib.refreshlayout.callback.OnItemClickListener;
import com.yfpj.lib.refreshlayout.callback.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * recyclerview适配器基类  定义一些基本的方法
 * 不同布局中的recyclerview继承这个基类 实现具体的功能
 * @param <T>
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected static final int TYPE_DEFAULT = 0;//默认布局
    protected static final int TYPE_EMPTY = TYPE_DEFAULT + 1;//空布局
    protected static final int TYPE_LOAD_MORE_SUCCESS = TYPE_EMPTY + 1;//加载更多成功，加载数据
    protected static final int TYPE_LOAD_MORE_FAIL = TYPE_LOAD_MORE_SUCCESS + 1;//加载更多失败，底部显示布局
    protected static final int TYPE_INIT = TYPE_LOAD_MORE_FAIL + 1;//数据初始化，显示加载中布局。
    protected static final int TYPE_HEADER = TYPE_INIT + 1;//添加头部view

    private String mRetryHintMsg;//重试，提示语
    private String mEmptyHintMsg;//空布局，提示语

    protected List<T> mData;
    protected Context mContext;
    protected LayoutInflater mInflater;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean isShowFooter;//显示底部
    private boolean mLoadMoreEnable;//加载更多。底部数据显示控制器。若为true：加载更多成功，显示数据。若为false，加载更多失败，可能显示加载重试。
    private boolean isShowEmptyView;//Recycler无数据，显示空布局
    private boolean isInitData;//刚开始加载数据，显示初始化布局
    private OnLoadMoreListener mOnLoadMoreListener;//加载更多监听器
    private boolean autoLoadMoreController = true;//自动加载更多控制器

    public void setAutoLoadMoreController(boolean autoLoadMoreController) {
        this.autoLoadMoreController = autoLoadMoreController;
    }

//    public BaseRecyclerViewAdapter(Context context, List<T> data) {
//        mContext = context;
//        mData = data == null ? new ArrayList<T>() : data;
//        mInflater = LayoutInflater.from(context);
//        mLayoutManager = new LinearLayoutManager(mContext);
//        this(context, data, new LinearLayoutManager(context));
//    }

    /**
     * 构造方法  传递参数
     * @param context 上下文  决定recyclerview生命周期
     * @param data  item中将要展示的数据
     * @param mLayoutManager 布局
     */
    public BaseRecyclerViewAdapter(Context context, List<T> data, RecyclerView.LayoutManager mLayoutManager) {
        mContext = context;
        mData = data == null ? new ArrayList<T>() : data;
        mInflater = LayoutInflater.from(context);
        this.mLayoutManager = mLayoutManager;
    }

    /**
     * 加载recyclerview布局
     * @param parent
     * @param viewType  recyclerview外层用StateFrameLayout（状态帧布局） 不同的viewtype加载不同的布局
     * @return 返回一个ViewHolder对象 在ViewHolder类中 根据返回的这个对象找到item控件
     */
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder holder = null;
        if (viewType == TYPE_LOAD_MORE_FAIL) {//加载更多失败
            //加载布局 创建ViewHolder对象
            holder = new BaseRecyclerViewHolder(mContext, mInflater.inflate(R.layout.footer_load_more_fail, parent, false));
            //加载重试控件并设置点击监听
            holder.getTextView(R.id.footer_retry).setOnClickListener(v -> {
                if (mOnLoadMoreListener == null) return;
                //true 加载更多成功 显示数据
                mLoadMoreEnable = true;
                isShowFooter = true;
                //刷新数据
                notifyItemChanged(getItemCount() - 1);
                v.post(() -> mOnLoadMoreListener.onLoadMore());
            });
        } else if (viewType == TYPE_INIT) {//初始化布局
            //加载初始化布局  创建对象
            holder = new BaseRecyclerViewHolder(mContext, mInflater.inflate(R.layout.init_recycler_data_layout, parent, false));
//            holder = new BaseRecyclerViewHolder(mContext, mInflater.inflate(R.layout.include_empty_layout, parent, false));
        } else if (viewType == TYPE_EMPTY) {//空布局
            holder = new BaseRecyclerViewHolder(mContext, mInflater.inflate(bindEmptyViewLayout(), parent, false));
        } else if (viewType == TYPE_LOAD_MORE_SUCCESS) {//加载更多成功
            holder = new BaseRecyclerViewHolder(mContext, mInflater.inflate(R.layout.footer_load_more, parent, false));
        } else if (viewType == TYPE_HEADER) {//头部
            holder = new BaseRecyclerViewHolder(mContext, mInflater.inflate(bindHeaderLayoutResId(), parent, false));
        } else {//recyclerview的布局
            holder = new BaseRecyclerViewHolder(mContext, mInflater.inflate(bindItemViewLayout(viewType), parent, false));
            onHolderItemHeight(holder.itemView);
        }

        holder.setOnItemClickListener(onItemClickListener);
        return holder;
    }


    protected void onHolderItemHeight(View view) {

    }

    public void setOnEmptyClickListener(OnEmptyClickListener onEmptyClickListener) {
        this.onEmptyClickListener = onEmptyClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;
    private OnEmptyClickListener onEmptyClickListener;

    public List<T> getAdapterData() {
        return mData;
    }


    /**
     * 空布局自定义
     * {@Link #bindEmptyViewData()}
     */
    protected int bindEmptyViewLayout() {
        return R.layout.include_empty_layout;
    }

    /**
     * 自定义空布局必须重写這个方法且不能调用super
     * {@Link #bindEmptyViewLayout()}
     */
    protected void bindEmptyViewData(BaseRecyclerViewHolder holder, int position, String mEmptyHintMsg) {
//        holder.setText(R.id.tv_error, mEmptyHintMsg);
        if(hidePullHint()){//隐藏下拉刷新屏幕提示
            holder.getTextView(R.id.tv_pull_refresh_hint).setVisibility(View.GONE);
        }
        holder.getTextView(R.id.tv_empty_refresh)//点击刷新屏幕
                .setOnClickListener(
                        v -> {
                            if (onEmptyClickListener != null)
                                //监听调用接口中的方法  具体的子类中重写点击事件方法
                                onEmptyClickListener.onEmptyClick(v);
                        }
                        /*new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onEmptyClickListener != null)
                            onEmptyClickListener.onEmptyClick(v);
                    }
                }*/
                );
    }

    protected boolean hidePullHint() {
        return false;
    }

    /**
     * 该方法实现布局中数据显示
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {//头部
            fullSpan(holder, TYPE_HEADER);
            bindHeaderViewData(holder, position);
        } else if (getItemViewType(position) == TYPE_EMPTY) {//空布局
            fullSpan(holder, TYPE_EMPTY);
            bindEmptyViewData(holder, position, mEmptyHintMsg);
        } else if (getItemViewType(position) == TYPE_INIT) {//初始化
            fullSpan(holder, TYPE_INIT);
        } else if (getItemViewType(position) == TYPE_LOAD_MORE_SUCCESS) {//加载更多成功
            fullSpan(holder, TYPE_LOAD_MORE_SUCCESS);
        } else if (getItemViewType(position) == TYPE_LOAD_MORE_FAIL) {//加载更多失败
            fullSpan(holder, TYPE_LOAD_MORE_FAIL);
            holder.setText(R.id.footer_retry_msg, mRetryHintMsg);
        } else {
            if (getHeaderCount() != 0)
                position -= getHeaderCount();
            bindItemViewData(holder, position, mData.get(position));
        }
        //数据滑动底部，是否自动加载
        //autoLoadMoreController true 是自动加载
        //isInitData             false 不是初始化布局
        //isShowEmptyView        false 不是显示空布局
        //mOnLoadMoreListener    !null 加载更多事件不为空
        //mLoadMoreEnable        true  加载更多可用
        //isShowFooter           false 不是显示底部加载中布局
        // 是最后一条数据
        //以上条件必须全部满足，才会在滑动到底部，自动加载数据。
        if (autoLoadMoreController && !isInitData && !isShowEmptyView
                && mOnLoadMoreListener != null && mLoadMoreEnable && !isShowFooter
                && position == getItemCount() - getHeaderCount() - 1) {
            isShowFooter = true;
            holder.itemView.post(() -> {
                notifyItemInserted(getItemCount());
                mOnLoadMoreListener.onLoadMore();
            });
        }

    }

    private void fullSpan(BaseRecyclerViewHolder holder, final int type) {
        if (mLayoutManager != null) {
            if (mLayoutManager instanceof StaggeredGridLayoutManager) {//瀑布流
                if (((StaggeredGridLayoutManager) mLayoutManager).getSpanCount() != 1) {//一行的item列数超过1个的时候
                    //拿到item布局参数
                    StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                    //将item填充满
                    params.setFullSpan(true);
                }
            } else if (mLayoutManager instanceof GridLayoutManager) {//网格布局
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) mLayoutManager;
                final GridLayoutManager.SpanSizeLookup oldSizeLookup = gridLayoutManager.getSpanSizeLookup();
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (getItemViewType(position) == type) {
                            return gridLayoutManager.getSpanCount();
                        }
                        if (oldSizeLookup != null) {
                            return oldSizeLookup.getSpanSize(position);
                        }
                        return 1;
                    }
                });
            }
        }
    }

    /**
     * 布局文件资源
     */
    public abstract int bindItemViewLayout(int viewType);

    public abstract int getEveryItemViewType(int position);

    /**
     * 加载每一个item的数据
     */
    public abstract void bindItemViewData(BaseRecyclerViewHolder holder, int position, T itemData);

    @Override
    public int getItemCount() {//1、底部显示加载中布局，enable为true  2、加载中未显示，enable为false，显示加载失败
        int hasFooter = mOnLoadMoreListener == null ? 0 : isShowFooter && mLoadMoreEnable || !isShowFooter && !mLoadMoreEnable ? 1 : 0;
        int itemCount = isShowEmptyView || isInitData ? 1 : mData == null ? 0 : mData.size() + hasFooter;
        return itemCount + getHeaderCount();
    }


    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderCount()) {
            return TYPE_HEADER;
        } else if (isInitData) {//是初始化
            return TYPE_INIT;
        } else if (isShowEmptyView) {//显示空布局
            return TYPE_EMPTY;
            //加载更多事件不为null，且加载更多可用，且可以显示最后底部item，且是最后一条数据。
        } else if (mOnLoadMoreListener != null && mLoadMoreEnable && isShowFooter && getItemCount() - 1 == position) {
            return TYPE_LOAD_MORE_SUCCESS;
            //加载更多事件不为空，可以加载更多。若加载更多不可用，可能是数据加载失败，若显示底部为false，且是最后一条，那么底部显示加载失败控件。
        } else if (mOnLoadMoreListener != null && !mLoadMoreEnable && !isShowFooter && getItemCount() - 1 == position) {
            return TYPE_LOAD_MORE_FAIL;
        }
//        return TYPE_DEFAULT;
        return getEveryItemViewType(position);
    }


    //========================================设置头部，需重写的方法================================================
    public int getHeaderCount() {
        return 0;
    }

    protected void bindHeaderViewData(BaseRecyclerViewHolder holder, int position) {
    }

    protected int bindHeaderLayoutResId() {
        return 0;
    }

//========================================数据更新==============================================================

    /**
     * 下拉刷新，更新数据
     */
    public void update(List<T> data) {
        if (data == null || data.isEmpty()) return;
        mData.clear();
        mData = data;
        notifyDataSetChanged();
    }

    public void updateNotClear(List<T> data) {
        if (data == null || data.isEmpty()) {
            //加载空布局
            updateNull();
            return;
        }
        mData = data;
        notifyDataSetChanged();
    }


    /**
     * 搜索刷新
     */
    public void searchUpdate(List<T> data) {
        if (data == null || data.isEmpty()) {
            updateNull();
            return;
        }
        mData = data;
        notifyDataSetChanged();
    }

    /**
     * 加载空布局
     */
    public void updateNull() {
        //清空数据
        mData.clear();
        //数据改变 刷新
        notifyDataSetChanged();
    }

    /**
     * 上拉加载，添加数据
     */
    public void addTail(List<T> data) {
        if (data == null || data.isEmpty()) return;
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        mLoadMoreEnable = true;
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    /**
     * 加载更多成功
     */
    public void onLoadMoreSuccess() {
        mLoadMoreEnable = true;
        isShowFooter = false;
        notifyItemRemoved(getItemCount());
    }

    /**
     * 加载更多失败
     *
     * @param msg 底部提示语句。
     */
    public void onLoadMoreFail(String msg) {
        mLoadMoreEnable = false;
        isShowFooter = false;
        mRetryHintMsg = msg;
        notifyItemChanged(getItemCount() - 1);
    }

    /**
     * 显示空布局
     *
     * @param showEmptyView 是否显示空布局
     * @param msg           空布局中的提示语
     */
    public void onShowEmptyView(boolean showEmptyView, String msg) {
        isShowEmptyView = showEmptyView;
        mEmptyHintMsg = msg;
    }

    /**
     * 初始化数据。用于显示加载界面
     */
    public void onShowInitView(boolean showInitView) {
        isInitData = showInitView;
    }

    /**
     * 点击空布局按钮刷新界面，显示加载界面
     */
    public void showInitView() {
        onShowInitView(true);
        notifyItemChanged(getItemCount() - 1);
    }

}
