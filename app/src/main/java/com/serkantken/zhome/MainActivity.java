package com.serkantken.zhome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.serkantken.zhome.adapters.MainPagerAdapter;
import com.serkantken.zhome.adapters.PageSwitcher;
import com.serkantken.zhome.databinding.ActivityMainBinding;

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

        adapter = new MainPagerAdapter(this, 2, pageSwitcher);
        binding.pageViewer.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}