package com.autowheel.bangbang.model.network.bean

data class CoachBean(
    var course_score: String,
    var declaration: String,
    var name: String,
    var price: Double, // 1.1
    var publisher_id: Int, // 1
    var publisher_nickname: String,//发布人昵称
    var type: String // course
)