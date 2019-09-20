package com.yfpj.lib.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by fire on 2017/9/19 0019.
 */

public class BaseDialogFragment extends DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNoTitle()) {//自定义布局
            setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        }
    }

    protected void forword(Class<?> c) {
        forword(c, true);
    }

    protected void forword(Class<?> c, boolean dismiss) {
        startActivity(new Intent(getActivity(), c));
        if (dismiss)
            dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        int[] wh = getWH();
        if (null != wh && isNoTitle()) {
            getDialog().getWindow().setLayout(wh[0], wh[1]);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    protected int[] getWH() {
        return null;
    }

    protected boolean isNoTitle() {
        return false;
    }

    @Override
    public void dismiss() {
        if (null == getActivity()) {
            return;
        }
        super.dismiss();
    }

    public void showDialog(FragmentManager manager) {
        FragmentTransaction ft = manager.beginTransaction();
        //添加fragment
        ft.add(BaseDialogFragment.this, "dialog");
        ft.commitAllowingStateLoss();
    }
}
