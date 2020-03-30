package com.autowheel.bangbang.ui.index;

import android.os.Bundle;

import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivitySearchBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Xily on 2020/3/29.
 */
public class SearchActivity extends BaseViewBindingActivity<ActivitySearchBinding> {
    @NotNull
    @Override
    public ActivitySearchBinding initViewBinding() {
        return ActivitySearchBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {

    }
}
