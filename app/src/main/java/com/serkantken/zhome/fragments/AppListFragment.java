package com.serkantken.zhome.fragments;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.serkantken.zhome.AppClickListener;
import com.serkantken.zhome.R;
import com.serkantken.zhome.adapters.AppListAdapter;
import com.serkantken.zhome.adapters.PageSwitcher;
import com.serkantken.zhome.database.AppDatabase;
import com.serkantken.zhome.databinding.FragmentAppListBinding;
import com.serkantken.zhome.models.AppModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppListFragment extends Fragment {
    private FragmentAppListBinding binding;
    private ArrayList<AppModel> installedApps;
    private AppListAdapter adapter;
    private AppDatabase database;
    private ConstraintLayout selectedMenu;
    private TextView selectedText;
    private final PageSwitcher switcher;

    private final AppClickListener listener = new AppClickListener() {
        @Override
        public void onClick(AppModel appModel) {
            if (selectedMenu != null) {
                selectedMenu.setVisibility(View.GONE);
                selectedText.setTextColor(getResources().getColor(R.color.textColorPrimary, null));
                selectedMenu = null;
                selectedText = null;
            } else {
                Intent launchAppIntent = requireContext().getPackageManager().getLaunchIntentForPackage(appModel.getPackageName());
                if (launchAppIntent != null)
                {
                    requireContext().startActivity(launchAppIntent);
                }
            }
        }

        @Override
        public void onLongClick(AppModel appModel, ConstraintLayout container, TextView text) {
            if (container.getVisibility() == View.VISIBLE) {
                container.setVisibility(View.GONE);
                text.setTextColor(getResources().getColor(R.color.textColorPrimary, null));
            } else {
                if (selectedMenu != null) {
                    selectedMenu.setVisibility(View.GONE);
                    text.setTextColor(getResources().getColor(R.color.textColorPrimary, null));
                }
                selectedMenu = container;
                selectedText = text;
                container.setVisibility(View.VISIBLE);
                text.setTextColor(getResources().getColor(R.color.purple_500, null));
            }
        }
    };

    public AppListFragment(PageSwitcher switcher) {
        this.switcher = switcher;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAppListBinding.inflate(getLayoutInflater());
        database = AppDatabase.getInstance(requireContext());
        installedApps = getInstalledApps();
        installedApps.sort(Comparator.comparing(AppModel::getAppName));
        adapter = new AppListAdapter(requireContext(), installedApps, listener, database, switcher);
        binding.appsRV.setAdapter(adapter);

        binding.appsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    if (selectedMenu != null)
                    {
                        selectedMenu.setVisibility(View.GONE);
                        selectedText.setTextColor(getResources().getColor(R.color.textColorPrimary, null));
                    }
                }
            }
        });
        binding.appsRV.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                selectedMenu.setVisibility(View.GONE);
                selectedText.setTextColor(getResources().getColor(R.color.textColorPrimary, null));
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        return binding.getRoot();
    }

    private ArrayList<AppModel> getInstalledApps() {
        ArrayList<AppModel> list = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> untreatedAppList = requireContext().getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo app : untreatedAppList)
        {
            String appName = app.loadLabel(requireContext().getPackageManager()).toString();
            String appPackageName = app.activityInfo.packageName;
            Drawable appImage = app.activityInfo.loadIcon(requireContext().getPackageManager());
            AppModel appModel = new AppModel(appName, appPackageName, appImage);
            if (!list.contains(appModel))
            {
                list.add(appModel);
            }
        }

        return list;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        binding.appsRV.scrollToPosition(0);
    }
}