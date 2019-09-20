package com.yfpj.lib.util;

import android.support.v4.widget.SwipeRefreshLayout;

import com.yfpj.lib.R;

/**
 * Created by fire on 2017/9/18 0018.
 */

public class SwipeInit {
    public static void init(SwipeRefreshLayout mSwipeRefreshLayout) {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.color_notice_empty, android.R.color.holo_blue_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }
}
