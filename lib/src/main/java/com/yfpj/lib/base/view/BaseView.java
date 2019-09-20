package com.yfpj.lib.base.view;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Created by fire on 2017/9/18.
 */

public interface BaseView {

    void toast(@Nullable String msg);

    void toast(@StringRes int msg);

    void showProgress();

    void hideProgress();
}
