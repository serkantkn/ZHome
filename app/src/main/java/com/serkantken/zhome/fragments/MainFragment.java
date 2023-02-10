package com.serkantken.zhome.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.serkantken.zhome.AppClickListener;
import com.serkantken.zhome.R;
import com.serkantken.zhome.adapters.ItemDeleteListener;
import com.serkantken.zhome.adapters.MainAdapter;
import com.serkantken.zhome.adapters.RecentAppAdapter;
import com.serkantken.zhome.database.AppDatabase;
import com.serkantken.zhome.database.RecentAppDatabase;
import com.serkantken.zhome.databinding.FragmentMainBinding;
import com.serkantken.zhome.models.AppModel;

import java.util.ArrayList;
import java.util.Comparator;

public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private AppDatabase database;
    private RecentAppDatabase recentAppDatabase;
    private ArrayList<AppModel> pinnedApps;
    private ArrayList<AppModel> recentApps;
    private final ArrayList<AppModel> installedApps;
    private MainAdapter adapter;
    private RecentAppAdapter recentAppAdapter;
    private ConstraintLayout selectedMenu;
    private TextView selectedText;
    private boolean isRecentsActive = false;

    private final AppClickListener listener = new AppClickListener() {
        @Override
        public void onClick(AppModel appModel) {
            if (selectedMenu != null) {
                selectedMenu.setVisibility(View.GONE);
                selectedText.setTextColor(getResources().getColor(R.color.textColorPrimary, null));
                selectedMenu = null;
                selectedText = null;
            } else {
                if (binding.mainFragmentRV.getAlpha() == 1.0f) {
                    recentAppDatabase.recentAppsDAO().insert(appModel);
                    Intent launchAppIntent = requireContext().getPackageManager().getLaunchIntentForPackage(appModel.getPackageName());
                    if (launchAppIntent != null)
                    {
                        requireContext().startActivity(launchAppIntent);
                    }
                } else {
                    if (isRecentsActive) {
                        doResizeAnimation(63, 350, binding.recentAppsRV, 0.3f, binding.mainFragmentRV, 1.0f);
                        //binding.buttonUp.setVisibility(View.VISIBLE);
                        binding.recentsTitle.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in));
                        binding.recentsTitle.setVisibility(View.GONE);
                        isRecentsActive = false;
                    }
                }
            }
        }

        @Override
        public void onLongClick(AppModel appModel, int position, ConstraintLayout container, TextView text) {
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
                binding.mainFragmentRV.smoothScrollToPosition(position);
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

    public MainFragment(ArrayList<AppModel> installedApps) {
        this.installedApps = installedApps;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater());
        database = AppDatabase.getInstance(requireContext());
        recentAppDatabase = RecentAppDatabase.getInstance(requireContext());

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
                        selectedText.setTextColor(getResources().getColor(R.color.textColorPrimary, null));
                    }
                    if (isRecentsActive) {
                        doResizeAnimation(63, 350, binding.recentAppsRV, 0.3f, binding.mainFragmentRV, 1.0f);
                        //binding.buttonUp.setVisibility(View.VISIBLE);
                        binding.recentsTitle.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in));
                        binding.recentsTitle.setVisibility(View.GONE);
                        isRecentsActive = false;
                    }
                }
            }
        });
        binding.recentAppsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isRecentsActive) {
                    doResizeAnimation(130, 350, binding.recentAppsRV, 1.0f, binding.mainFragmentRV, 0.3f);
                    //binding.buttonUp.setVisibility(View.GONE);
                    binding.recentsTitle.setVisibility(View.VISIBLE);
                    binding.recentsTitle.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out));
                    isRecentsActive = true;
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
                selectedText.setTextColor(getResources().getColor(R.color.textColorPrimary, null));
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        recentApps = (ArrayList<AppModel>) recentAppDatabase.recentAppsDAO().getAll();
        recentApps.sort(Comparator.comparing(AppModel::getId));
        recentAppAdapter = new RecentAppAdapter(recentApps, installedApps);
        binding.recentAppsRV.setAdapter(recentAppAdapter);

        return binding.getRoot();
    }

    private void doResizeAnimation(int dp, int duration, View animatingView, float animatingViewsAlpha, View secondView, float secondViewsAlpha) {
        ResizeWidthAnimation animation = new ResizeWidthAnimation(binding.recentAppsRV, dpToPx(dp));
        animation.setDuration(duration);
        animatingView.startAnimation(animation);
        animatingView.setAlpha(animatingViewsAlpha);
        secondView.setAlpha(secondViewsAlpha);
    }

    private void refreshList() {
        pinnedApps = (ArrayList<AppModel>) database.mainDAO().getAll();
        pinnedApps.sort(Comparator.comparing(AppModel::getId));
        adapter = new MainAdapter(requireContext(), pinnedApps, listener, deleteListener);
        binding.mainFragmentRV.setAdapter(adapter);

        recentApps = (ArrayList<AppModel>) recentAppDatabase.recentAppsDAO().getAll();
        recentApps.sort(Comparator.comparing(AppModel::getId));
        recentAppAdapter = new RecentAppAdapter(recentApps, installedApps);
        binding.recentAppsRV.setAdapter(recentAppAdapter);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = requireContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    public static class ResizeWidthAnimation extends Animation {
        private final int mWidth;
        private final int mStartWidth;
        private final View mView;

        public ResizeWidthAnimation(View view, int width) {
            mView = view;
            mWidth = width;
            mStartWidth = view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            mView.getLayoutParams().width = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);
            mView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}