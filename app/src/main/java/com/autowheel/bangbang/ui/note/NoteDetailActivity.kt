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

/**
 * Created by Xily on 2020/4/5.
 */
class NoteDetailActivity : BaseActivity() {
    private lateinit var markwon: Markwon
    override fun getLayoutId(): Int {
        return R.layout.activity_note_detail
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolbar()
        markwon = Markwon.builder(this)
            .usePlugin(TablePlugin.create(this))
            .usePlugin(GlideImagesPlugin.create(this))
            .build()
        markwon.setMarkdown(tv_content, "")
    }

    override fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar_title.text = "笔记详情"
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