package com.autowheel.bangbang.ui.index.activity

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.FileProvider
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BackBaseActivity
import com.autowheel.bangbang.model.network.RetrofitHelper
import com.autowheel.bangbang.utils.FileUtil
import com.autowheel.bangbang.utils.gone
import com.autowheel.bangbang.utils.toastError
import com.autowheel.bangbang.utils.toastSuccess
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_help_pick.*
import kotlinx.android.synthetic.main.layout_dialog_imgtype.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Created by Xily on 2020/5/28.
 */
class HelpPickActivity : BackBaseActivity(), View.OnClickListener {
    private lateinit var dialog: Dialog
    private var file: File? = null
    private var helpId = 0
    override fun getToolbarTitle(): String {
        return "帮扶打卡"
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_help_pick
    }

    override fun initViews(savedInstanceState: Bundle?) {
        helpId = intent.getIntExtra("id", 0)
        initUploadImageDialog()
        btn_clock.setOnClickListener {
            if (file == null) {
                toastError("请先选择图片!")
            } else {
                pick(file!!)
            }
        }
    }

    private fun initUploadImageDialog() {
        iv_upload.setOnClickListener {
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
                            this@HelpPickActivity,
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
                1 -> {
                    file = File(externalCacheDir, "output_image.jpg").also {
                        setImage(it)
                    }
                }
                2 -> {
                    val imagePath: String? = FileUtil.getPath(this, data?.data!!)
                    if (imagePath != null) {
                        file = File(imagePath).also {
                            setImage(it)
                        }
                    } else {
                        toastError("获取图片失败！！")
                    }
                }
            }
        }
    }

    private fun setImage(file: File) {
        iv_upload.setImageBitmap(BitmapFactory.decodeStream(FileInputStream(file)))
        group_upload.gone()
    }

    private fun pick(file: File) {
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val coupleId =
            RequestBody.create(MediaType.parse("multipart/form-data"), helpId.toString())
        val proDialog = ProgressDialog(this)
        proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        proDialog.setMessage("打卡中,请稍候..")
        proDialog.isIndeterminate = false
        proDialog.setCancelable(false)
        proDialog.show()
        launch(tryBlock = {
            val result = RetrofitHelper.getApiService().pickUpHelp(coupleId, body)
            if (result.code == 0) {
                toastSuccess("打卡成功!")
                finish()
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

}