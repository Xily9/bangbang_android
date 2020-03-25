package com.autowheel.bangbang.model.network.bean.response

data class GeneralResponseBean<T>(
    var code: Int, // 0
    var `data`: T,
    var msg: String // 登录成功
)