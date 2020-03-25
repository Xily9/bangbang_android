package com.autowheel.bangbang.model.pref.user


/**
 * Created by Xily on 2020/3/25.
 */
object UserPrefImpl : IUserPref {
    override var token: String by UserPrefHelper("")
    override fun clearUserPref() {
        UserPrefHelper.clear()
    }
}