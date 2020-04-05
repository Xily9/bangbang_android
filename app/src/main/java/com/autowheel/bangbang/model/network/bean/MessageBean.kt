package com.autowheel.bangbang.model.network.bean


data class MessageBean(
    var from: Int, // 0
    var fromNickname: String,
    var to: Int,
    var timestamp: Int, // 1234567890
    var message: String, // test
    var associatedItemId: Int = 0
) {
    var id = 0 // 2
}