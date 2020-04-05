package com.autowheel.bangbang.ui.note

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.ui.note.handler.CodeEditHandler
import com.autowheel.bangbang.ui.note.handler.StrikeThroughEditHandler
import com.autowheel.bangbang.utils.FileUtil
import com.autowheel.bangbang.utils.startActivity
import com.autowheel.bangbang.utils.text
import com.autowheel.bangbang.utils.toastError
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
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by Xily on 2020/4/5.
 */
class NotePublishActivity : BackBaseActivity(), View.OnClickListener {
    private lateinit var markwon: Markwon
    private lateinit var dialog: Dialog
    private var imageUri: Uri? = null
    override fun getToolbarTitle(): String {
        return "发布笔记"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_note_publish
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initEditor()
        initUploadImageDialog()
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
                    imageUri = if (Build.VERSION.SDK_INT >= 24) {
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
                1 -> imageUri?.let {
                    val bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(it))
                    uploadImage(bitmap)
                }
                2 -> {
                    val imagePath: String? = FileUtil.getPath(this, data?.data!!)
                    if (imagePath != null) {
                        val file = File(imagePath)
                        val imageUri = if (Build.VERSION.SDK_INT >= 24) {
                            FileProvider.getUriForFile(
                                this,
                                "com.autowheel.bangbang.fileProvider",
                                file
                            )
                        } else {
                            Uri.fromFile(file)
                        }
                        imageUri?.let {
                            val bitmap =
                                BitmapFactory.decodeStream(contentResolver.openInputStream(it))
                            uploadImage(bitmap)
                        }
                    } else {
                        toastError("获取图片失败！！")
                    }
                }
            }
        }
    }

    private fun uploadImage(bitmap: Bitmap) {
        val file = File(externalCacheDir, "${System.currentTimeMillis()}.jpg")
        val bos = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        bos.flush()
        bos.close()
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("avatar", file.name, requestFile)
        val proDialog = ProgressDialog(this)
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        proDialog.setMessage("上传中,请稍候..")
        proDialog.isIndeterminate = false
        proDialog.setCancelable(false)
        proDialog.show()
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
                    "content" to et_content.text()
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}