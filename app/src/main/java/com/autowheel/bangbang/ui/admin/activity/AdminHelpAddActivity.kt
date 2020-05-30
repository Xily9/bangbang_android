package com.autowheel.bangbang.ui.admin.activity

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.utils.text
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastSuccess
import kotlinx.android.synthetic.main.activity_admin_helper_add.*

/**
 * Created by Xily on 2020/5/29.
 */
class AdminHelpAddActivity : BackBaseActivity() {
    override fun getToolbarTitle(): String {
        return "添加帮扶对象"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_admin_helper_add
    }

    override fun initViews(savedInstanceState: Bundle?) {
        btn_add.setOnClickListener {
            addHelper()
        }
    }

    private fun addHelper() {
        val userId = et_new_helper.text().toIntOrNull() ?: 0
        val course = et_help_subject.text()
        if (userId <= 0) {
            toastError("请输入正确的用户id")
        } else if (course.isBlank()) {
            toastError("请输入课程名")
        } else {
            launch(tryBlock = {
                val result = RetrofitHelper.getApiService().addHelper(userId, course)
                if (result.code == 0) {
                    toastSuccess("添加成功!")
                    finish()
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
}