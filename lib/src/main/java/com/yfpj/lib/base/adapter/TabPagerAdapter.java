package com.yfpj.lib.base.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by fire on 2017/9/19 0019.
 */

public abstract class TabPagerAdapter extends FragmentPagerAdapter {
    private List<String> titles;

    public TabPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = Arrays.asList(titles);
    }


    public TabPagerAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        this.titles = titles;
    }


    @Override
    public int getCount() {
        return titles.size();
    }

    //重写此方法  不然tab不会显示标题
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}
