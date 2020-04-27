package com.autowheel.bangbang.ui.user.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.os.CountDownTimer
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastSuccess
import kotlinx.android.synthetic.main.activity_forget_password.*

/**
 * Created by Xily on 2020/4/26.
 */
class ForgetPasswordActivity : BackBaseActivity() {
    override fun getToolbarTitle(): String {
        return "忘记密码"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_forget_password
    }

    override fun initViews(savedInstanceState: Bundle?) {
        btn_gain.setOnClickListener {
            sendVerifyCode()
        }
        btn_submit.setOnClickListener {
            submit()
        }
    }

    private fun sendVerifyCode() {
        val email = et_email.text.toString()
        if (email.isEmpty()) {
            toastError("email不能为空")
        } else {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("发送中...")
            progressDialog.show()
            launch(tryBlock = {
                val result = RetrofitHelper.getApiService().forgetPasswordSend(email)
                if (result.code == 0) {
                    toastSuccess("验证码发送成功!")
                    btn_gain.setBackgroundResource(R.drawable.bg_btn_disable)
                    object : CountDownTimer(60 * 1000, 1000) {
                        override fun onFinish() {
                            btn_gain.setBackgroundResource(R.drawable.bg_btn)
                            btn_gain.text = "获取验证码"
                        }

                        override fun onTick(millisUntilFinished: Long) {
                            btn_gain.text = "${millisUntilFinished / 1000}s"
                        }
                    }.start()
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

    private fun submit() {
        val email = et_email.text.toString()
        val verifyCode = et_verification_code.text.toString()
        val newPassword = et_new_password.text.toString()
        val newPasswordAgain = et_password_again.text.toString()
        if (email.isEmpty()) {
            toastError("email不能为空")
        } else if (verifyCode.isEmpty()) {
            toastError("验证码不能为空")
        } else if (newPassword != newPasswordAgain) {
            toastError("两次输入的密码不一致")
        } else {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("提交中...")
            progressDialog.show()
            launch(tryBlock = {
                val result =
                    RetrofitHelper.getApiService().forgetPassword(email, newPassword, verifyCode)
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