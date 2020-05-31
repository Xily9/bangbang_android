package com.autowheel.bangbang.ui.admin.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.GeneralResponseBean
import com.autowheel.bangbang.model.network.bean.ProfileBean
import com.autowheel.bangbang.ui.user.activity.AvatarActivity
import com.autowheel.bangbang.ui.user.activity.LoginActivity
import com.autowheel.bangbang.ui.user.activity.SettingsActivity
import com.autowheel.bangbang.ui.user.activity.UserActivity
import com.autowheel.bangbang.utils.UserUtil
import com.autowheel.bangbang.utils.startActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.activity_admin_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Xily on 2020/5/28.
 */
class AdminMainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_admin_main
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolbar()
        initData()
        getProfile()
        initLiveEventBus()
        iv_avatar.setOnClickListener {
            startActivity<AvatarActivity>()
        }
        btn_logout.setOnClickListener {
            logout()
        }
        layout_user.setOnClickListener {
            startActivity<UserActivity>()
        }
        layout_help_add.setOnClickListener {
            startActivity<AdminHelpAddActivity>()
        }
        layout_help_approve.setOnClickListener {
            startActivity<AdminHelpApproveActivity>()
        }
        layout_help_point_check.setOnClickListener {
            startActivity<AdminHelpRewardActivity>()
        }
    }

    private fun getProfile() {
        RetrofitHelper.getApiService().getProfile()
            .enqueue(object : Callback<GeneralResponseBean<ProfileBean>?> {
                override fun onFailure(
                    call: Call<GeneralResponseBean<ProfileBean>?>,
                    t: Throwable
                ) {
                    initData()
                }

                override fun onResponse(
                    call: Call<GeneralResponseBean<ProfileBean>?>,
                    response: Response<GeneralResponseBean<ProfileBean>?>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()!!
                        if (body.code == 0) {
                            val profile = body.data
                            UserUtil.profile = profile
                        }
                    }
                    initData()
                }
            })
    }

    private fun initData() {
        val profile = UserUtil.profile
        if (profile.uid != 0) {
            tv_nickname.text = profile.nickname
            Glide.with(this).load("$BASE_URL/user/avatar/${profile.uid}")
                .signature(ObjectKey(UserUtil.avatarUpdateTime))
                .error(R.mipmap.ic_launcher).into(iv_avatar)
        }
    }

    private fun initLiveEventBus() {
        LiveEventBus.get("refresh", Boolean::class.java)
            .observe(this, Observer {
                initData()
            })
    }

    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle("退出登录")
            .setMessage("确认要退出登录吗?")
            .setPositiveButton("确认") { dialog, which ->
                UserUtil.clear()
                startActivity<LoginActivity>()
                finish()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    override fun initToolbar() {
        toolbar_title.text = "后台"
        iv_settings.setOnClickListener {
            startActivity<SettingsActivity>()
        }
    }
}