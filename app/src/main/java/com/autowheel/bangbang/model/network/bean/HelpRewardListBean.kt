package com.autowheel.bangbang.model.network.bean

data class HelpRewardListBean(
    var assistant_id: Int, // 0
    var assistant_nickname: String,
    var assisted_id: Int, // 0
    var assisted_nickname: String,
    var complement: String,
    var couple_id: Int, // 0
    var course: String,
    var days: Int // 0
)