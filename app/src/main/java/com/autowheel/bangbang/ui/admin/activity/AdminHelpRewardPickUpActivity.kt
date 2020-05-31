package com.autowheel.bangbang.ui.admin.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.HelpRewardPickUpBean
import com.autowheel.bangbang.ui.admin.adapter.AdminHelpRewardPickUpAdapter
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastInfo
import kotlinx.android.synthetic.main.activity_admin_help_reward_pickup.*

/**
 * Created by Xily on 2020/5/30.
 */
class AdminHelpRewardPickUpActivity : BackBaseActivity() {
    private lateinit var adapter: AdminHelpRewardPickUpAdapter
    private var list = arrayListOf<HelpRewardPickUpBean>()
    private var id = 0
    override fun getToolbarTitle(): String {
        return "查看打卡记录"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_admin_help_reward_pickup
    }

    override fun initViews(savedInstanceState: Bundle?) {
        id = intent.getIntExtra("id", 0)
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        rv_pickup.layoutManager = LinearLayoutManager(this)
        adapter = AdminHelpRewardPickUpAdapter(list)
        rv_pickup.adapter = adapter
    }

    private fun loadData() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getHelpRewardPickUpHistory(id)
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data)
                adapter.notifyDataSetChanged()
                if (result.data.isEmpty()) {
                    toastInfo("空空如也")
                }
            } else {
                toastError("加载失败")
            }
        }, catchBlock = {
            toastError("加载失败")
        })
    }

}