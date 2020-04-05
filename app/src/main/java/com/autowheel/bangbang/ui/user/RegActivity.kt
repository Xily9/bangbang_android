package com.autowheel.bangbang.ui.user

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.utils.putExtras
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastSuccess
import kotlinx.android.synthetic.main.activity_reg.*

/**
 * Created by Xily on 2020/4/3.
 */
class RegActivity : BackBaseActivity() {
    override fun getToolbarTitle(): String {
        return "注册"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_reg
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolbar()
        btn_reg.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val username = et_username.text.toString()
        val password = et_password.text.toString()
        val passwordAgain = et_password_again.text.toString()
        val email = et_email.text.toString()
        val nickname = et_nickname.text.toString()
        if (username.isEmpty() || password.isEmpty() || passwordAgain.isEmpty() || email.isEmpty() || nickname.isEmpty()) {
            toastError("所有项目均为必填项")
        } else if (password != passwordAgain) {
            toastError("两次输入的密码不一致")
        } else {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("注册中...")
            progressDialog.show()
            launch(tryBlock = {
                val result =
                    RetrofitHelper.getApiService().register(username, password, email, nickname)
                if (result.code == 0) {
                    toastSuccess("注册成功")
                    setResult(
                        Activity.RESULT_OK,
                        Intent().putExtras("username" to username, "password" to password)
                    )
                    finish()
                } else {
                    toastError(result.msg)
                }
            }, catchBlock = {
                it.printStackTrace()
                toastError("网络请求出错")
            }, finallyBlock = {
                progressDialog.dismiss()
            })
        }
    }
}