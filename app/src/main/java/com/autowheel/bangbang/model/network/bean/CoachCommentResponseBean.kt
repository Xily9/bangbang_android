package com.autowheel.bangbang.model.network.bean

data class CoachCommentResponseBean(
    var code: Int, // 0
    var `data`: List<CoachCommentBean>,
    var length: Int, // 0
    var msg: String // 返回成功
)