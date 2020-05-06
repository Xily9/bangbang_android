package com.autowheel.bangbang.model.network.bean

data class NoteBean(
    var content: String,
    var publisher_id: Int,
    var publisher_nickname: String,
    var tag: String,
    var title: String
)