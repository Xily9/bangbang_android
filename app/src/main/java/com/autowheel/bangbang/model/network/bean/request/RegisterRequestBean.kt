package com.autowheel.bangbang.model.network.bean.request

data class RegisterRequestBean(
    var email: String,
    var nickname: String,
    var password: String,
    var username: String
)