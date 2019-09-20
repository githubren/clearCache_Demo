package com.yfpj.lib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yfpj.lib.base.presenter.BasePresenter;
import com.yfpj.lib.base.view.BaseView;
import com.yfpj.lib.util.BaseUtils;
import com.yfpj.lib.util.MeasureUtil;
import com.yfpj.lib.waitdialog.WaitDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {
    protected View mRootView;
    private String TAG = BaseFragment.this.getClass().getSimpleName();

    private Unbinder unbinder;
    protected T mPresenter;

    protected void initPlaceView(View view) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = MeasureUtil.getStatusBarHeight(getActivity());
        view.requestLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) mPresenter.onResume();
    }

    @Override
    public void toast(@StringRes int msg) {
        BaseUtils.toastShort(getActivity(), msg);
    }

    @Override
    public void hideProgress() {
        WaitDialog.getInstance(getActivity()).dismissWaitDialog();
    }

    @Override
    public void showProgress() {
        WaitDialog.getInstance(getActivity()).showWaitDialog();
    }

    @Override
    public void toast(String msg) {
        if (getActivity() != null)
            BaseUtils.toastShort(getActivity(), msg);
    }

    protected void log(String msg) {
        BaseUtils.logh(TAG, msg);
    }

    protected String getEditText(EditText et) {
        return BaseUtils.getEditTextString(et);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        WaitDialog.destroyDialog(getActivity());
        if (mPresenter != null) mPresenter.onDestory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBinderBefore();
        if (unbinder != null)
            unbinder.unbind();
    }

    /**
     * B
     * 地图需要onDestroy
     */
    protected void unBinderBefore() {
    }

    /**
     * 设置recycle完全显示
     */
    protected void initRecycler(RecyclerView recycle, RecyclerView.LayoutManager manager) {
        manager.setAutoMeasureEnabled(true);
        recycle.setLayoutManager(manager);
        recycle.setHasFixedSize(true);
        recycle.setNestedScrollingEnabled(false);
    }

    protected void initTabAdapter(PagerAdapter adapter, ViewPager mViewPager, TabLayout mTabLayout) {
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final boolean rootNull = mRootView == null;
        if (rootNull) {
            mRootView = onCreateViewInit(inflater, container,
                    savedInstanceState);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        getArgs();
        if (rootNull) {
            initWhenRootViewNull(savedInstanceState);
        }
        return mRootView;
    }

    /**
     * 获取传递的参数
     */
    protected void getArgs() {
    }

    /**
     * 在这里初始化数据
     */
    protected void initWhenRootViewNull(Bundle savedInstanceState) {
    }

    /**
     * 刷新界面
     */
    protected void refreshUi() {

    }

    /**
     * 重写此函数来获取view
     */
    protected abstract View onCreateViewInit(LayoutInflater inflater,
                                             ViewGroup container, Bundle savedInstanceState);

    protected void forword(Class<? extends BaseActivity> c) {
        //页面跳转 intent连接两个activity
        startActivity(new Intent(getActivity(), c));
    }

}
