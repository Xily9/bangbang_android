package com.autowheel.bangbang.model.network.bean

data class CoachBookListBean(
    var book_userlist: List<BookUserlist>,
    var help_id: Int, // 1
    var name: String,
    var selectIndex: Int = -1
) {
    data class BookUserlist(
        var nickname: String, // aaa
        var uid: Int, // 1,
        var select: Boolean = false
    )
}