package com.autowheel.bangbang.model.pref.user

import android.content.Context
import android.content.SharedPreferences
import com.autowheel.bangbang.MyApplication
import com.autowheel.bangbang.model.pref.PrefHelper

/**
 * Created by Xily on 2020/3/25.
 */
class UserPrefHelper<T>(default: T) : PrefHelper<T>(default) {
    override fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }

    companion object {
        private val sharedPreferences by lazy {
            MyApplication.getInstance().getSharedPreferences("user", Context.MODE_PRIVATE)
        }

        fun clear() {
            sharedPreferences.edit().clear().apply()
        }
    }

}