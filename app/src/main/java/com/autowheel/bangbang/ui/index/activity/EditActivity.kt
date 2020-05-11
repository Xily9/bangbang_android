package com.autowheel.bangbang.ui.index.activity

import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastSuccess
import kotlinx.android.synthetic.main.activity_edit.*

/**
 * Created by Xily on 2020/5/10.
 */
class EditActivity : BackBaseActivity() {
    private var id = 0
    override fun getToolbarTitle(): String {
        return "编辑辅导"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit
    }

    override fun initViews(savedInstanceState: Bundle?) {
        id = intent.getIntExtra("id", 0)
        et_declaration.setText(intent.getStringExtra("declaration"))
        btn_submit.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val declaration = et_declaration.text.toString()
        if (declaration.isBlank()) {
            toastError("辅导宣言不能为空！")
        } else {
            launch(tryBlock = {
                val result = RetrofitHelper.getApiService().editCoach(id, declaration)
                if (result.code == 0) {
                    toastSuccess("编辑成功！")
                    startActivity<DetailActivity>("id" to id)
                    finish()
                } else {
                    toastError(result.msg)
                }
            }, catchBlock = {
                it.printStackTrace()
                toastError("网络请求出错!")
            }, finallyBlock = {

            })
        }
    }
}