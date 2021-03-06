package com.autowheel.bangbang.ui.index.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import co.lujun.androidtagview.TagView
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseActivity
import com.autowheel.bangbang.model.DataManager
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.CoachBean
import com.autowheel.bangbang.ui.index.adapter.CoachAdapter
import com.autowheel.bangbang.utils.*
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Created by Xily on 2020/4/2.
 */
class SearchActivity : BaseActivity() {
    private lateinit var adapter: CoachAdapter
    private val list = arrayListOf<CoachBean>()
    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initSearchView()
        initRecyclerView()
        layout_clear.setOnClickListener {
            DataManager.clearSearchHistory()
            tagGroup.removeAllTags()
        }
        tagGroup.tags = DataManager.getSearchHistory()
        tagGroup.setOnTagClickListener(object : TagView.OnTagClickListener {
            override fun onSelectedTagDrag(position: Int, text: String?) {
            }

            override fun onTagLongClick(position: Int, text: String?) {
            }

            override fun onTagCrossClick(position: Int) {
            }

            override fun onTagClick(position: Int, text: String?) {
                et_search.setText(text)
                iv_search.performClick()
            }
        })
    }

    private var isFirst = true
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && isFirst) {
            isFirst = false
            et_search.isFocusable = true
            et_search.isFocusableInTouchMode = true
            et_search.requestFocus()
            showSoftInput(et_search)
        }
    }

    private fun initSearchView() {
        //window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        //showSoftInput(et_search)
        iv_back.setOnClickListener {
            onBackPressed()
        }
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isNotEmpty()) {
                        iv_close.visible()
                    } else {
                        iv_close.gone()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        et_search.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                layout_empty.visible()
                rv_coach.gone()
                if (et_search.text.isNotEmpty()) {
                    iv_close.visible()
                } else {
                    iv_close.gone()
                }
            }
        }
        iv_close.setOnClickListener {
            et_search.setText("")
        }
        et_search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                iv_search.performClick()
            }
            false
        }
        iv_search.setOnClickListener {
            val text = et_search.text.toString()
            if (text.isNotBlank()) {
                tagGroup.addTag(text, 0)
                DataManager.addSearchHistory(text)
                iv_close.gone()
                layout_empty.gone()
                rv_coach.visible()
                hideSoftInput()
                et_search.clearFocus()
                if (text.isInt()) {
                    startActivity<DetailActivity>("id" to text.toInt())
                    finish()
                } else {
                    /*if (text.startsWith("UID:", true)) {
                        val id = text.replace("UID:", "", true)
                        if (id.isNotBlank() && id.isInt()) {
                            startActivity<UserActivity>("id" to id.toInt())
                            finish()
                        } else {
                            search(text)
                        }
                    } else {*/
                    search(text)
                    //}
                }
            }
        }
    }

    private fun initRecyclerView() {
        rv_coach.layoutManager = LinearLayoutManager(this)
        adapter = CoachAdapter(list)
        adapter.setOnItemClickListener {
            startActivity<DetailActivity>("id" to list[it].help_id)
        }
        rv_coach.adapter = adapter
    }

    private fun search(word: String) {
        progressBar.visible()
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().searchCoach(word)
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data)
                adapter.notifyDataSetChanged()
                if (list.isEmpty()) {
                    toastWarning("搜索结果为空呢,换个关键词试试吧!")
                }
            } else {
                toastError(result.msg)
            }
        }, catchBlock = {
            toastError("网络请求出错!")
        }, finallyBlock = {
            progressBar.gone()
        })
    }
}