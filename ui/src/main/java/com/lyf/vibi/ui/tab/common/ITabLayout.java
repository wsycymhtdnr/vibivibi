package com.lyf.vibi.ui.tab.common;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * 导航容器接口
 * @param <Tab>Tab视图
 * @param <D>数据
 */
public interface ITabLayout<Tab extends ViewGroup, D> {
    /**
     * 根据数据找到对应的Tab
     */
    Tab findTab(@NonNull D data);

    /**
     * 添加Tab变化的监听器
     */
    void addTabSelectedChangeListener(OnTabSelectedListener<D> listener);

    /**
     * 默认选中Tab
     */
    void defaultSelected(@NonNull D defaultInfo);

    /**
     * 初始化数据
     */
    void inflateInfo(@NonNull List<D> infoList);

    interface OnTabSelectedListener<D> {
        /**
         * @param index tab索引
         * @param prevInfo 上一个选中Tab对应的数据
         * @param nextInfo 下一个选中Tab对应的数据
         */
        void onTabSelectedChange(int index, @Nullable D prevInfo, @NonNull D nextInfo);
    }
}
