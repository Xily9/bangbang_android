package com.autowheel.bangbang.model.network.bean

data class HelpApproveListBean(
    var assistants: List<Assistant>,
    var assisted_course: String,
    var assisted_id: Int, // 0
    var assisted_nickname: String,
    var selectIndex: Int
) {
    data class Assistant(
        var assistant_id: Int, // 0
        var assistant_nickname: String,
        var complement: String,
        var couple_id: Int, // 0
        var course: String,
        var grade: String,
        var select: Boolean
    )
}