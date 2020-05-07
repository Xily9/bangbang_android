package com.autowheel.bangbang.ui.note.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseFragment
import com.autowheel.bangbang.model.bean.NoteTagBean
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.NoteBean
import com.autowheel.bangbang.ui.note.activity.NoteDetailActivity
import com.autowheel.bangbang.ui.note.activity.NoteSearchActivity
import com.autowheel.bangbang.ui.note.adapter.NoteAdapter
import com.autowheel.bangbang.ui.note.adapter.TagAdapter
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.toastInfo
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.item_fragment_note_content_header.view.*

/**
 * Created by Xily on 2020/4/1.
 */
class NoteFragment : BaseFragment() {
    private lateinit var tagAdapter: TagAdapter
    private lateinit var noteAdapter: NoteAdapter
    private var tagList = arrayListOf<NoteTagBean>()
    private var noteList = arrayListOf<NoteBean>()
    private lateinit var noteHeaderView: View
    private var totalDataCount: Int = 0
    private var page = 1
    private var curSelectTagIndex = 0
    private val eachPage = 20
    override fun getLayoutId(): Int {
        return R.layout.fragment_note
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolbar()
        initRecyclerView()
        loadTags()
        swipe_refresh_layout.setColorSchemeResources(R.color.blue)
        swipe_refresh_layout.setOnRefreshListener {
            page = 1
            //startActivity<ChatActivity>("id" to 1)
            loadData(tagList[curSelectTagIndex].name, true)
        }
    }

    private fun initRecyclerView() {
        rv_tag.layoutManager = LinearLayoutManager(requireContext())
        tagAdapter = TagAdapter(tagList)
        tagAdapter.setOnItemClickListener {
            if (!tagList[it].isSelected) {
                tagList[curSelectTagIndex].isSelected = false
                tagList[it].isSelected = true
                tagAdapter.notifyItemChanged(curSelectTagIndex)
                tagAdapter.notifyItemChanged(it)
                curSelectTagIndex = it
                page = 1
                loadData(tagList[curSelectTagIndex].name, true)
            }
        }
        rv_tag.adapter = tagAdapter
        rv_note.layoutManager = LinearLayoutManager(requireContext())
        noteAdapter = NoteAdapter(noteList)
        noteHeaderView =
            layoutInflater.inflate(R.layout.item_fragment_note_content_header, rv_note, false)
        noteAdapter.headerView = noteHeaderView
        noteAdapter.setOnItemClickListener {
            startActivity<NoteDetailActivity>("id" to noteList[it].note_id)
        }
        rv_note.adapter = noteAdapter
        rv_note.enableLoadMore()
        rv_note.loadingMoreListener = {
            if (totalDataCount > noteList.size) {
                page++
                loadData(tagList[curSelectTagIndex].name)
            }
        }
    }

    override fun initToolbar() {
        toolbar_title.text = "笔记"
        iv_search.setOnClickListener {
            startActivity<NoteSearchActivity>()
        }
    }

    private fun loadTags() {
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getNoteTags()
            if (result.code == 0) {
                tagList.clear()
                tagList.addAll(result.data.map { NoteTagBean(it, false) })
                curSelectTagIndex = 0
                if (tagList.isEmpty()) {
                    toastInfo("空空如也!")
                } else {
                    tagList[0].isSelected = true
                    tagAdapter.notifyDataSetChanged()
                    page = 1
                    loadData(tagList[0].name, true)
                }
            }
        })
    }

    private fun loadData(tag: String, isRefreshing: Boolean = false) {
        launch(tryBlock = {
            if (isRefreshing) {
                swipe_refresh_layout.isRefreshing = true
            } else {
                rv_note.showLoading()
            }
            val result = RetrofitHelper.getApiService().getNotes(tag, page, eachPage)
            if (result.code == 0) {
                totalDataCount = result.length
                if (isRefreshing) {
                    noteHeaderView.tv_count.text = "共${totalDataCount}个内容"
                }
                if (totalDataCount == 0) {
                    toastInfo("空空如也!")
                } else {
                    if (isRefreshing) {
                        noteList.clear()
                    }
                    noteList.addAll(result.data)
                    noteAdapter.notifyDataSetChanged()
                }
            }
            rv_note.showComplete()
        }, catchBlock = {
            it.printStackTrace()
            rv_note.showError()
        }, finallyBlock = {
            if (isRefreshing) {
                swipe_refresh_layout.isRefreshing = false
            }
        })
    }
}