package com.autowheel.bangbang.model.network.bean


data class MessageBean(
    var from_user_id: Int, // 0
    var from_nickname: String,
    var to_user_id: Int,
    var date: Int, // 1234567890
    var content: String // test
) {
    var id = 0 // 2
}