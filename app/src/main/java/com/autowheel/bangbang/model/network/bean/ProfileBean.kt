package com.autowheel.bangbang.model.network.bean

import java.io.Serializable

data class ProfileBean(
    var uid: Int,
    var email: String,
    var nickname: String,
    var signature: String,
    var username: String
) : Serializable