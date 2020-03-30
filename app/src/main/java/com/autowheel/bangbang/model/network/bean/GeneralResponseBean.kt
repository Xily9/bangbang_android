package com.autowheel.bangbang.model.network.bean

data class GeneralResponseBean<T>(
    var code: Int, // 0
    var `data`: T,
    var msg: String // 登录成功
)