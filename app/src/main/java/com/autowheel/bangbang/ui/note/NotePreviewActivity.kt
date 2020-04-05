package com.autowheel.bangbang.ui.note

import android.os.Bundle
import android.view.MenuItem
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseActivity
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import kotlinx.android.synthetic.main.activity_note_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Xily on 2020/4/5.
 */
class NotePreviewActivity : BaseActivity() {
    private lateinit var markwon: Markwon
    override fun getLayoutId(): Int {
        return R.layout.activity_note_preview
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolbar()
        val content = intent.getStringExtra("content") ?: ""
        val title = intent.getStringExtra("title") ?: ""
        tv_title.text = title
        tv_date.text = SimpleDateFormat("YYYY年MM月dd日", Locale.CHINA).format(Date())
        markwon = Markwon.builder(this)
            .usePlugin(TablePlugin.create(this))
            .usePlugin(GlideImagesPlugin.create(this))
            .build()
        markwon.setMarkdown(tv_content, content)
    }

    override fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar_title.text = "笔记预览"
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish() // back button
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}