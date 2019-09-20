package com.yfpj.lib.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yfpj.lib.base.presenter.BasePresenter;
import com.yfpj.lib.base.view.BaseView;
import com.yfpj.lib.util.BaseUtils;
import com.yfpj.lib.waitdialog.WaitDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by fire on 2017/9/19 0019.
 */

public abstract class BaseDialogView<T extends BasePresenter> extends BaseDialogFragment implements BaseView {

    protected View mRootView;
    private Unbinder unbinder;
    protected T mPresenter;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected void addToCompositeDis(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final boolean isNull = mRootView == null;
        if (mRootView == null) {
            mRootView = LayoutInflater.from(getActivity()).inflate(getRootViewId(), container, false);
            initRootView(savedInstanceState);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        getArgs();
        if (isNull) initViewData();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPresenter!=null)
            mPresenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null)
            mPresenter.onDestory();
    }

    protected void initRootView(Bundle savedInstanceState) {
    }

    protected abstract int getRootViewId();

    protected void toastShort(String msg) {
        BaseUtils.toastShort(getActivity(), msg);
    }

    protected void toastShort(@StringRes int resId) {
        BaseUtils.toastShort(getActivity(), resId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mRootView) {
            ViewGroup viewGroup = (ViewGroup) mRootView.getParent();
            if (null != viewGroup) {
                viewGroup.removeView(mRootView);
            }
        }
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        WaitDialog.destroyDialog(getActivity());
        if (unbinder != null)
            unbinder.unbind();
    }


    protected void showLoadDialog() {
        WaitDialog.getInstance(getActivity()).showWaitDialog();
    }

    protected void dismissLoadDialog() {
        WaitDialog.getInstance(getActivity()).dismissWaitDialog();
    }


    protected void getArgs() {
    }

    /**
     * 首次执行调用。在获取了控件的id后
     */
    protected void initViewData() {
    }


    @Override
    public void toast(@Nullable String msg) {
        toastShort(msg);
    }

    @Override
    public void toast(@StringRes int msg) {
        toastShort(msg);
    }

    @Override
    public void showProgress() {
        showLoadDialog();
    }

    @Override
    public void hideProgress() {
        dismissLoadDialog();
    }
}
