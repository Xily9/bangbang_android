package com.autowheel.bangbang.ui.user.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.NoteBean
import com.autowheel.bangbang.ui.note.activity.NotePublishActivity
import com.autowheel.bangbang.ui.user.adapter.UserNoteAdapter
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastInfo
import kotlinx.android.synthetic.main.activity_user_note.*

/**
 * Created by Xily on 2020/4/26.
 */
class UserNoteActivity : BackBaseActivity() {
    private lateinit var adapter: UserNoteAdapter
    private val list = arrayListOf<NoteBean>()
    override fun getToolbarTitle(): String {
        return "我发布的笔记"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_note
    }

    override fun initViews(savedInstanceState: Bundle?) {
        swipe_refresh_layout.setColorSchemeColors(resources.getColor(R.color.blue))
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
        }
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        rv_note.layoutManager = LinearLayoutManager(this)
        adapter = UserNoteAdapter(list)
        adapter.btnEditListener = {
            startActivity<NotePublishActivity>("data" to list[it])
        }
        adapter.btnApplyListener = {

        }
        rv_note.adapter = adapter
    }

    private fun loadData() {
        swipe_refresh_layout.isRefreshing = true
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getReleasedNote()
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data)
                adapter.notifyDataSetChanged()
                if (list.isEmpty()) {
                    toastInfo("空空如也")
                }
            } else {
                toastError("加载失败")
            }
        }, catchBlock = {
            toastError("加载失败")
        }, finallyBlock = {
            swipe_refresh_layout.isRefreshing = false
        })
    }

}