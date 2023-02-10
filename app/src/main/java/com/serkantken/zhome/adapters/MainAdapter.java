package com.serkantken.zhome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serkantken.zhome.AppClickListener;
import com.serkantken.zhome.databinding.PinnedAppItemBinding;
import com.serkantken.zhome.models.AppModel;

import java.util.List;
import java.util.Locale;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.PinnedAppsViewHolder>{
    private final Context context;
    private final List<AppModel> pinnedAppList;
    private final AppClickListener listener;
    private final ItemDeleteListener deleteListener;

    public MainAdapter(Context context, List<AppModel> pinnedAppList, AppClickListener listener, ItemDeleteListener deleteListener) {
        this.context = context;
        this.pinnedAppList = pinnedAppList;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public PinnedAppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PinnedAppsViewHolder(PinnedAppItemBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PinnedAppsViewHolder holder, int position) {
        AppModel model = pinnedAppList.get(position);
        holder.binding.appName.setText(model.getAppName().toLowerCase(Locale.ROOT));
        holder.binding.pinnedContainer.setOnClickListener(view -> listener.onClick(model));
        holder.binding.pinnedContainer.setOnLongClickListener(view -> {
            listener.onLongClick(model, position, holder.binding.menuContainer, holder.binding.appName);
            return true;
        });
        holder.binding.optionUnpin.setOnClickListener(view -> deleteListener.onItemDeleted(model));
    }

    @Override
    public int getItemCount() {
        return pinnedAppList.size();
    }

    public static class PinnedAppsViewHolder extends RecyclerView.ViewHolder {
        PinnedAppItemBinding binding;
        public PinnedAppsViewHolder(@NonNull PinnedAppItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
