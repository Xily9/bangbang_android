package com.autowheel.bangbang.ui.note.activity

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.FileProvider
import com.autowheel.bangbang.BASE_URL
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.ui.note.handler.CodeEditHandler
import com.autowheel.bangbang.ui.note.handler.StrikeThroughEditHandler
import com.autowheel.bangbang.utils.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import io.noties.markwon.editor.handler.EmphasisEditHandler
import io.noties.markwon.editor.handler.StrongEmphasisEditHandler
import kotlinx.android.synthetic.main.activity_note_publish.*
import kotlinx.android.synthetic.main.layout_dialog_imgtype.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException


/**
 * Created by Xily on 2020/4/5.
 */
class NotePublishActivity : BackBaseActivity(), View.OnClickListener {
    private lateinit var markwon: Markwon
    private lateinit var dialog: Dialog
    override fun getToolbarTitle(): String {
        return "发布笔记"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_note_publish
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initEditor()
        initUploadImageDialog()
        btn_insert_file.setOnClickListener {
            Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val intent = Intent("android.intent.action.GET_CONTENT")
                        intent.type = "*/*"
                        startActivityForResult(intent, 3)
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    }
                }).check()
        }
        //startActivity<NoteDetailActivity>()
    }

    private fun initEditor() {
        markwon = Markwon.create(this)
        val editor = MarkwonEditor.builder(markwon)
            .useEditHandler(CodeEditHandler())
            .useEditHandler(EmphasisEditHandler())
            .useEditHandler(StrongEmphasisEditHandler())
            .useEditHandler(StrikeThroughEditHandler())
            .build()
        et_content.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor))
    }

    private fun initUploadImageDialog() {
        btn_insert_image.setOnClickListener {
            dialog = Dialog(this, R.style.ActionSheetDialogStyle2)
            val inflater = layoutInflater.inflate(R.layout.layout_dialog_imgtype, null)
            dialog.setContentView(inflater)
            inflater.tv_photo.setOnClickListener(this)
            inflater.tv_alarm.setOnClickListener(this)
            inflater.tv_close.setOnClickListener(this)
            dialog.window?.apply {
                setGravity(Gravity.BOTTOM)
                val lp = attributes
                lp.width = LinearLayout.LayoutParams.MATCH_PARENT
                attributes = lp
            }
            dialog.show()//显示对话框
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_photo -> camera()
            R.id.tv_alarm -> album()
        }
        dialog.dismiss()
    }

    private fun camera() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    val outputImage = File(externalCacheDir, "output_image.jpg")
                    try {
                        if (outputImage.exists()) outputImage.delete()
                        outputImage.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val imageUri = if (Build.VERSION.SDK_INT >= 24) {
                        FileProvider.getUriForFile(
                            this@NotePublishActivity,
                            "com.autowheel.bangbang.fileProvider",
                            outputImage
                        )
                    } else {
                        Uri.fromFile(outputImage)
                    }
                    val intent1 = Intent("android.media.action.IMAGE_CAPTURE")
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(intent1, 1)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                }
            }).check()
    }

    private fun album() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    val intent = Intent("android.intent.action.GET_CONTENT")
                    intent.type = "image/*"
                    startActivityForResult(intent, 2)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                }
            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1 -> uploadFile(File(externalCacheDir, "output_image.jpg"), true)
                2 -> {
                    val imagePath: String? = FileUtil.getPath(this, data?.data!!)
                    if (imagePath != null) {
                        val file = File(imagePath)
                        uploadFile(file, true)
                    } else {
                        toastError("获取图片失败！！")
                    }
                }
                3 -> {
                    val filePath: String? = FileUtil.getPath(this, data?.data!!)
                    if (filePath != null) {
                        val file = File(filePath)
                        uploadFile(file, false)
                    } else {
                        toastError("获取文件失败！！")
                    }
                }
            }
        }
    }

    private fun uploadFile(file: File, isImage: Boolean) {
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val proDialog = ProgressDialog(this)
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        proDialog.setMessage("上传中,请稍候..")
        proDialog.isIndeterminate = false
        proDialog.setCancelable(false)
        proDialog.show()
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().uploadNoteFile(body)
            if (result.code == 0) {
                toastSuccess("上传成功!")
                et_content.append(if (isImage) "![图片](${BASE_URL}/note/file/${result.data.file_id})" else "[点击下载附件](${BASE_URL}/note/file/${result.data.file_id})")
            } else {
                toastError(result.msg)
            }
        }, catchBlock = {
            it.printStackTrace()
            toastError("网络请求出错!")
        }, finallyBlock = {
            proDialog.dismiss()
        })
    }

    private fun publish() {
        val title = et_title.text()
        val content = et_content.text()
        val tag = et_tag.text()
        if (title.isBlank()) {
            toastError("标题不能为空!")
        } else if (tag.isBlank()) {
            toastError("TAG不能为空!")
        } else if (content.isBlank()) {
            toastError("笔记内容不能为空!")
        } else {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("提交中,请稍候..")
            progressDialog.show()
            launch(tryBlock = {
                val result =
                    RetrofitHelper.getApiService().publishNote(title, tag, content)
                if (result.code == 0) {
                    toastSuccess("发布成功!")
                    startActivity<NoteDetailActivity>("id" to result.data.id)
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_note_publish_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_preview -> {
                startActivity<NotePreviewActivity>(
                    "title" to et_title.text(),
                    "content" to et_content.text(),
                    "tag" to et_tag.text()
                )
            }
            R.id.action_finish -> {
                publish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}