package com.yfpj.lib.base;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.yfpj.lib.BaseFragment;
import com.yfpj.lib.base.presenter.BasePresenter;

/**
 * Created by fire on 2017/9/21 0021.
 */

public abstract class BaseTabFragment<T extends BasePresenter> extends BaseFragment<T> {

    protected void initTabAdapter(ViewPager mViewPager, TabLayout mTabLayout) {
        mViewPager.setAdapter(getPagerAdapter());
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }


    protected abstract PagerAdapter getPagerAdapter();
}
