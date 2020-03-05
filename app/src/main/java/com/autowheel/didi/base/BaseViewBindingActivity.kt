package com.autowheel.didi.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.autowheel.didi.utils.setDarkStatusIcon
import com.autowheel.didi.utils.setStatusBarUpper

/**
 * Created by Xily on 2020/3/5.
 */
abstract class BaseViewBindingActivity<T : ViewBinding> : AppCompatActivity() {
    private lateinit var _viewBinding: T
    abstract fun initViewBinding(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = initViewBinding()
        //设置布局内容
        setContentView(_viewBinding.root)
        //初始化控件
        initViews(savedInstanceState)
        setDarkStatusIcon(true)
    }

    fun getViewBinding(): T = _viewBinding
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