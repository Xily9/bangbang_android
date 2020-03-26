package com.autowheel.bangbang.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityLoginBinding;
import com.autowheel.bangbang.model.DataManager;
import com.autowheel.bangbang.model.network.RetrofitHelper;
import com.autowheel.bangbang.model.network.bean.request.LoginRequestBean;
import com.autowheel.bangbang.model.network.bean.response.GeneralResponseBean;
import com.autowheel.bangbang.model.network.bean.response.LoginResponseBean;
import com.autowheel.bangbang.model.network.bean.response.VerifyResponseBean;
import com.autowheel.bangbang.ui.MainActivity;
import com.autowheel.bangbang.utils.DeviceUtilKt;
import com.autowheel.bangbang.utils.LogUtilKt;
import com.autowheel.bangbang.utils.ToastyUtilKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Xily on 2020/3/26.
 */
public class LoginActivity extends BaseViewBindingActivity<ActivityLoginBinding> {

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
            LogUtilKt.debug("test", "我被点击了!");
            login();
        });
        getViewBinding().tvRegister.setOnClickListener(v -> {
            LogUtilKt.debug("test","success!");
            startActivity(new Intent(this, VerifyActivity.class));
        });
        getViewBinding().tvForgetPassword.setOnClickListener(v -> {

        });
    }

    private void login() {
        String username = getViewBinding().etUsername.getText().toString();
        String password = getViewBinding().etPassword.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            ToastyUtilKt.toastError("用户名或密码不能为空!");
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("登陆中...");
            progressDialog.show();
            LoginRequestBean loginRequestBean = new LoginRequestBean(username, password);
            RetrofitHelper.getApiService().login(loginRequestBean).enqueue(new Callback<GeneralResponseBean<LoginResponseBean>>() {
                @Override
                public void onResponse(Call<GeneralResponseBean<LoginResponseBean>> call, Response<GeneralResponseBean<LoginResponseBean>> response) {
                    //progressDialog.dismiss();
                    GeneralResponseBean<LoginResponseBean> loginResponseBean = response.body();
                    if (loginResponseBean.getCode() == 0) {
                        String token = loginResponseBean.getData().getToken();
                        DataManager.INSTANCE.setToken(token);
                        ToastyUtilKt.toastSuccess("登陆成功!");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        ToastyUtilKt.toastError(loginResponseBean.getMsg());
                    }
                }



                @Override
                public void onFailure(Call<GeneralResponseBean<LoginResponseBean>> call, Throwable t) {
                    //progressDialog.dismiss();
                    ToastyUtilKt.toastError("网络请求出错!");
                }
            });
        }
    }
}
