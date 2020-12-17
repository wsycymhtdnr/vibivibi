package com.lyf.vibi.ui.tab.common;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

/**
 * 单个Tab对外接口
 */
public interface ITab<D> extends ITabLayout.OnTabSelectedListener<D> {
    /**
     * 设置Tab数据
     */
    void setTabInfo(@NonNull D data);

    /**
     * 动态修改某个Tab的大小
     * @param height
     */
    void resetHeight(@Px int height);
}
