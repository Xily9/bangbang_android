package com.autowheel.bangbang.model.db

import com.autowheel.bangbang.model.db.bean.SearchHistoryBean
import org.litepal.LitePal
import org.litepal.extension.delete
import org.litepal.extension.deleteAll
import org.litepal.extension.find
import org.litepal.extension.findFirst

/**
 * Created by Xily on 2020/4/2.
 */
object LitePalImpl : IDB {
    override fun clearSearchHistory() {
        LitePal.deleteAll<SearchHistoryBean>()
    }

    override fun getSearchHistory(): List<String> {
        return LitePal.order("id desc").find<SearchHistoryBean>().map {
            it.word
        }
    }

    override fun addSearchHistory(string: String) {
        LitePal.where("word=?", string).findFirst<SearchHistoryBean>()?.apply {
            LitePal.delete<SearchHistoryBean>(id.toLong())
        }
        SearchHistoryBean(string).save()
    }
}