package com.autowheel.bangbang.model.network.bean

data class NoteIndexResponseBean(
    var code: Int, // 0
    var `data`: List<NoteBean>,
    var length: Int, // 1234
    var msg: String // 返回成功
)