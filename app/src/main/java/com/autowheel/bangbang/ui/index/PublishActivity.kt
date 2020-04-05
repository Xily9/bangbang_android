package com.autowheel.bangbang.ui.index

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.utils.gone
import com.autowheel.bangbang.utils.visible
import kotlinx.android.synthetic.main.activity_publish.*

/**
 * Created by Xily on 2020/4/5.
 */
class PublishActivity : BackBaseActivity() {
    private var isCourse = true

    override fun getToolbarTitle(): String {
        return "发布辅导"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_publish
    }

    override fun initViews(savedInstanceState: Bundle?) {
        rg_type.setOnCheckedChangeListener { group, checkedId ->
            isCourse = checkedId == R.id.rb_course
            initLayout()
        }
        initLayout()
        spinner.attachDataSource<String>(listOf("高级语言程序设计", "汇编语言程序设计", "Java程序设计"))
    }

    private fun initLayout() {
        if (isCourse) {
            layout_course.visible()
            layout_skill.gone()
        } else {
            layout_skill.visible()
            layout_course.gone()
        }
    }

}