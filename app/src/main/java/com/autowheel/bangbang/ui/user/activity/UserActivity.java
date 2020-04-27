package com.autowheel.bangbang.ui.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.autowheel.bangbang.R;
import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityUserBinding;
import com.autowheel.bangbang.model.network.bean.ProfileBean;
import com.autowheel.bangbang.utils.UserUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Xily on 2020/4/2.
 */
public class UserActivity extends BaseViewBindingActivity<ActivityUserBinding> {
    @NotNull
    @Override
    public ActivityUserBinding initViewBinding() {
        return ActivityUserBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        initToolbar();
        initData();
        initLiveEventBus();
    }

    private void initData() {
        ProfileBean profileBean = UserUtil.INSTANCE.getProfile();
        getViewBinding().tvNickname.setText(profileBean.getNickname());
        getViewBinding().tvEmail.setText(profileBean.getEmail());
        getViewBinding().tvSignature.setText(profileBean.getSignature());
        getViewBinding().tvUsername.setText(profileBean.getUsername());
    }

    @Override
    public void initToolbar() {
        setSupportActionBar(getViewBinding().toolbar.toolbar);
        getViewBinding().toolbar.toolbarTitle.setText("查看资料");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initLiveEventBus() {
        LiveEventBus.get("refresh", Boolean.class)
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        initData();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_edit:
                startActivity(new Intent(this, UserEditActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
