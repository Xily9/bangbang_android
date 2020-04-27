package com.autowheel.bangbang.ui.user.activity

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity

/**
 * Created by Xily on 2020/4/26.
 */
class ChangeEmailActivity : BackBaseActivity() {
    override fun getToolbarTitle(): String {
        return "修改email"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_change_email
    }

    override fun initViews(savedInstanceState: Bundle?) {
    }
}