package com.autowheel.bangbang.ui.admin.activity

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity

/**
 * Created by Xily on 2020/5/30.
 */
class AdminHelpRewardPickUpActivity : BackBaseActivity() {
    override fun getToolbarTitle(): String {
        return "查看打卡记录"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_admin_help_reward_pickup
    }

    override fun initViews(savedInstanceState: Bundle?) {

    }
}