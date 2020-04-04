package com.autowheel.bangbang.model.db.bean

import org.litepal.crud.LitePalSupport

data class SearchHistoryBean(var word: String) : LitePalSupport() {
    var id: Int = 0
}