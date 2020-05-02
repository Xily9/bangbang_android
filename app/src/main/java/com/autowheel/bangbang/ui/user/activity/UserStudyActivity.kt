package com.autowheel.bangbang.ui.user.activity

import android.app.ProgressDialog
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.GradeBean
import com.autowheel.bangbang.ui.user.adapter.UserStudyAdapter
import com.autowheel.bangbang.utils.toastError
import kotlinx.android.synthetic.main.activity_user_study.*

/**
 * Created by Xily on 2020/4/25.
 */
class UserStudyActivity : BackBaseActivity() {
    private lateinit var adapter: UserStudyAdapter
    private val list = arrayListOf<GradeBean>()
    override fun getToolbarTitle(): String {
        return "学业状况"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_study
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        rv_grade.layoutManager = LinearLayoutManager(this)
        adapter = UserStudyAdapter(list)
        rv_grade.adapter = adapter
    }

    private fun loadData() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("正在加载中,请稍候..")
        progressDialog.show()
        var a = 1
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getGrade()
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data)
                adapter.notifyDataSetChanged()
            } else {
                toastError("加载失败")
            }
        }, catchBlock = {
            toastError("加载失败")
        }, finallyBlock = {
            progressDialog.dismiss()
        })
    }
}