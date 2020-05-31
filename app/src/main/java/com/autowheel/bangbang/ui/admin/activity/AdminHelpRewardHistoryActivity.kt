package com.autowheel.bangbang.ui.admin.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.HelpRewardHistoryBean
import com.autowheel.bangbang.ui.admin.adapter.AdminHelpRewardHistoryAdapter
import com.autowheel.bangbang.utils.toastError
import kotlinx.android.synthetic.main.activity_admin_help_reward_history.*

/**
 * Created by Xily on 2020/5/31.
 */
class AdminHelpRewardHistoryActivity : BackBaseActivity() {
    private lateinit var adapter: AdminHelpRewardHistoryAdapter
    private val list = arrayListOf<HelpRewardHistoryBean>()
    override fun getToolbarTitle(): String {
        return "通过审核名单"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_admin_help_reward_history
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        rv_history.layoutManager = LinearLayoutManager(this)
        adapter = AdminHelpRewardHistoryAdapter(list)
        rv_history.adapter = adapter
    }

    private fun loadData() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getHelpRewardHistory()
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data)
                adapter.notifyDataSetChanged()
            } else {
                toastError("加载失败")
            }
        }, catchBlock = {
            toastError("加载失败")
        })
    }
}