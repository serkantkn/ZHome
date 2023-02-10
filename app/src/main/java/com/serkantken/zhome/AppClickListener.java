package com.serkantken.zhome;

import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.serkantken.zhome.models.AppModel;

public interface AppClickListener {
    void onClick(AppModel appModel);
    void onLongClick(AppModel appModel, int position, ConstraintLayout container, TextView text);
}
