package com.autowheel.bangbang.ui.index.activity

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
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
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Xily on 2020/5/28.
 */
class HelpPickActivity : BackBaseActivity(), View.OnClickListener {
    private lateinit var dialog: Dialog
    private var bitmap: Bitmap? = null
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
            if (bitmap == null) {
                toastError("请先选择图片!")
            } else {
                pick(bitmap!!)
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
                    bitmap = decodeFileToBitmapByDegree(
                        File(
                            externalCacheDir,
                            "output_image.jpg"
                        ).path
                    )?.also {
                        setImage(it)
                    }
                }
                2 -> {
                    val imagePath: String? = FileUtil.getPath(this, data?.data!!)
                    if (imagePath != null) {
                        bitmap = decodeFileToBitmapByDegree(imagePath)?.also {
                            setImage(it)
                        }
                    } else {
                        toastError("获取图片失败！！")
                    }
                }
            }
        }
    }

    private fun setImage(bitmap: Bitmap) {
        iv_upload.setImageBitmap(bitmap)
        group_upload.gone()
    }

    private fun pick(bitmap: Bitmap) {
        val file = File(externalCacheDir, "${System.currentTimeMillis()}.jpg")
        val bos = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        bos.flush()
        bos.close()
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

    private fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    private fun decodeFileToBitmapByDegree(path: String): Bitmap? {
        val degree = readPictureDegree(path).toFloat()
        val bm = BitmapFactory.decodeFile(path)
        var returnBm: Bitmap? = null
        // 根据旋转角度，生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(degree)
        val less = if (bm.width > bm.height)
            bm.height
        else
            bm.width
        var i = 1f
        if (less > 1080) {
            i = 1080f / less
        }
        matrix.postScale(i, i)
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(
                bm, 0, 0, bm.width,
                bm.height, matrix, true
            )
        } catch (e: OutOfMemoryError) {
        }

        if (returnBm == null) {
            returnBm = bm
        }
        if (bm != returnBm) {
            bm.recycle()
        }
        return returnBm
    }
}