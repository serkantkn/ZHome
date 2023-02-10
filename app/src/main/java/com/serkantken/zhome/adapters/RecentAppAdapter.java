package com.serkantken.zhome.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.serkantken.zhome.databinding.RecentAppTileItemBinding;
import com.serkantken.zhome.models.AppModel;

import java.util.ArrayList;
import java.util.Objects;

public class RecentAppAdapter extends RecyclerView.Adapter<RecentAppAdapter.ViewHolder> {
    private final ArrayList<AppModel> recentAppList;
    private final ArrayList<AppModel> installedApps;

    public RecentAppAdapter(ArrayList<AppModel> recentAppList, ArrayList<AppModel> installedApps) {
        this.recentAppList = recentAppList;
        this.installedApps = installedApps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RecentAppTileItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppModel model = recentAppList.get(position);
        for (int j = 0; j <= installedApps.size()-1; j++) {
            if (Objects.equals(installedApps.get(j).getPackageName(), model.getPackageName())) {
                Glide.with(holder.binding.appIcon.getContext()).load(installedApps.get(j).getAppIcon()).into(holder.binding.appIcon);
            }
        }
        holder.binding.recentAppContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return recentAppList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecentAppTileItemBinding binding;
        public ViewHolder(@NonNull RecentAppTileItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
