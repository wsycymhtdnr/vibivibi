package com.lyf.vibivibi.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lyf.vibi.common.ui.component.BaseFragment;
import com.lyf.vibivibi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomePageFragment extends BaseFragment {
    private View view;
    private TabPagerAdapter adapter;
    private List<Fragment> mFragments = new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView tv_game = view.findViewById(R.id.tv_game);
        TextView tv_message = view.findViewById(R.id.tv_message);
        Typeface typeface =
                Typeface.createFromAsset(getContext().getAssets(), "fonts/iconfont.ttf");
        tv_game.setTypeface(typeface);
        tv_message.setTypeface(typeface);
        initChildFragment();
        TabLayout mTabLayout = view.findViewById(R.id.tab_layout);
        // 添加 tab item
        mTabLayout.addTab(mTabLayout.newTab().setText("直播"));
        mTabLayout.addTab(mTabLayout.newTab().setText("推荐"));
        mTabLayout.addTab(mTabLayout.newTab().setText("追番"));
        mTabLayout.addTab(mTabLayout.newTab().setText("影视"));
        mTabLayout.addTab(mTabLayout.newTab().setText("专栏"));
        Objects.requireNonNull(mTabLayout.getTabAt(1)).select();
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        adapter = new TabPagerAdapter(getChildFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        //mTabLayout.setupWithViewPager(viewPager);
    }

    private void initChildFragment() {
        mFragments.add(new HomeTabFragment());
        mFragments.add(new HomeTabFragment());
        mFragments.add(new HomeTabFragment());
        mFragments.add(new HomeTabFragment());
        mFragments.add(new HomeTabFragment());
    }
    private class TabPagerAdapter extends FragmentPagerAdapter {
        public TabPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
