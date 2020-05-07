package com.autowheel.bangbang.model.network.bean

data class NoteBean(
    var compliments: Int, // 0
    var content: String,
    var note_date: String,
    var note_id: Int, // 1
    var publisher_id: Int,
    var publisher_nickname: String,
    var tag: String,
    var title: String
)