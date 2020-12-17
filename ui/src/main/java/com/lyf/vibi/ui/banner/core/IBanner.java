package com.lyf.vibi.ui.banner.core;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.lyf.vibi.ui.banner.Banner;
import com.lyf.vibi.ui.banner.indicator.Indicator;

import java.util.List;

public interface IBanner {
    void setBannerData(@LayoutRes int layoutResId, @NonNull List<? extends BannerMo> models);

    void setBannerData(@NonNull List<? extends BannerMo> models);

    void setHiIndicator(Indicator hiIndicator);

    void setAutoPlay(boolean autoPlay);

    void setLoop(boolean loop);

    void setIntervalTime(int intervalTime);

    void setBindAdapter(IBindAdapter bindAdapter);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener);

    void setOnBannerClickListener(Banner.OnBannerClickListener onBannerClickListener);

    void setScrollDuration(int duration);

    interface OnBannerClickListener {
        void onBannerClick(@NonNull BannerAdapter.HiBannerViewHolder viewHolder, @NonNull BannerMo bannerMo, int position);
    }
}
