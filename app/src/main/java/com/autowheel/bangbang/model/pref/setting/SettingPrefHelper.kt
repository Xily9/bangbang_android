package com.autowheel.bangbang.model.pref.setting

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.autowheel.bangbang.MyApplication
import com.autowheel.bangbang.model.pref.PrefHelper

/**
 * Created by Xily on 2020/3/25.
 */
class SettingPrefHelper<T>(default: T) : PrefHelper<T>(default) {
    override fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }

    companion object {
        private val sharedPreferences by lazy {
            PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance())
        }
    }

}