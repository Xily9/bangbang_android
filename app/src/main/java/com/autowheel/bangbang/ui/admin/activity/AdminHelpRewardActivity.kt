package com.autowheel.bangbang.ui.admin.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.HelpRewardListBean
import com.autowheel.bangbang.ui.admin.adapter.AdminHelpRewardAdapter
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastInfo
import com.autowheel.bangbang.utils.toastSuccess
import kotlinx.android.synthetic.main.activity_admin_help_reward.*

/**
 * Created by Xily on 2020/5/30.
 */
class AdminHelpRewardActivity : BackBaseActivity() {
    private lateinit var adapter: AdminHelpRewardAdapter
    private var list = arrayListOf<HelpRewardListBean>()
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
        rv_reward.layoutManager = LinearLayoutManager(this)
        adapter = AdminHelpRewardAdapter(list)
        adapter.btnAgreeListener = {
            agree(it, list[it].couple_id)
        }
        adapter.btnDisagreeListener = {
            disagree(it, list[it].couple_id)
        }
        adapter.btnPickUpListener = {
            startActivity<AdminHelpRewardPickUpActivity>("id" to list[it].couple_id)
        }
        rv_reward.adapter = adapter
    }

    private fun loadData() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getHelpRewardList()
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

    private fun agree(position: Int, id: Int) {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().rewardApprove(id)
            if (result.code == 0) {
                toastSuccess("同意成功!")
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

    private fun disagree(position: Int, id: Int) {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().rewardReject(id)
            if (result.code == 0) {
                toastSuccess("拒绝成功!")
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_admin_help_reward_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_history) {
            startActivity<AdminHelpRewardHistoryActivity>()
        }
        return super.onOptionsItemSelected(item)
    }
}