package com.autowheel.bangbang.model.network.bean

data class CoachBean(
    var course_score: String,
    var declaration: String,
    var is_pay: Boolean,
    var help_id: Int,
    var name: String,
    var price: Double, // 1.1
    var publisher_id: Int, // 1
    var publisher_nickname: String,//发布人昵称
    var release_time: String,
    var type: String // course
)