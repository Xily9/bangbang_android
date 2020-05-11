package com.autowheel.bangbang.ui.index.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.CoachCommentBean
import com.autowheel.bangbang.ui.index.adapter.CommentAdapter
import com.autowheel.bangbang.ui.msg.activity.ChatActivity
import com.autowheel.bangbang.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * Created by Xily on 2020/4/28.
 */
class DetailActivity : BackBaseActivity() {
    private var id = 0
    private lateinit var adapter: CommentAdapter
    private var commentList = arrayListOf<CoachCommentBean>()
    override fun getToolbarTitle(): String {
        return "辅导详情"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_detail
    }

    override fun initViews(savedInstanceState: Bundle?) {
        id = intent.getIntExtra("id", 0)
        btn_order.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("预约")
                .setMessage("确定要预约吗?")
                .setPositiveButton("确定") { dialog, which ->
                    orderCoach()
                }
                .setNegativeButton("取消", null)
                .show()
        }
        initRecyclerView()
        loadData()
        loadComment()
    }

    private fun initRecyclerView() {
        rv_comment.layoutManager = LinearLayoutManager(this)
        adapter = CommentAdapter(commentList)
        rv_comment.adapter = adapter
    }

    private fun loadData() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getCoachDetail(id)
            if (result.code == 0) {
                val data = result.data
                loadUserProfile(data.publisher_id)
                loadRating(data.publisher_id)
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

    private fun loadRating(uid: Int) {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getUserStar(uid)
            if (result.code == 0) {
                ratingBar.rating = result.data.star
            }
        })
    }

    private fun loadComment() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getCoachComments(id)
            if (result.code == 0) {
                commentList.clear()
                commentList.addAll(result.data)
                tv_comment_count.text = "共${result.length}条"
                if (commentList.isEmpty()) {
                    tv_empty.visible()
                    rv_comment.gone()
                } else {
                    tv_empty.gone()
                    rv_comment.visible()
                    adapter.notifyDataSetChanged()
                }
            }
        }, catchBlock = {

        }, finallyBlock = {

        })
    }

    private fun orderCoach() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().bookCoach(id)
            if (result.code == 0) {
                toastSuccess("预约成功,请等待辅导人确认")
            } else {
                toastError(result.msg)
            }
        }, catchBlock = {
            toastError("网络请求出错!")
        })
    }
}