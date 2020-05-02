package com.autowheel.bangbang.ui.index.activity

import android.app.ProgressDialog
import android.os.Bundle
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.model.network.bean.GradeBean
import com.autowheel.bangbang.utils.*
import kotlinx.android.synthetic.main.activity_publish.*

/**
 * Created by Xily on 2020/4/5.
 */
class PublishActivity : BackBaseActivity() {
    private var isCourse = true
    private var list = arrayListOf<GradeBean>()
    private var fileId = 0
    override fun getToolbarTitle(): String {
        return "发布辅导"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_publish
    }

    override fun initViews(savedInstanceState: Bundle?) {
        rg_type.setOnCheckedChangeListener { group, checkedId ->
            isCourse = checkedId == R.id.rb_course
            initLayout()
            if (isCourse && list.isEmpty()) {
                loadGrade()
            }
        }
        btn_pubish.setOnClickListener {
            publish()
        }
    }

    private fun loadGrade() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("正在加载中,请稍候..")
        progressDialog.show()
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().getGrade()
            if (result.code == 0) {
                list.clear()
                list.addAll(result.data.filter {
                    (it.point.toDoubleOrNull() ?: 0.0) >= 90 || it.point == "优秀"
                })
                spinner.attachDataSource<String>(list.map { it.name })
            } else {
                toastError("加载失败")
            }
        }, catchBlock = {
            toastError("加载失败")
        }, finallyBlock = {
            progressDialog.dismiss()
        })
    }

    private fun publish() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("提交中,请稍候..")
        progressDialog.show()
        launch(tryBlock = {
            val result = if (isCourse) {
                if (list.isEmpty()) {
                    toastError("课程加载不成功或没有可辅导课程，请重新进入后再试")
                    return@launch
                } else {
                    val gradeBean = list[spinner.selectedIndex]
                    RetrofitHelper.getApiService().publishCoach(
                        "course",
                        gradeBean.name,
                        gradeBean.point,
                        gradeBean.token,
                        "",
                        "",
                        et_declaration.text.toString(),
                        "0"
                    )
                }
            } else {
                val name = et_skill_name.text.toString()
                if (name.isEmpty()) {
                    toastError("技能名字必填")
                    return@launch
                } else if (fileId == 0) {
                    toastError("必须上传证明文件")
                    return@launch
                } else {
                    RetrofitHelper.getApiService().publishCoach(
                        "skill",
                        "",
                        "",
                        "",
                        name,
                        fileId.toString(),
                        et_declaration.text.toString(),
                        "0"
                    )
                }
            }
            if (result.code == 0) {
                toastSuccess("发布成功!")
                startActivity<DetailActivity>("id" to result.data.id)
                finish()
            } else {
                toastError(result.msg)
            }
        }, catchBlock = {
            it.printStackTrace()
            toastError("网络请求出错!")
        }, finallyBlock = {
            progressDialog.dismiss()
        })
    }

    private fun initLayout() {
        if (isCourse) {
            layout_course.visible()
            layout_skill.gone()
        } else {
            layout_skill.visible()
            layout_course.gone()
        }
    }

}