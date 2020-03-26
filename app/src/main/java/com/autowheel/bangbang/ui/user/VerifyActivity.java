package com.autowheel.bangbang.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.autowheel.bangbang.R;
import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityVerifyBinding;
import com.autowheel.bangbang.model.DataManager;
import com.autowheel.bangbang.model.network.RetrofitHelper;
import com.autowheel.bangbang.model.network.bean.request.VerifyRequestBean;
import com.autowheel.bangbang.model.network.bean.response.GeneralResponseBean;
import com.autowheel.bangbang.model.network.bean.response.VerifyResponseBean;
import com.autowheel.bangbang.utils.DeviceUtilKt;
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
        DeviceUtilKt.setStatusBarUpper(this);
        DeviceUtilKt.setDarkStatusIcon(this, false);
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
            VerifyRequestBean verifyRequestBean = new VerifyRequestBean(username, password);
            RetrofitHelper.getApiService().verify(verifyRequestBean).enqueue(new Callback<GeneralResponseBean<VerifyResponseBean>>() {
                private Call<GeneralResponseBean<VerifyResponseBean>> call;
                private Throwable t;

                @Override
                public void onResponse(Call<GeneralResponseBean<VerifyResponseBean>> call, Response<GeneralResponseBean<VerifyResponseBean>> response) {
                    //progressDialog.dismiss();
                    GeneralResponseBean<VerifyResponseBean> verifyResponseBean = response.body();
                    if (verifyResponseBean.getCode() == 0) {
                        String token = verifyResponseBean.getData().getToken();
                        DataManager.INSTANCE.setToken(token);
                        ToastyUtilKt.toastSuccess("认证成功!");
                        Intent intent = new Intent(VerifyActivity.this, RegActivity.class);
                        startActivity(intent);
                    } else {
                        ToastyUtilKt.toastError(verifyResponseBean.getMsg());
                    }
                }

                @Override
                public void onFailure(Call<GeneralResponseBean<VerifyResponseBean>> call, Throwable t) {
                    this.call = call;
                    this.t = t;
                    //progressDialog.dismiss();
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
