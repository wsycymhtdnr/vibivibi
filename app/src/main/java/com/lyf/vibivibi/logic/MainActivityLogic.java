package com.lyf.vibivibi.logic;

import android.content.res.Resources;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;

import com.lyf.vibi.common.tab.FragmentTabView;
import com.lyf.vibi.common.tab.TabViewAdapter;
import com.lyf.vibi.ui.tab.common.ITabLayout;
import com.lyf.vibi.ui.tab.bottom.TabBottomInfo;
import com.lyf.vibi.ui.tab.bottom.TabBottomLayout;
import com.lyf.vibivibi.R;
import com.lyf.vibivibi.fragment.DynamicFragment;
import com.lyf.vibivibi.fragment.ChannelFragment;
import com.lyf.vibivibi.fragment.HomePageFragment;
import com.lyf.vibivibi.fragment.PersonalFragment;
import com.lyf.vibivibi.fragment.ShoppingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivityLogic {
    private FragmentTabView fragmentTabView;
    private TabBottomLayout tabBottomLayout;
    private List<TabBottomInfo<?>> infoList;
    private ActivityProvider activityProvider;
    private int currentItemIndex;

    public MainActivityLogic(ActivityProvider activityProvider) {
        this.activityProvider = activityProvider;
        initTabBottom();
    }

    public FragmentTabView getFragmentTabView() {
        return fragmentTabView;
    }

    public TabBottomLayout getTabBottomLayout() {
        return tabBottomLayout;
    }

    public List<TabBottomInfo<?>> getInfoList() {
        return infoList;
    }

    private void initTabBottom() {
        tabBottomLayout = activityProvider.findViewById(R.id.tab_bottom_layout);
        tabBottomLayout.setTabAlpha(0.85f);
        infoList = new ArrayList<>();
        int defaultColor = activityProvider.getResources().getColor(R.color.tabBottomDefaultColor);
        int tintColor = activityProvider.getResources().getColor(R.color.tabBottomTintColor);
        TabBottomInfo<Integer> homeInfo = new TabBottomInfo<>("首页", "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_home), null, defaultColor, tintColor);
        homeInfo.fragment = HomePageFragment.class;
        TabBottomInfo<Integer> channelInfo = new TabBottomInfo<>("频道",
                "fonts/iconfont.ttf", activityProvider.getString(R.string.if_channel),
                null, defaultColor, tintColor);
        channelInfo.fragment = ChannelFragment.class;
        TabBottomInfo<Integer> dynamicInfo = new TabBottomInfo<>("动态", "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_dynamic), null,
                defaultColor, tintColor);
        dynamicInfo.fragment = DynamicFragment.class;
        TabBottomInfo<Integer> shoppingInfo = new TabBottomInfo<>("会员购", "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_shopping), null,
                defaultColor, tintColor);
        shoppingInfo.fragment = ShoppingFragment.class;
        TabBottomInfo<Integer> personalInfo = new TabBottomInfo<>("我的", "fonts/iconfont.ttf",
                activityProvider.getString(R.string.if_personal), null,
                defaultColor, tintColor);
        personalInfo.fragment = PersonalFragment.class;
        infoList.add(homeInfo);
        infoList.add(channelInfo);
        infoList.add(dynamicInfo);
        infoList.add(shoppingInfo);
        infoList.add(personalInfo);
        tabBottomLayout.inflateInfo(infoList);
        initFragmentTabView();
        tabBottomLayout.addTabSelectedChangeListener(new ITabLayout.OnTabSelectedListener<TabBottomInfo<?>>() {
            @Override
            public void onTabSelectedChange(int index, @Nullable TabBottomInfo<?> prevInfo, @NonNull TabBottomInfo<?> nextInfo) {
                fragmentTabView.setCurrentItem(index);
            }
        });
        tabBottomLayout.defaultSelected(homeInfo);
    }

    private void initFragmentTabView() {
        TabViewAdapter tabViewAdapter = new TabViewAdapter(infoList, activityProvider.getSupportFragmentManager());
        fragmentTabView = activityProvider.findViewById(R.id.fragment_tab_view);
        fragmentTabView.setAdapter(tabViewAdapter);
    }

    public interface ActivityProvider {
        <T extends View> T findViewById(@IdRes int id);

        Resources getResources();

        FragmentManager getSupportFragmentManager();

        String getString(@StringRes int resId);
    }
}
