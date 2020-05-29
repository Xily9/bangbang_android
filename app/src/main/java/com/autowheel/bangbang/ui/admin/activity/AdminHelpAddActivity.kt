package com.autowheel.bangbang.ui.admin.activity

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity

/**
 * Created by Xily on 2020/5/29.
 */
class AdminHelpAddActivity : BackBaseActivity() {
    override fun getToolbarTitle(): String {
        return ""
    }

    override fun  getLayoutId(): Int {
        return  R.layout.activity_admin_helper_add
    }

    override fun initViews(savedInstanceState: Bundle?) {
    }
}