package com.autowheel.bangbang.ui.user.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.autowheel.bangbang.R;
import com.autowheel.bangbang.base.BaseViewBindingActivity;
import com.autowheel.bangbang.databinding.ActivityUserEditBinding;
import com.autowheel.bangbang.model.network.RetrofitHelper;
import com.autowheel.bangbang.model.network.bean.GeneralResponseBean;
import com.autowheel.bangbang.model.network.bean.ProfileBean;
import com.autowheel.bangbang.utils.ToastyUtilKt;
import com.autowheel.bangbang.utils.UserUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        initData();
    }

    @Override
    public void initToolbar() {
        setSupportActionBar(getViewBinding().toolbar.toolbar);
        getViewBinding().toolbar.toolbarTitle.setText("编辑资料");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        ProfileBean profileBean = UserUtil.INSTANCE.getProfile();
        getViewBinding().etNickname.setText(profileBean.getNickname());
        getViewBinding().etSignature.setText(profileBean.getSignature());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void submit() {
        String nickname = getViewBinding().etNickname.getText().toString();
        String signature = getViewBinding().etSignature.getText().toString();
        RetrofitHelper.getApiService().editProfile(nickname, signature).enqueue(new Callback<GeneralResponseBean<Object>>() {
            @Override
            public void onResponse(Call<GeneralResponseBean<Object>> call, Response<GeneralResponseBean<Object>> response) {
                if (response.isSuccessful() && response.body().getCode() == 0) {
                    ToastyUtilKt.toastSuccess("修改成功");
                    ProfileBean profileBean = UserUtil.INSTANCE.getProfile();
                    profileBean.setNickname(nickname);
                    profileBean.setSignature(signature);
                    UserUtil.INSTANCE.setProfile(profileBean);
                    LiveEventBus.get("refresh", Boolean.class).post(true);
                    finish();
                } else {
                    ToastyUtilKt.toastError("修改失败");
                }
            }

            @Override
            public void onFailure(Call<GeneralResponseBean<Object>> call, Throwable t) {
                ToastyUtilKt.toastError("修改失败!");
            }
        });
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
