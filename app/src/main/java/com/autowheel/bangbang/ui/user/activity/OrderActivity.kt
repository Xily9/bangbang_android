package com.autowheel.bangbang.ui.user.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.bean.MessageEventBean
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.CoachBookListBean
import com.autowheel.bangbang.ui.user.adapter.OrderAdapter
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastInfo
import com.autowheel.bangbang.utils.toastSuccess
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.activity_order.*

/**
 * Created by Xily on 2020/5/2.
 */
class OrderActivity : BackBaseActivity() {
    private lateinit var adapter: OrderAdapter
    private var list = arrayListOf<CoachBookListBean>()
    override fun getToolbarTitle(): String {
        return "预约列表"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_order
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
        rv_order.layoutManager = LinearLayoutManager(this)
        adapter = OrderAdapter(list)
        adapter.listener = {
            agreeOrder(it, list[it].help_id, list[it].book_userlist[list[it].selectIndex - 1].uid)
        }
        rv_order.adapter = adapter
    }

    private fun loadData() {
        swipe_refresh_layout.isRefreshing = true
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getCoachBookList()
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data.filter { it.book_userlist.isNotEmpty() })
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

    private fun agreeOrder(position: Int, id: Int, uid: Int) {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().agreeCoach(id, uid)
            if (result.code == 0) {
                toastSuccess("同意预约成功！记得与被辅导人保持沟通哦~")
                list.removeAt(position)
                adapter.notifyDataSetChanged()
                LiveEventBus.get("messageEvent", MessageEventBean::class.java)
                    .post(MessageEventBean().apply {
                        type = 1
                        message = "我已经同意你的预约啦！"
                        chatId = uid
                    })
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