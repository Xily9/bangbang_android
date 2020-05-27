package com.autowheel.bangbang.ui.index.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.HelpBean
import com.autowheel.bangbang.ui.index.adapter.HelpAdapter
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastInfo
import kotlinx.android.synthetic.main.activity_help.*

/**
 * Created by Xily on 2020/5/25.
 */
class HelpActivity : BackBaseActivity() {
    private lateinit var adapter: HelpAdapter
    private var list = arrayListOf<HelpBean>()
    override fun getToolbarTitle(): String {
        return "帮扶活动"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_help
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        rv_help.layoutManager = LinearLayoutManager(this)
        adapter = HelpAdapter(list)
        rv_help.adapter = adapter
        adapter.applyListener = {
            val data = list[it]
            startActivity<HelpApplyActivity>(
                "user_id" to data.user_id,
                "user_nickname" to data.user_nickname,
                "course" to data.course
            )
        }
    }

    private fun loadData() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getHelpList()
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data)
                adapter.notifyDataSetChanged()
                if (list.isEmpty()) {
                    toastInfo("空空如也")
                }
            } else {
                toastError(result.msg)
            }
        }, catchBlock = {
            it.printStackTrace()
            toastError("网络请求出错!")
        })
    }
}