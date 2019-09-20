package com.yfpj.lib.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.yfpj.lib.util.BaseUtils;
import com.yfpj.lib.util.MeasureUtil;

/**
 * Created by fire on 2017/9/19 0019.
 * 点击外部不消失
 */

public abstract class BaseNoBackgDialog extends BaseDialogView {

    protected String getEditTxt(EditText editText) {
        return BaseUtils.getEditTextString(editText);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
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
