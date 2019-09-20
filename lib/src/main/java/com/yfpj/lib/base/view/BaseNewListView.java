package com.yfpj.lib.base.view;

import com.yfpj.lib.refreshlayout.type.InitDataType;

import java.util.List;

/**
 * Created by fire on 2017/10/11 0011.
 */

public interface BaseNewListView<T,V> extends BaseView {

    /**
     * 这个方法在presenter中用来刷新ui
     *
     * @param data       数据
     * @param msg        提示信息
     * @param type       更新类型
     */
    void dealListView(T data, String msg, @InitDataType.InitDataTypeChecker int type);
    /**
     * 这个方法在presenter中用来刷新ui
     *
     * @param list       数据
     * @param msg        提示信息
     * @param type       更新类型
     * @param mustIsNull 是否必须为空
     */
    void updateRecycleView(List<V> list, String msg, @InitDataType.InitDataTypeChecker int type, boolean mustIsNull);

    /**
     * 这个方法在presenter中用来刷新ui
     *
     * @param list       数据
     * @param msg        提示信息
     * @param type       更新类型
     */
    void updateRecycleView(List<V> list, String msg, @InitDataType.InitDataTypeChecker int type);

}
