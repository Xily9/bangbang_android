package com.autowheel.bangbang.ui.user.activity

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.utils.startActivity
import kotlinx.android.synthetic.main.activity_safe_center.*

/**
 * Created by Xily on 2020/4/27.
 */
class SafeCenterActivity : BackBaseActivity() {
    override fun getToolbarTitle(): String {
        return "安全中心"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_safe_center
    }

    override fun initViews(savedInstanceState: Bundle?) {
        layout_change_password.setOnClickListener {
            startActivity<ChangePasswordActivity>()
        }
        layout_change_email.setOnClickListener {
            startActivity<ChangeEmailActivity>()
        }
    }
}