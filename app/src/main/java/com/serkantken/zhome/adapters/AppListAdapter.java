package com.serkantken.zhome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.serkantken.zhome.AppClickListener;
import com.serkantken.zhome.R;
import com.serkantken.zhome.database.AppDatabase;
import com.serkantken.zhome.databinding.AppIconItemBinding;
import com.serkantken.zhome.models.AppModel;

import java.util.ArrayList;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<AppModel> appList;
    private final AppClickListener listener;
    private final PageSwitcher switcher;
    private final AppDatabase database;

    public AppListAdapter(Context context, ArrayList<AppModel> appList, AppClickListener listener, AppDatabase database, PageSwitcher switcher) {
        this.context = context;
        this.appList = appList;
        this.listener = listener;
        this.database = database;
        this.switcher = switcher;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AppIconItemBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppModel model = appList.get(position);
        holder.binding.appName.setText(model.getAppName());
        Glide.with(context).load(model.getAppIcon()).into(holder.binding.appIcon);
        holder.binding.appContainer.setOnClickListener(view -> listener.onClick(model));
        holder.binding.appContainer.setOnLongClickListener(view -> {
            listener.onLongClick(model, holder.binding.menuContainer, holder.binding.appName);
            return true;
        });
        holder.binding.optionPin.setOnClickListener(view -> {
            database.mainDAO().insert(model);
            holder.binding.menuContainer.setVisibility(View.GONE);
            holder.binding.appName.setTextColor(context.getResources().getColor(R.color.textColorPrimary, null));
            switcher.switchTo();
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppIconItemBinding binding;
        public ViewHolder(@NonNull AppIconItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
