package com.autowheel.bangbang.ui.note.activity

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.MyApplication
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
    private var isGood = false
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
            .usePlugin(GlideImagesPlugin.create(MyApplication.getInstance()))
            .build()
        tv_content.movementMethod = LinkMovementMethod.getInstance()
        layout_good.setOnClickListener {
            if (isGood) {
                cancelGood()
            } else {
                good()
            }
        }
        loadData()
    }

    private fun initGoodIcon() {
        if (isGood) {
            iv_good.setImageResource(R.drawable.ic_good_fill_2)
            iv_good.setColorFilter(resources.getColor(R.color.blue))
        } else {
            iv_good.setImageResource(R.drawable.ic_good_2)
            iv_good.setColorFilter(Color.parseColor("#777777"))
        }
    }

    private fun loadData() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getNoteDetail(id)
            if (result.code == 0) {
                tv_title.text = result.data.title
                tv_tag.text = result.data.tag
                markwon.setMarkdown(tv_content, result.data.content)
                tv_good_number.text = result.data.compliments.toString()
                isGood = result.data.flag
                initGoodIcon()
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
                    .error(R.mipmap.ic_launcher).into(iv_avatar)
            }
        }, catchBlock = {

        })
    }

    private fun good() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().complimentNote(id)
            if (result.code == 0) {
                isGood = true
                tv_good_number.text = (tv_good_number.text.toString().toInt() + 1).toString()
                initGoodIcon()
            } else {
                toastError(result.msg)
            }
        }, catchBlock = {
            it.printStackTrace()
            toastError("网络请求出错!")
        }, finallyBlock = {

        })
    }

    private fun cancelGood() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().reComplimentNote(id)
            if (result.code == 0) {
                isGood = false
                tv_good_number.text = (tv_good_number.text.toString().toInt() - 1).toString()
                initGoodIcon()
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