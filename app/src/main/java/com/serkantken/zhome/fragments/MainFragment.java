package com.serkantken.zhome.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.serkantken.zhome.AppClickListener;
import com.serkantken.zhome.R;
import com.serkantken.zhome.adapters.ItemDeleteListener;
import com.serkantken.zhome.adapters.MainAdapter;
import com.serkantken.zhome.database.AppDatabase;
import com.serkantken.zhome.databinding.FragmentMainBinding;
import com.serkantken.zhome.models.AppModel;

import java.util.ArrayList;
import java.util.Comparator;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private AppDatabase database;
    private ArrayList<AppModel> pinnedApps;
    private MainAdapter adapter;
    private ConstraintLayout selectedMenu;
    private TextView selectedText;

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
    private final ItemDeleteListener deleteListener = new ItemDeleteListener() {
        @Override
        public void onItemDeleted(AppModel model) {
            database.mainDAO().delete(model);
            refreshList();
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater());
        database = AppDatabase.getInstance(requireContext());
        pinnedApps = (ArrayList<AppModel>) database.mainDAO().getAll();
        pinnedApps.sort(Comparator.comparing(AppModel::getId));
        adapter = new MainAdapter(requireContext(), pinnedApps, listener, deleteListener);
        binding.mainFragmentRV.setAdapter(adapter);

        binding.mainFragmentRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    if (selectedMenu != null)
                    {
                        selectedMenu.setVisibility(View.GONE);
                    }
                }
            }
        });
        binding.mainFragmentRV.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                selectedMenu.setVisibility(View.GONE);
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        return binding.getRoot();
    }

    private void refreshList() {
        pinnedApps = (ArrayList<AppModel>) database.mainDAO().getAll();
        adapter = new MainAdapter(requireContext(), pinnedApps, listener, deleteListener);
        binding.mainFragmentRV.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}