package com.autowheel.bangbang.base

import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * Created by Xily on 2020/3/5.
 */
abstract class BackBaseActivity : BaseActivity() {

    abstract fun getToolbarTitle(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
    }

    /**
     * 初始化toolbar
     */
    override fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar_title.text = getToolbarTitle()
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