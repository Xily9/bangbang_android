package com.autowheel.bangbang.ui.note.activity

import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.utils.UserUtil
import com.autowheel.bangbang.utils.toastError
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import kotlinx.android.synthetic.main.activity_note_detail.*

/**
 * Created by Xily on 2020/4/5.
 */
class NoteDetailActivity : BackBaseActivity() {
    private lateinit var markwon: Markwon
    private var id = 0
    override fun getToolbarTitle(): String {
        return "笔记详情"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_note_detail
    }

    override fun initViews(savedInstanceState: Bundle?) {
        id = intent.getIntExtra("id", 0)
        markwon = Markwon.builder(this)
            .usePlugin(TablePlugin.create(this))
            .usePlugin(GlideImagesPlugin.create(this))
            .build()
        tv_content.movementMethod = LinkMovementMethod.getInstance()
        loadData()
    }

    private fun loadData() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getNoteDetail(id)
            if (result.code == 0) {
                tv_title.text = result.data.title
                tv_tag.text = result.data.tag
                markwon.setMarkdown(tv_content, result.data.content)
                loadUserProfile(result.data.publisher_id)
            } else {
                toastError(result.msg)
            }
        }, catchBlock = {
            it.printStackTrace()
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
                Glide.with(this@NoteDetailActivity).load("$BASE_URL/user/avatar/${data.uid}")
                    .signature(ObjectKey(UserUtil.avatarUpdateTime))
                    .error(R.mipmap.ic_launcher_round).into(iv_avatar)
            }
        }, catchBlock = {

        })
    }
}