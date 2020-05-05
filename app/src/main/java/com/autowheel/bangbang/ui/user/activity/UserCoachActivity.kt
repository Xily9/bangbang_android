package com.autowheel.bangbang.ui.user.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.CoachBean
import com.autowheel.bangbang.ui.user.adapter.UserCoachAdapter
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastInfo
import com.autowheel.bangbang.utils.toastSuccess
import kotlinx.android.synthetic.main.activity_user_coach.*

/**
 * Created by Xily on 2020/5/2.
 */
class UserCoachActivity : BackBaseActivity() {
    private lateinit var adapter: UserCoachAdapter
    private var list = arrayListOf<CoachBean>()
    override fun getToolbarTitle(): String {
        return "我发布的辅导"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_coach
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
        rv_coach.layoutManager = LinearLayoutManager(this)
        adapter = UserCoachAdapter(list)
        adapter.btnEditListener = {

        }
        adapter.btnRemoveListener = { position ->
            AlertDialog.Builder(this)
                .setTitle("下架")
                .setMessage("确定要下架吗?下架之后不可再次恢复上架!")
                .setPositiveButton("确定") { dialog, which ->
                    removeCoach(position, list[position].help_id)
                }
                .setNegativeButton("取消", null)
                .show()
        }
        rv_coach.adapter = adapter
    }

    private fun loadData() {
        swipe_refresh_layout.isRefreshing = true
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getReleasedCoach()
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

    private fun removeCoach(position: Int, id: Int) {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().removeCoach(id)
            if (result.code == 0) {
                toastSuccess("下架成功!")
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