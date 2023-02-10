package com.serkantken.zhome;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.serkantken.zhome.adapters.AppListAdapter;
import com.serkantken.zhome.adapters.MainPagerAdapter;
import com.serkantken.zhome.adapters.PageSwitcher;
import com.serkantken.zhome.databinding.ActivityMainBinding;
import com.serkantken.zhome.models.AppModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainPagerAdapter adapter;
    private final PageSwitcher pageSwitcher = new PageSwitcher() {
        @Override
        public void switchTo() {
            binding.pageViewer.setCurrentItem(0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                synchronized (this){
                    runOnUiThread(() -> {
                        ArrayList<AppModel> list = new ArrayList<>();

                        Intent intent = new Intent(Intent.ACTION_MAIN, null);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        List<ResolveInfo> untreatedAppList = getPackageManager().queryIntentActivities(intent, 0);

                        for (ResolveInfo app : untreatedAppList)
                        {
                            String appName = app.loadLabel(getPackageManager()).toString();
                            String appPackageName = app.activityInfo.packageName;
                            Drawable appImage = app.activityInfo.loadIcon(getPackageManager());
                            AppModel appModel = new AppModel(appName, appPackageName, appImage);
                            if (!list.contains(appModel))
                            {
                                list.add(appModel);
                            }
                        }

                        list.sort(Comparator.comparing(AppModel::getAppName));

                        adapter = new MainPagerAdapter(MainActivity.this, 2, list, pageSwitcher);
                        binding.pageViewer.setAdapter(adapter);
                        dialog.dismiss();
                    });
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}