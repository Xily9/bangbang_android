package com.autowheel.bangbang.ui.user.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.CoachCommentBean
import com.autowheel.bangbang.ui.index.activity.EvaluateActivity
import com.autowheel.bangbang.ui.user.adapter.UserEvaluateAdapter
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastInfo
import kotlinx.android.synthetic.main.activity_user_evaluate.*

/**
 * Created by Xily on 2020/5/4.
 */
class UserEvaluateActivity : BackBaseActivity() {
    private var list = arrayListOf<CoachCommentBean>()
    private lateinit var adapter: UserEvaluateAdapter
    override fun getToolbarTitle(): String {
        return "我的评价"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_evaluate
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
        rv_evaluate.layoutManager = LinearLayoutManager(this)
        adapter = UserEvaluateAdapter(list)
        adapter.btnListener = {
            startActivity<EvaluateActivity>("id" to list[it].help_id)
        }
        rv_evaluate.adapter = adapter
    }

    private fun loadData() {
        swipe_refresh_layout.isRefreshing = true
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getCommentHistory()
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
        }, finallyBlock = {
            swipe_refresh_layout.isRefreshing = false
        })
    }
}