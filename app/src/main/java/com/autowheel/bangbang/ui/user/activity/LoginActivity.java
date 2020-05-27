package com.autowheel.bangbang.ui.user.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityLoginBinding;
import com.autowheel.bangbang.model.network.RetrofitHelper;
import com.autowheel.bangbang.model.network.bean.GeneralResponseBean;
import com.autowheel.bangbang.model.network.bean.LoginBean;
import com.autowheel.bangbang.model.network.bean.ProfileBean;
import com.autowheel.bangbang.ui.MainActivity;
import com.autowheel.bangbang.utils.DeviceUtilKt;
import com.autowheel.bangbang.utils.ToastyUtilKt;
import com.autowheel.bangbang.utils.UserUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Xily on 2020/3/26.
 */
public class LoginActivity extends BaseViewBindingActivity<ActivityLoginBinding> {
    private ProgressDialog progressDialog;

    @NotNull
    @Override
    public ActivityLoginBinding initViewBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        DeviceUtilKt.setStatusBarUpper(this);
        DeviceUtilKt.setDarkStatusIcon(this, false);
        getViewBinding().btnLogin.setOnClickListener(v -> {
            login();
        });
        getViewBinding().tvRegister.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, RegActivity.class), 1);
        });
        getViewBinding().tvForgetPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgetPasswordActivity.class));
        });
    }

    private void login() {
        String username = getViewBinding().etUsername.getText().toString();
        String password = getViewBinding().etPassword.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            ToastyUtilKt.toastError("用户名或密码不能为空!");
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("登录中...");
            progressDialog.show();
            RetrofitHelper.getApiService().login(username, password).enqueue(new Callback<GeneralResponseBean<LoginBean>>() {
                @Override
                public void onResponse(Call<GeneralResponseBean<LoginBean>> call, Response<GeneralResponseBean<LoginBean>> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        GeneralResponseBean<LoginBean> loginResponseBean = response.body();
                        if (loginResponseBean.getCode() == 0) {
                            if (loginResponseBean.getData().is_admin()) {
                                UserUtil.INSTANCE.setAdmin(true);
                            }
                            getProfile();
                        } else {
                            ToastyUtilKt.toastError(loginResponseBean.getMsg());
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        ToastyUtilKt.toastError("网络请求出错!");
                    }
                }

                @Override
                public void onFailure(Call<GeneralResponseBean<LoginBean>> call, Throwable t) {
                    progressDialog.dismiss();
                    ToastyUtilKt.toastError("网络请求出错!");
                }
            });
        }
    }

    private void getProfile() {
        RetrofitHelper.getApiService().getProfile().enqueue(new Callback<GeneralResponseBean<ProfileBean>>() {
            @Override
            public void onResponse(Call<GeneralResponseBean<ProfileBean>> call, Response<GeneralResponseBean<ProfileBean>> response) {
                if (response.isSuccessful()) {
                    GeneralResponseBean<ProfileBean> body = response.body();
                    if (body.getCode() == 0) {
                        UserUtil.INSTANCE.setProfile(body.getData());
                        ToastyUtilKt.toastSuccess("登陆成功!");
                        UserUtil.INSTANCE.setVerify(true);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (body.getCode() == -2) {
                        ToastyUtilKt.toastWarning("请先进行实名认证");
                        Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastyUtilKt.toastError(body.getMsg());
                    }
                } else {
                    ToastyUtilKt.toastError("网络请求出错!");
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<GeneralResponseBean<ProfileBean>> call, Throwable t) {
                ToastyUtilKt.toastError("网络请求出错!");
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            getViewBinding().etUsername.setText(data.getStringExtra("username"));
            getViewBinding().etPassword.setText(data.getStringExtra("password"));
            getViewBinding().btnLogin.performClick();
        }
    }
}
