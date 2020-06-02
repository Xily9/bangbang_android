package com.autowheel.bangbang.ui.index.activity

import android.app.ProgressDialog
import android.os.Bundle
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.GradeBean
import com.autowheel.bangbang.utils.UserUtil
import com.autowheel.bangbang.utils.text
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastSuccess
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.activity_help_apply.*

/**
 * Created by Xily on 2020/5/25.
 */
class HelpApplyActivity : BackBaseActivity() {
    private var list = arrayListOf<GradeBean>()
    private var userId = 0
    override fun getToolbarTitle(): String {
        return "申请帮扶"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_help_apply
    }

    override fun initViews(savedInstanceState: Bundle?) {
        tv_name.text = intent.getStringExtra("course")
        tv_nickname.text = intent.getStringExtra("user_nickname")
        userId = intent.getIntExtra("user_id", 0)
        Glide.with(this).load("$BASE_URL/user/avatar/${userId}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher).into(iv_avatar)
        loadGrade()
        btn_publish.setOnClickListener {
            apply()
        }
    }

    private fun loadGrade() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("正在加载中,请稍候..")
        progressDialog.show()
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getGrade()
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data.filter {
                    (it.point.toDoubleOrNull() ?: 0.0) >= 90 || it.point == "优秀"
                })
                spinner.attachDataSource<String>(list.map { it.name })
            } else {
                toastError("加载失败")
            }
        }, catchBlock = {
            toastError("加载失败")
        }, finallyBlock = {
            progressDialog.dismiss()
        })
    }

    private fun apply() {
        launch(tryBlock = {
            if (list.isEmpty()) {
                toastError("课程加载不成功或没有可关联课程，请重新进入后再试")
                return@launch
            } else {
                val gradeBean = list[spinner.selectedIndex]
                val result = RetrofitHelper.getApiService().applyHelp(
                    userId, gradeBean.name,
                    gradeBean.point,
                    gradeBean.token,
                    et_note.text()
                )
                if (result.code == 0) {
                    toastSuccess("申请成功,正在等待审核")
                    finish()
                } else {
                    toastError(result.msg)
                }
            }
        }, catchBlock = {
            it.printStackTrace()
            toastError("网络请求出错!")
        })
    }
}