package com.autowheel.bangbang.model.pref.user

import com.autowheel.bangbang.model.network.bean.ProfileBean


/**
 * Created by Xily on 2020/3/25.
 */
interface IUserPref {
    var token: String
    var verify: Boolean
    var profile: ProfileBean
    fun clearUserPref()
}