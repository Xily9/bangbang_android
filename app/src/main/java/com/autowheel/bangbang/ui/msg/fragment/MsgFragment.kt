package com.autowheel.bangbang.ui.msg.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseFragment
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.MessageListBean
import com.autowheel.bangbang.ui.msg.activity.ChatActivity
import com.autowheel.bangbang.ui.msg.adapter.MsgAdapter
import com.autowheel.bangbang.ui.user.activity.OrderActivity
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.toastError
import kotlinx.android.synthetic.main.fragment_msg.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * Created by Xily on 2020/3/25.
 */
class MsgFragment : BaseFragment() {
    private lateinit var adapter: MsgAdapter
    private var list = arrayListOf<MessageListBean>()
    override fun getLayoutId(): Int {
        return R.layout.fragment_msg
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolbar()
        initRecyclerView()
        swipe_refresh_layout.setColorSchemeResources(R.color.blue)
        swipe_refresh_layout.setOnRefreshListener {
            //startActivity<ChatActivity>("id" to 1)
            loadData()
        }
        loadData()
    }

    private fun initRecyclerView() {
        rv_msg.layoutManager = LinearLayoutManager(requireContext())
        adapter = MsgAdapter(list)
        adapter.setOnItemClickListener {
            val data = list[it]
            if (data.user_id == 0) {
                startActivity<OrderActivity>()
            } else {
                startActivity<ChatActivity>("id" to data.user_id)
            }
        }
        rv_msg.adapter = adapter
    }

    override fun initToolbar() {
        toolbar_title.text = "消息"
    }

    private fun loadData() {
        swipe_refresh_layout.isRefreshing = true
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getMsgList()
            if (result.code == 0) {
                list.clear()
                list.add(
                    MessageListBean(
                        (System.currentTimeMillis() / 1000).toInt(),
                        "及时处理预约信息",
                        0,
                        "待处理请求"
                    )
                )
                list.addAll(result.data)
                adapter.notifyDataSetChanged()
            } else {
                toastError(result.msg)
            }
        }, catchBlock = {
            toastError("网络请求出错!")
        })
        adapter.notifyDataSetChanged()
        swipe_refresh_layout.isRefreshing = false
    }

}