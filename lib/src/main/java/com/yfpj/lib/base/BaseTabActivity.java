package com.yfpj.lib.base;

import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.yfpj.lib.BaseActivity;
import com.yfpj.lib.base.presenter.BasePresenter;

/**
 * Created by fire on 2017/9/19 0019.
 */

public abstract class BaseTabActivity<T extends BasePresenter> extends BaseActivity<T> {
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected int currentPage = 0;

    /**
     * 控制tablayout指示器的长度
     */
    public static void reduceMarginsInTabs(TabLayout tabLayout, int marginOffset) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            for (int i = 0; i < ((ViewGroup) tabStrip).getChildCount(); i++) {
                View tabView = tabStripGroup.getChildAt(i);
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    layoutParams.leftMargin = marginOffset;
                    layoutParams.rightMargin = marginOffset;
                }
            }
            tabLayout.requestLayout();
        }
    }

    protected void initLayoutView(@IdRes int tabResId, @IdRes int vpResId) {
        tabLayout = findViewById(tabResId);
        viewPager = findViewById(vpResId);
        viewPager.setAdapter(getPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        setTabModeAndGravity(tabLayout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        reduceMarginsInTabs(tabLayout, 150);
    }

    protected void setTabModeAndGravity(TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//不设置gravity没有效果
    }

    protected abstract PagerAdapter getPagerAdapter();
}
