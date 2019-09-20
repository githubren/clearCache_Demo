package com.yfpj.lib.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.yfpj.lib.R;
import com.yfpj.lib.base.presenter.BasePresenter;
import com.yfpj.lib.base.view.BaseView;
import com.yfpj.lib.util.BaseUtils;
import com.yfpj.lib.util.MeasureUtil;

/**
 * Created by fire on 2017/9/19 0019.
 */

public abstract class BaseDialog<T extends BasePresenter> extends BaseDialogView<T>{

    @Override
    protected void initRootView(Bundle savedInstanceState) {
        ((FrameLayout) mRootView.findViewById(R.id.base_dialog_container))
                .addView(LayoutInflater.from(getActivity()).inflate(getContentViewLayId(), null));
    }

    @Override
    protected int getRootViewId() {
        return R.layout.base_dialog_bg;
    }

    /**
     * 获取ContentView
     */
    protected abstract int getContentViewLayId();

    protected String getEditTxt(EditText editText) {
        return BaseUtils.getEditTextString(editText);
    }

    @Override
    protected int[] getWH() {
        int[] wh = {(int) (MeasureUtil.getScreenSize(getActivity()).x * 0.8), getDialog().getWindow().getAttributes().height};
        return wh;
    }

    @Override
    protected boolean isNoTitle() {
        return true;
    }
}
