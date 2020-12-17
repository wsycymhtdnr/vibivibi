package com.lyf.vibi.common.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 将Fragment的操作内聚
 * 提供通用的一些API
 */
public class FragmentTabView extends FrameLayout {
    private TabViewAdapter mAdapter;
    private int currentPosition;

    public FragmentTabView(@NonNull Context context) {
        super(context);
    }

    public FragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentTabView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TabViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(TabViewAdapter adapter) {
        if (this.mAdapter != null || adapter == null) {
            return;
        }
        this.mAdapter = adapter;
        currentPosition = -1;
    }

    public void setCurrentItem(int position) {
        if (position < 0 || position >= mAdapter.getCount()) {
            return;
        }
        if (currentPosition != position) {
            currentPosition = position;
            mAdapter.instantiateItem(this, position);
        }
    }

    public int getCurrentItem() {
        return currentPosition;
    }

    public Fragment getCurrentFragment() {
        if (this.mAdapter == null) {
            throw new IllegalArgumentException("please call setAdapter first.");
        }
        return mAdapter.getCurFragment();
    }
}
