package com.autowheel.bangbang.ui.user;

import android.os.Bundle;

import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivitySettingsBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Xily on 2020/3/30.
 */
class SettingsActivity extends BaseViewBindingActivity<ActivitySettingsBinding> {
    @NotNull
    @Override
    public ActivitySettingsBinding initViewBinding() {
        return ActivitySettingsBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {

    }
}
