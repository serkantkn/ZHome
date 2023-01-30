package com.serkantken.zhome.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.serkantken.zhome.fragments.AppListFragment;
import com.serkantken.zhome.fragments.MainFragment;

public class MainPagerAdapter extends FragmentStateAdapter {
    private final int totalTabs;
    private final PageSwitcher pageSwitcher;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity, int totalTabs, PageSwitcher switcher) {
        super(fragmentActivity);
        this.totalTabs = totalTabs;
        pageSwitcher = switcher;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new AppListFragment(pageSwitcher);
        }
        return new MainFragment();
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
