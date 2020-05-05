package com.autowheel.bangbang.model.network.bean

data class CoachCommentBean(
    var date: String,
    var help_id: Int, // 1
    var name: String,
    var publisher_id: Int,
    var publisher_nickname: String,
    var text: String
)