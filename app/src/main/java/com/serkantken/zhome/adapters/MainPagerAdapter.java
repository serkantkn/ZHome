package com.serkantken.zhome.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.serkantken.zhome.fragments.AppListFragment;
import com.serkantken.zhome.fragments.MainFragment;
import com.serkantken.zhome.models.AppModel;

import java.util.ArrayList;

public class MainPagerAdapter extends FragmentStateAdapter {
    private final int totalTabs;
    private final ArrayList<AppModel> appList;
    private final PageSwitcher pageSwitcher;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity, int totalTabs, ArrayList<AppModel> appList, PageSwitcher switcher) {
        super(fragmentActivity);
        this.totalTabs = totalTabs;
        this.appList = appList;
        pageSwitcher = switcher;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new AppListFragment(appList, pageSwitcher);
        }
        return new MainFragment(appList);
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
