package com.lyf.vibi.ui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.lyf.vibi.library.util.DisplayUtil;
import com.lyf.vibi.ui.tab.common.ITabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TabTopLayout extends HorizontalScrollView implements ITabLayout<TabTop, TabTopInfo<?>> {
    private List<OnTabSelectedListener<TabTopInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private TabTopInfo<?> selectedInfo;
    private List<TabTopInfo<?>> infoList;
    private int tabWith;

    public TabTopLayout(Context context) {
        this(context, null);
    }

    public TabTopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public TabTop findTab(@NonNull TabTopInfo<?> info) {
        ViewGroup ll = getRootLayout(false);
        for (int i = 0; i < ll.getChildCount(); i++) {
            View child = ll.getChildAt(i);
            if (child instanceof TabTop) {
                TabTop tab = (TabTop) child;
                if (tab.getHiTabInfo() == info) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<TabTopInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull TabTopInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<TabTopInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        LinearLayout linearLayout = getRootLayout(true);
        selectedInfo = null;
        // 清除之前添加的HiTabBottom listener，Tips：Java foreach remove问题
        Iterator<OnTabSelectedListener<TabTopInfo<?>>> iterator = tabSelectedChangeListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof TabTop) {
                iterator.remove();
            }
        }
        for (int i = 0; i < infoList.size(); i++) {
            final TabTopInfo<?> info = infoList.get(i);
            TabTop tab = new TabTop(getContext());
            tabSelectedChangeListeners.add(tab);
            tab.setTabInfo(info);
            linearLayout.addView(tab);
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(info);
                }
            });
        }
    }

    private void onSelected(@NonNull TabTopInfo nextInfo) {
        for (OnTabSelectedListener<TabTopInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
        autoScroll(nextInfo);
    }

    /**
     * 自动滚动，实现点击的位置能够自动滚动以展示前后2个
     *
     * @param nextInfo 点击tab的info
     */
    private void autoScroll(TabTopInfo nextInfo) {
        TabTop tabTop = findTab(nextInfo);
        if (tabTop == null) return;
        int index = infoList.indexOf(nextInfo);
        int[] loc = new int[2];
        //获取点击的控件在屏幕的位置
        tabTop.getLocationInWindow(loc);
        int scrollWidth;
        if (tabWith == 0) {
            tabWith = tabTop.getWidth();
        }
        //判断点击了屏幕左侧还是右侧
        if ((loc[0] + tabWith / 2) > DisplayUtil.getDisplayWidthInPx(getContext()) / 2) {
            scrollWidth = rangeScrollWidth(index, 2);
        } else {
            scrollWidth = rangeScrollWidth(index, -2);
        }
        scrollTo(getScrollX() + scrollWidth, 0);
    }

    private LinearLayout getRootLayout(boolean clear) {
        LinearLayout rootView = (LinearLayout) getChildAt(0);
        if (rootView == null) {
            rootView = new LinearLayout(getContext());
            rootView.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(rootView, layoutParams);
        } else if (clear) {
            rootView.removeAllViews();
        }
        return rootView;
    }

    /**
     * 获取可滚动的范围
     *
     * @param index 从第几个开始
     * @param range 向前向后的范围
     * @return 可滚动的范围
     */
    private int rangeScrollWidth(int index, int range) {
        int scrollWidth = 0;
        for (int i = 0; i <= Math.abs(range); i++) {
            int next;
            if (range < 0) {
                next = range + i + index;
            } else {
                next = range - i + index;
            }
            if (next >= 0 && next < infoList.size()) {
                if (range < 0) {
                    scrollWidth -= scrollWidth(next, false);
                } else {
                    scrollWidth += scrollWidth(next, true);
                }
            }
        }
        return scrollWidth;

    }

    /**
     * 指定位置的控件可滚动的距离
     *
     * @param index   指定位置的控件
     * @param toRight 是否是点击了屏幕右侧
     * @return 可滚动的距离
     */
    private int scrollWidth(int index, boolean toRight) {
        TabTop target = findTab(infoList.get(index));
        if (target == null) return 0;
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        if (toRight) {//点击屏幕右侧
            if (rect.right > tabWith) {//right坐标大于控件的宽度时，说明完全没有显示
                return tabWith;
            } else {//显示部分，减去已显示的宽度
                return tabWith - rect.right;
            }
        } else {
            if (rect.left <= -tabWith) {//left坐标小于等于-控件的宽度，说明完全没有显示
                return tabWith;
            } else if (rect.left > 0) {//显示部分
                return rect.left;
            }
            return 0;
        }
    }
}
