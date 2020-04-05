package com.autowheel.bangbang.ui.note

import android.os.Bundle
import android.view.MenuItem
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseActivity
import com.autowheel.bangbang.utils.startActivity
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * Created by Xily on 2020/4/5.
 */
class NotePublishActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_note_publish
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolbar()
        startActivity<NoteDetailActivity>()
    }

    override fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar_title.text = "发布笔记"
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