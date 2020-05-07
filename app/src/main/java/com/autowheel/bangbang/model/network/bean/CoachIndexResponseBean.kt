package com.autowheel.bangbang.model.network.bean

data class CoachIndexResponseBean(
    var code: Int, // 0
    var `data`: List<CoachBean>,
    var length: Int, // 1234
    var msg: String // 返回成功
)