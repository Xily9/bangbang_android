package com.autowheel.bangbang.ui.msg.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseFragment
import com.autowheel.bangbang.model.network.bean.MessageBean
import com.autowheel.bangbang.ui.msg.activity.ChatActivity
import com.autowheel.bangbang.ui.msg.adapter.MsgAdapter
import com.autowheel.bangbang.ui.user.activity.OrderActivity
import com.autowheel.bangbang.utils.startActivity
import kotlinx.android.synthetic.main.fragment_msg.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * Created by Xily on 2020/3/25.
 */
class MsgFragment : BaseFragment() {
    private lateinit var adapter: MsgAdapter
    private var list = arrayListOf<MessageBean>()
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
            if (data.id == 0) {
                startActivity<OrderActivity>()
            } else {
                startActivity<ChatActivity>("id" to data.from)
            }
        }
        rv_msg.adapter = adapter
    }

    override fun initToolbar() {
        toolbar_title.text = "消息"
    }

    private fun loadData() {
        swipe_refresh_layout.isRefreshing = true
        list.clear()
        list.add(
            MessageBean(
                0,
                "待处理请求",
                0,
                (System.currentTimeMillis() / 1000).toInt(),
                "及时处理预约信息"
            )
        )
        adapter.notifyDataSetChanged()
        swipe_refresh_layout.isRefreshing = false
    }

}