package com.autowheel.bangbang.ui.index.activity

import android.os.Bundle
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.ui.msg.ChatActivity
import com.autowheel.bangbang.utils.UserUtil
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.toastError
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * Created by Xily on 2020/4/28.
 */
class DetailActivity : BackBaseActivity() {
    private var id = 0
    override fun getToolbarTitle(): String {
        return "辅导详情"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }

    override fun initViews(savedInstanceState: Bundle?) {
        id = intent.getIntExtra("id", 0)
        btn_order.setOnClickListener {

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
                    tv_skill.text = "${data.name}(成绩${data.course_score})"
                else
                    tv_skill.text = "${data.name}"
                tv_declaration.text = data.declaration
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
                tv_nickname.text = data.nickname
                tv_signature.text = data.signature
                Glide.with(this@DetailActivity).load("$BASE_URL/user/avatar/${data.uid}")
                    .signature(ObjectKey(UserUtil.avatarUpdateTime))
                    .error(R.mipmap.ic_launcher_round).into(iv_avatar)
                layout_chat.setOnClickListener {
                    startActivity<ChatActivity>("id" to uid, "itemId" to id)
                }
            }
        }, catchBlock = {

        })
    }

    private fun loadComment() {

    }
}