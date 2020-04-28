package com.autowheel.bangbang.ui.note

import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.utils.UserUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import kotlinx.android.synthetic.main.activity_note_preview.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Xily on 2020/4/5.
 */
class NotePreviewActivity : BackBaseActivity() {
    private lateinit var markwon: Markwon
    override fun getToolbarTitle(): String {
        return "笔记预览"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_note_preview
    }

    override fun initViews(savedInstanceState: Bundle?) {
        val content = intent.getStringExtra("content") ?: ""
        val title = intent.getStringExtra("title") ?: ""
        tv_title.text = title
        tv_date.text = SimpleDateFormat("YYYY年MM月dd日", Locale.CHINA).format(Date())
        tv_nickname.text = UserUtil.profile.nickname
        Glide.with(this).load("$BASE_URL/user/avatar/${UserUtil.profile.uid}")
            .signature(ObjectKey(UserUtil.avatarUpdateTime))
            .error(R.mipmap.ic_launcher_round).into(iv_avatar)
        markwon = Markwon.builder(this)
            .usePlugin(TablePlugin.create(this))
            .usePlugin(GlideImagesPlugin.create(this))
            .build()
        markwon.setMarkdown(tv_content, content)
        tv_content.movementMethod = LinkMovementMethod.getInstance()
    }
}