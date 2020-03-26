package com.autowheel.bangbang.ui.user;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.autowheel.bangbang.R;
import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityRegBinding;
import com.autowheel.bangbang.utils.ToastyUtilKt;

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
        setTitle("");
        getViewBinding().toolbar.toolbarTitle.setText("注册");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_index:
                ToastyUtilKt.toastWarning("aaa");
                break;
            case R.id.action_msg:
                ToastyUtilKt.toastInfo("bbb");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
