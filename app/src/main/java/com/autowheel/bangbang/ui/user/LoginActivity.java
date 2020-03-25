package com.autowheel.bangbang.ui.user;

import android.os.Bundle;

import androidx.viewbinding.ViewBinding;

import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityLoginBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Xily on 2020/3/25.
 */
public class LoginActivity extends BaseViewBindingActivity {
    @NotNull
    @Override
    public ViewBinding initViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {

    }
}
