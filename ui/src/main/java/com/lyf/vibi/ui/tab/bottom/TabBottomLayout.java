package com.lyf.vibi.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lyf.vibi.library.util.DisplayUtil;
import com.lyf.vibi.ui.R;
import com.lyf.vibi.ui.tab.common.ITabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TabBottomLayout extends FrameLayout implements ITabLayout<TabBottom, TabBottomInfo<?>> {
    private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";
    private List<OnTabSelectedListener<TabBottomInfo<?>>> tabSelectedListeners = new ArrayList<>();
    private TabBottomInfo<?> selectedInfo;
    private float bottomAlpha = 1f;
    private float tabBottomHeight = 50;
    private float bottomLineHeight = 0.5f;
    private String bottomLineColor = "#dfe0e1";
    private List<TabBottomInfo<?>> infoList;

    public TabBottomLayout(@NonNull Context context) {
        this(context, null);
    }

    public TabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabBottomLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public TabBottom findTab(@NonNull TabBottomInfo<?> info) {
        ViewGroup frameLayout = findViewWithTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < frameLayout.getChildCount(); i++) {
            View child = frameLayout.getChildAt(i);
            if (child instanceof TabBottom) {
                TabBottom tab = (TabBottom) child;
                if (tab.getHiTabInfo() == info) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedChangeListener(OnTabSelectedListener<TabBottomInfo<?>> listener) {
        tabSelectedListeners.add(listener);
    }

    @Override
    public void defaultSelected(@NonNull TabBottomInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    public void setTabAlpha(float alpha) {
        this.bottomAlpha = alpha;
    }

    public void setTabHeight(float tabHeight) {
        this.tabBottomHeight = tabHeight;
    }

    public void setBottomLineHeight(float bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
    }

    public void setBottomLineColor(String bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
    }

    @Override
    public void inflateInfo(@NonNull List<TabBottomInfo<?>> infoList) {
        if (infoList.isEmpty()) {
            return;
        }
        this.infoList = infoList;
        // 防止重复添加，移除之前已经添加的view
        for (int i = getChildCount() - 1; i > 0; i++) {
            removeViewAt(i);
        }
        selectedInfo = null;
        addBackground();
        // 清除之前添加的HiTabBottom listener，Tips：Java foreach remove问题
        Iterator<OnTabSelectedListener<TabBottomInfo<?>>> iterator = tabSelectedListeners.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof TabBottom) {
                iterator.remove();
            }
        }
        FrameLayout frameLayout = new FrameLayout(getContext());
        int width = DisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
        int height = DisplayUtil.dp2px(getContext(), tabBottomHeight);
        frameLayout.setTag(TAG_TAB_BOTTOM);
        for (int i = 0; i < infoList.size(); i++) {
            final TabBottomInfo<?> info = infoList.get(i);
            LayoutParams params = new LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM;
            params.leftMargin = i * width;
            TabBottom tabBottom = new TabBottom(getContext());
            tabSelectedListeners.add(tabBottom);
            tabBottom.setTabInfo(info);
            frameLayout.addView(tabBottom, params);
            tabBottom.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelected(info);
                }
            });
        }
        LayoutParams flPrams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        flPrams.gravity = Gravity.BOTTOM;
        addBottomLine();
        addView(frameLayout, flPrams);

        fixContentView();
    }

    private void addBottomLine() {
        View bottomLine = new View(getContext());
        bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor));

        LayoutParams bottomLineParams =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(bottomLineHeight, getResources()));
        bottomLineParams.gravity = Gravity.BOTTOM;
        bottomLineParams.bottomMargin = DisplayUtil.dp2px(tabBottomHeight - bottomLineHeight, getResources());
        addView(bottomLine, bottomLineParams);
        bottomLine.setAlpha(bottomAlpha);
    }

    private void onSelected(@NonNull TabBottomInfo<?> nextInfo) {
        for (OnTabSelectedListener<TabBottomInfo<?>> listener : tabSelectedListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;
    }

    private void addBackground() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_layout_bg, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(getContext(), tabBottomHeight));
        params.gravity = Gravity.BOTTOM;
        addView(view, params);
        view.setAlpha(bottomAlpha);
    }

    /**
     * 修复内容区域的底部Padding
     */
    private void fixContentView() {
        if (!(getChildAt(0) instanceof ViewGroup)) {
            return;
        }
        ViewGroup rootView = (ViewGroup) getChildAt(0);
        ViewGroup targetView = ViewUtil.findTypeView(rootView, RecyclerView.class);
        if (targetView == null) {
            targetView = ViewUtil.findTypeView(rootView, ScrollView.class);
        }
        if (targetView == null) {
            targetView = ViewUtil.findTypeView(rootView, AbsListView.class);
        }
        if (targetView != null) {
            targetView.setPadding(0, 0, 0, DisplayUtil.dp2px(tabBottomHeight, getResources()));
            targetView.setClipToPadding(false);
        }
    }
}
