package com.autowheel.bangbang.ui.main;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xily on 2020/3/5.
 */
public class MainActivity extends BaseViewBindingActivity<ActivityMainBinding> {

    @NotNull
    @Override
    public ActivityMainBinding initViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        initToolbar();
        setTitle("学长帮帮忙");
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = getViewBinding().recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("测试" + i);
        }
        recyclerView.setAdapter(new MainAdapter(list));
    }

    @Override
    public void initToolbar() {
        setSupportActionBar(getViewBinding().toolbar.toolbar);
    }
}
