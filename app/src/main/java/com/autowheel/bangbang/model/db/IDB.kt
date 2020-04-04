package com.autowheel.bangbang.model.db

/**
 * Created by Xily on 2020/4/2.
 */
interface IDB {
    fun clearSearchHistory()
    fun getSearchHistory(): List<String>
    fun addSearchHistory(string: String)
}