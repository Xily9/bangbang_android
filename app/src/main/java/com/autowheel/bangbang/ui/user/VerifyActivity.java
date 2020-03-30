package com.autowheel.bangbang.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityVerifyBinding;
import com.autowheel.bangbang.model.network.RetrofitHelper;
import com.autowheel.bangbang.model.network.bean.GeneralResponseBean;
import com.autowheel.bangbang.utils.ToastyUtilKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by XP228-x on 2020/3/26.
 */

public class VerifyActivity extends BaseViewBindingActivity<ActivityVerifyBinding> {
    @NotNull
    @Override
    public ActivityVerifyBinding initViewBinding() {
        return ActivityVerifyBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        initToolbar();
        getViewBinding().btnVerify.setOnClickListener(v -> {
            verify();
        });
    }

    private void verify() {
        String username = getViewBinding().etStudentId.getText().toString();
        String password = getViewBinding().etStudentPassword.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            ToastyUtilKt.toastError("学号或密码不能为空!");
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("认证中...");
            progressDialog.show();
            RetrofitHelper.getApiService().verify(username, password)
                    .enqueue(new Callback<GeneralResponseBean<Object>>() {
                        @Override
                        public void onResponse(Call<GeneralResponseBean<Object>> call, Response<GeneralResponseBean<Object>> response) {
                            GeneralResponseBean<Object> verifyResponseBean = response.body();
                            if (verifyResponseBean.getCode() == 0) {
                                ToastyUtilKt.toastSuccess("认证成功!");
                                Intent intent = new Intent(VerifyActivity.this, RegActivity.class);
                                startActivity(intent);
                            } else {
                                ToastyUtilKt.toastError(verifyResponseBean.getMsg());
                            }
                        }

                        @Override
                        public void onFailure(Call<GeneralResponseBean<Object>> call, Throwable t) {
                            ToastyUtilKt.toastError("网络请求出错!");
                        }
                    });
        }
    }

    @Override
    public void initToolbar() {
        setSupportActionBar(getViewBinding().toolbar.toolbar);
        setTitle("");
        getViewBinding().toolbar.toolbarTitle.setText("认证");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
