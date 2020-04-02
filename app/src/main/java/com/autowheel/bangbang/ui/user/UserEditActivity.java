package com.autowheel.bangbang.ui.user;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.autowheel.bangbang.R;
import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityUserEditBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Xily on 2020/4/2.
 */
public class UserEditActivity extends BaseViewBindingActivity<ActivityUserEditBinding> {
    @NotNull
    @Override
    public ActivityUserEditBinding initViewBinding() {
        return ActivityUserEditBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        initToolbar();
    }

    @Override
    public void initToolbar() {
        setSupportActionBar(getViewBinding().toolbar.toolbar);
        getViewBinding().toolbar.toolbarTitle.setText("编辑资料");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void submit() {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_finish:
                submit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
