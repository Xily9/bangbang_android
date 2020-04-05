package com.autowheel.bangbang

import android.app.Application
import com.didichuxing.doraemonkit.DoraemonKit
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.ios.IosEmojiProvider
import org.litepal.LitePal

/**
 * Created by Xily on 2020/4/6.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        LitePal.initialize(this)
        DoraemonKit.install(this)
        EmojiManager.install(IosEmojiProvider())
    }

    companion object {
        private lateinit var instance: MyApplication
        @JvmStatic
        fun getInstance(): MyApplication {
            return instance
        }
    }
}