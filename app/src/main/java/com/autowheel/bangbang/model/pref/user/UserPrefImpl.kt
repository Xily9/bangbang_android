package com.autowheel.bangbang.model.pref.user

import com.autowheel.bangbang.model.network.bean.ProfileBean


/**
 * Created by Xily on 2020/3/25.
 */
object UserPrefImpl : IUserPref {
    override var token: String by UserPrefHelper("")
    override var verify: Boolean by UserPrefHelper(false)
    override var profile: ProfileBean by UserPrefHelper(ProfileBean(0, "", "", "", ""))
    override fun clearUserPref() {
        UserPrefHelper.clear()
    }
}