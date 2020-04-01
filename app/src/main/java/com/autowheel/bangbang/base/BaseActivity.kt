package com.autowheel.bangbang.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.autowheel.bangbang.utils.setDarkStatusIcon

/**
 * Created by Xily on 2020/3/5.
 */
abstract class BaseActivity : AppCompatActivity() {
    /**
     * 设置布局layout
     *
     * @return
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置布局内容
        setContentView(getLayoutId())
        setDarkStatusIcon(true)
        title = ""
        //初始化控件
        initViews(savedInstanceState)
    }

    /**
     * 初始化views
     *
     * @param savedInstanceState
     */
    abstract fun initViews(savedInstanceState: Bundle?)

    /**
     * 初始化toolbar
     */
    open fun initToolbar() {}

}