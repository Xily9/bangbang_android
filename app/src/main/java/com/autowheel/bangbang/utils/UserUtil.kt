package com.autowheel.bangbang.utils

import com.autowheel.bangbang.model.DataManager

/**
 * Created by Xily on 2020/4/6.
 */
object UserUtil {
    val isLogin
        get() = DataManager.token.isNotEmpty() && DataManager.verify
    var isVerify = DataManager.verify
        set(value) {
            DataManager.verify = value
            field = value
        }
    var isAdmin = DataManager.admin
        set(value) {
            DataManager.admin = value
            field = value
        }
    var avatarUpdateTime = System.currentTimeMillis().toString()
    var token = DataManager.token
        set(value) {
            DataManager.token = value
            field = value
        }
    var profile = DataManager.profile
        set(value) {
            DataManager.profile = profile
            field = value
        }

}