package com.yfpj.lib.refreshlayout.divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by fire on 2017/8/11  10:37.
 * 四周边距相等
 */

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;

    public DividerGridItemDecoration(Context context, int drawableId) {
        if (0 != drawableId) {
            mDivider = ContextCompat.getDrawable(context, drawableId);
        } else {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        //int childCount = parent.getAdapter().getItemCount();
        int itemPosition = parent.getChildLayoutPosition(view);
        if (isFirstRow(itemPosition, spanCount)) {
            //如果是第一行，绘制top和bottom  Offset,
            if (isFirstColumn(itemPosition, spanCount)) {
                //如果是第一列，padding、padding/2
                outRect.set(0, mDivider.getIntrinsicHeight(),
                        mDivider.getIntrinsicWidth() / 2, mDivider.getIntrinsicHeight());
            } else if (isLastColumn(itemPosition, spanCount)) {
                //如果是最后一列，padding/2、padding
                outRect.set(mDivider.getIntrinsicWidth() / 2, mDivider.getIntrinsicHeight(),
                        0, mDivider.getIntrinsicHeight());
            } else {
                //padding/2、padding/2
                outRect.set(mDivider.getIntrinsicWidth() / 2, mDivider.getIntrinsicHeight(),
                        mDivider.getIntrinsicWidth() / 2, mDivider.getIntrinsicHeight());
            }

        } else {
            //仅仅绘制bottom  Offset
            if (isFirstColumn(itemPosition, spanCount)) {
                //如果是第一列，padding、padding/2
                outRect.set(0, 0,
                        mDivider.getIntrinsicWidth() / 2, mDivider.getIntrinsicHeight());
            } else if (isLastColumn(itemPosition, spanCount)) {
                //如果是最后一列，padding/2、padding
                outRect.set(mDivider.getIntrinsicWidth() / 2, 0,
                        0, mDivider.getIntrinsicHeight());
            } else {
                //padding/2、padding/2
                outRect.set(mDivider.getIntrinsicWidth() / 2, 0,
                        mDivider.getIntrinsicWidth() / 2, mDivider.getIntrinsicHeight());
            }
        }
    }


    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int spanCount = getSpanCount(parent);
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            if (isFirstColumn(i, spanCount)) {
                left = left - mDivider.getIntrinsicWidth();
            }
            int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();

            if (isFirstRow(i, spanCount)) {
                int bottom = child.getTop() - params.bottomMargin;
                int top = bottom - mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
            int top2 = child.getBottom() + params.bottomMargin;
            int bottom2 = top2 + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top2, right, bottom2);
            mDivider.draw(c);

        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        int spanCount = getSpanCount(parent);
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            if (isFirstColumn(i, spanCount)) {
                int left2 = child.getRight() + params.rightMargin;
                int right2 = left2 + mDivider.getIntrinsicWidth() / 2;
                mDivider.setBounds(left2, top, right2, bottom);
                mDivider.draw(c);
                int right = child.getLeft() - params.leftMargin;
                int left = right - 20;
                mDivider.setBounds(left, top, right, bottom);
            } else if (isLastColumn(i, spanCount)) {
                final int right = child.getLeft() - params.leftMargin;
                final int left = right - mDivider.getIntrinsicWidth() / 2;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
                final int left2 = child.getRight() + params.rightMargin;
                final int right2 = left2 + mDivider.getIntrinsicWidth();
                mDivider.setBounds(left2, top, right2, bottom);
            } else {
                final int right = child.getLeft() - params.leftMargin;
                final int left = right - mDivider.getIntrinsicWidth() / 2;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
                final int left2 = child.getRight() + params.rightMargin;
                final int right2 = left2 + mDivider.getIntrinsicWidth() / 2;
                mDivider.setBounds(left2, top, right2, bottom);
            }
            mDivider.draw(c);
        }
    }

    private boolean isFirstRow(int itemPosition, int spanCount) {
        return (itemPosition < spanCount);
    }

    private boolean isFirstColumn(int itemPosition, int spanCount) {
        return ((itemPosition) % spanCount == 0);
    }

    private boolean isLastColumn(int itemPosition, int spanCount) {
        return ((itemPosition + 1) % spanCount == 0);
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }
}
