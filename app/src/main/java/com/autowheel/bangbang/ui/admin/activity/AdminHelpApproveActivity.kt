package com.autowheel.bangbang.ui.admin.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.HelpApproveListBean
import com.autowheel.bangbang.ui.admin.adapter.AdminHelpApproveAdapter
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastInfo
import com.autowheel.bangbang.utils.toastSuccess
import kotlinx.android.synthetic.main.activity_admin_help_approve.*

/**
 * Created by Xily on 2020/5/29.
 */
class AdminHelpApproveActivity : BackBaseActivity() {
    private lateinit var adapter: AdminHelpApproveAdapter
    private var list = arrayListOf<HelpApproveListBean>()
    override fun getToolbarTitle(): String {
        return "帮扶批准"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_admin_help_approve
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
        rv_approve.layoutManager = LinearLayoutManager(this)
        adapter = AdminHelpApproveAdapter(list)
        adapter.listener = {
            approveHelp(it, list[it].assistants[list[it].selectIndex - 1].couple_id)
        }
        rv_approve.adapter = adapter
    }

    private fun loadData() {
        swipe_refresh_layout.isRefreshing = true
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getHelpApproveList()
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data.filter { it.assistants.isNotEmpty() })
                adapter.notifyDataSetChanged()
                if (list.isEmpty()) {
                    toastInfo("空空如也")
                }
            } else {
                toastError("加载失败")
            }
        }, catchBlock = {
            it.printStackTrace()
            toastError("网络请求出错")
        }, finallyBlock = {
            swipe_refresh_layout.isRefreshing = false
        })
    }

    private fun approveHelp(position: Int, coupleId: Int) {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().approveHelp(coupleId)
            if (result.code == 0) {
                toastSuccess("批准帮扶成功！")
                list.removeAt(position)
                adapter.notifyDataSetChanged()
            } else {
                toastError(result.msg)
            }
        }, catchBlock = {
            it.printStackTrace()
            toastError("网络请求出错!")
        }, finallyBlock = {

        })
    }
}