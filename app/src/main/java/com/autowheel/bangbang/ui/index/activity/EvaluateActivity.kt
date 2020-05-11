package com.autowheel.bangbang.ui.index.activity

import android.os.Bundle
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.utils.UserUtil
import com.autowheel.bangbang.utils.text
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastSuccess
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.activity_evaluate.*

/**
 * Created by Xily on 2020/5/4.
 */
class EvaluateActivity : BackBaseActivity() {
    private var id = 0
    override fun getToolbarTitle(): String {
        return "发表评价"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_evaluate
    }

    override fun initViews(savedInstanceState: Bundle?) {
        id = intent.getIntExtra("id", 0)
        btn_evaluate.setOnClickListener {
            evaluate()
        }
        loadData()
    }

    private fun loadData() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getCoachDetail(id)
            if (result.code == 0) {
                val data = result.data
                loadUserProfile(data.publisher_id)
                if (data.type == "course")
                    tv_detail.text = "${data.name}(成绩${data.course_score})"
                else
                    tv_detail.text = "${data.name}"
            }
        }, catchBlock = {
            toastError("网络请求出错!")
        }, finallyBlock = {

        })
    }

    private fun loadUserProfile(uid: Int) {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getProfile(uid)
            if (result.code == 0) {
                val data = result.data
                tv_name.text = data.nickname
                Glide.with(this@EvaluateActivity).load("$BASE_URL/user/avatar/${data.uid}")
                    .signature(ObjectKey(UserUtil.avatarUpdateTime))
                    .error(R.mipmap.ic_launcher_round).into(iv_avatar)
            }
        }, catchBlock = {

        })
    }

    private fun evaluate() {
        val text = et_evaluation.text()
        if (text.isEmpty()) {
            toastError("评价内容不能为空！")
        } else {
            launch(tryBlock = {
                val result = RetrofitHelper.getApiService().commentCoach(id, ratingBar.rating, text)
                if (result.code == 0) {
                    toastSuccess("评价成功！")
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