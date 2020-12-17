package com.lyf.vibi.ui.refresh;

public interface Refresh {
    /**
     * 刷新时是否禁止滚动
     *
     * @param disableRefreshScroll 否禁止滚动
     */
    void setDisableRefreshScroll(boolean disableRefreshScroll);

    /**
     * 刷新完成
     */
    void refreshFinished();

    /**
     * 设置下拉刷新的监听器
     *
     * @param hiRefreshListener 刷新的监听器
     */
    void setRefreshListener(HiRefreshListener hiRefreshListener);

    /**
     * 设置下拉刷新的视图
     *
     * @param overView 下拉刷新的视图
     */
    void setRefreshOverView(OverView overView);

    interface HiRefreshListener {

        void onRefresh();

        boolean enableRefresh();
    }
}
