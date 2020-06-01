package com.autowheel.bangbang.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.autowheel.bangbang.service.MessageService
import com.autowheel.bangbang.utils.UserUtil
import com.autowheel.bangbang.utils.setDarkStatusIcon
import com.autowheel.bangbang.utils.startService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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
        if (UserUtil.isLogin && !UserUtil.isAdmin)
            startService<MessageService>()
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

    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) =
        lifecycleScope.launch(context, start, block)

    fun launch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: (suspend CoroutineScope.(Throwable) -> Unit) = {},
        finallyBlock: (suspend CoroutineScope.() -> Unit) = {}
    ) {
        lifecycleScope.launch {
            try {
                tryBlock()
            } catch (e: CancellationException) {
            } catch (e: Exception) {
                catchBlock(e)
            } finally {
                finallyBlock()
            }
        }
    }
}