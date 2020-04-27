package com.autowheel.bangbang.ui.user.activity

import android.app.ProgressDialog
import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastSuccess
import kotlinx.android.synthetic.main.activity_change_password.*

/**
 * Created by Xily on 2020/4/25.
 */
class ChangePasswordActivity : BackBaseActivity() {
    override fun getToolbarTitle(): String {
        return "修改密码"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_change_password
    }

    override fun initViews(savedInstanceState: Bundle?) {
        btn_submit.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val oldPassword = et_old_password.text.toString()
        val newPassword = et_new_password.text.toString()
        val newPasswordAgain = et_password_again.text.toString()
        if (oldPassword.isEmpty()) {
            toastError("原密码为空")
        } else if (newPassword != newPasswordAgain) {
            toastError("两次输入的密码不一致")
        } else {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("提交中...")
            progressDialog.show()
            launch(tryBlock = {
                val result = RetrofitHelper.getApiService().changePassword(oldPassword, newPassword)
                if (result.code == 0) {
                    toastSuccess("密码修改成功!")
                    finish()
                } else {
                    toastError(result.msg)
                }
            }, catchBlock = {
                toastError("网络请求出错!")
            }, finallyBlock = {
                progressDialog.dismiss()
            })

        }
    }
}