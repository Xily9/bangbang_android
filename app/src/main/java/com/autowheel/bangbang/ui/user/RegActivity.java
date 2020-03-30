package com.autowheel.bangbang.ui.user;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityRegBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Xily on 2020/3/26.
 */
public class RegActivity extends BaseViewBindingActivity<ActivityRegBinding> {
    @NotNull
    @Override
    public ActivityRegBinding initViewBinding() {
        return ActivityRegBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        initToolbar();
    }

    @Override
    public void initToolbar() {
        setSupportActionBar(getViewBinding().toolbar.toolbar);
        getViewBinding().toolbar.toolbarTitle.setText("注册");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
