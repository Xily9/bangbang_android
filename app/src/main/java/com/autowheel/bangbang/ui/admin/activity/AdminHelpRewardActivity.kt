package com.autowheel.bangbang.ui.admin.activity

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import kotlinx.android.synthetic.main.activity_admin_help_reward.*

/**
 * Created by Xily on 2020/5/30.
 */
class AdminHelpRewardActivity : BackBaseActivity() {
    override fun getToolbarTitle(): String {
        return "综测申请审核"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_admin_help_reward
    }

    override fun initViews(savedInstanceState: Bundle?) {
        swipe_refresh_layout.setColorSchemeColors(resources.getColor(R.color.blue))
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
        }
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {

    }

    private fun loadData() {

    }
}