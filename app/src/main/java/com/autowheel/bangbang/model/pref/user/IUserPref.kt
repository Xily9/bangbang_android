package com.autowheel.bangbang.model.pref.user


/**
 * Created by Xily on 2020/3/25.
 */
interface IUserPref {
    var token: String
    fun clearUserPref()
}