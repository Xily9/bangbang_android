package com.autowheel.bangbang.ui.user.activity

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.utils.startActivity
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * Created by Xily on 2020/4/28.
 */
class SettingsActivity : BackBaseActivity() {
    override fun getToolbarTitle(): String {
        return "设置"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_settings
    }

    override fun initViews(savedInstanceState: Bundle?) {
        layout_safe_center.setOnClickListener {
            startActivity<SafeCenterActivity>()
        }
    }
}