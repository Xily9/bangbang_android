package com.autowheel.bangbang.ui.msg

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseFragment
import com.autowheel.bangbang.utils.startActivity
import kotlinx.android.synthetic.main.fragment_msg.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * Created by Xily on 2020/3/25.
 */
class MsgFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_msg
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolbar()
        initRecyclerView()
        swipe_refresh_layout.setColorSchemeResources(R.color.blue)
        swipe_refresh_layout.setOnRefreshListener {
            startActivity<ChatActivity>("id" to 1)
            loadData()
        }
        loadData()
    }

    private fun initRecyclerView() {

    }

    override fun initToolbar() {
        toolbar_title.text = "消息"
    }

    private fun loadData() {

    }

}