package com.autowheel.bangbang.ui.note

import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import kotlinx.android.synthetic.main.activity_note_detail.*

/**
 * Created by Xily on 2020/4/5.
 */
class NoteDetailActivity : BackBaseActivity() {
    private lateinit var markwon: Markwon
    override fun getToolbarTitle(): String {
        return "笔记详情"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_note_detail
    }

    override fun initViews(savedInstanceState: Bundle?) {
        markwon = Markwon.builder(this)
            .usePlugin(TablePlugin.create(this))
            .usePlugin(GlideImagesPlugin.create(this))
            .build()
        markwon.setMarkdown(tv_content, "")
        tv_content.movementMethod = LinkMovementMethod.getInstance()
    }
}