package com.autowheel.bangbang.ui.user.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.UserHelpBean
import com.autowheel.bangbang.ui.index.activity.HelpPickActivity
import com.autowheel.bangbang.ui.msg.activity.ChatActivity
import com.autowheel.bangbang.ui.user.adapter.UserHelpAdapter
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastInfo
import com.autowheel.bangbang.utils.toastSuccess
import kotlinx.android.synthetic.main.activity_user_help.*

/**
 * Created by Xily on 2020/5/25.
 */
class UserHelpActivity : BackBaseActivity() {
    private lateinit var adapter: UserHelpAdapter
    private val list = arrayListOf<UserHelpBean>()
    override fun getToolbarTitle(): String {
        return "我的帮扶"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_help
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
        rv_help.layoutManager = LinearLayoutManager(this)
        adapter = UserHelpAdapter(list)
        adapter.btnApplyListener = {
            val data = list[it]
            startActivity<HelpPickActivity>("id" to data.couple_id)
        }
        adapter.btnPickListener = {
            val data = list[it]
            rewardHelp(it, data.couple_id)
        }
        adapter.btnChatListener = {
            val data = list[it]
            startActivity<ChatActivity>("id" to data.assisted_id)
        }
        rv_help.adapter = adapter
    }

    private fun loadData() {
        swipe_refresh_layout.isRefreshing = true
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getUserHelp()
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data)
                adapter.notifyDataSetChanged()
                if (list.isEmpty()) {
                    toastInfo("空空如也")
                }
            } else {
                toastError("加载失败")
            }
        }, catchBlock = {
            toastError("加载失败")
        }, finallyBlock = {
            swipe_refresh_layout.isRefreshing = false
        })
    }

    private fun rewardHelp(position: Int, id: Int) {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().rewardHelp(id)
            if (result.code == 0) {
                toastSuccess("申请成功!")
                list.removeAt(position)
                adapter.notifyDataSetChanged()
            } else {
                toastError(result.msg)
            }
        }, catchBlock = {
            it.printStackTrace()
            toastError("网络请求出错!")
        })
    }
}