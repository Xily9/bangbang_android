package com.autowheel.bangbang.ui.user

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.core.content.FileProvider
import com.autowheel.bangbang.R
import com.autowheel.bangbang.base.BaseActivity
import com.autowheel.bangbang.utils.FileUtil
import com.autowheel.bangbang.utils.toastError
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_avatar.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Xily on 2020/3/30.
 */
class AvatarActivity : BaseActivity() {
    private var imageUri: Uri? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_avatar
    }

    override fun initViews(savedInstanceState: Bundle?) {
        initToolbar()
        btn_photo.setOnClickListener {
            camera()
        }
        btn_album.setOnClickListener {
            album()
        }
    }

    override fun initToolbar() {
        setSupportActionBar(toolbar)
        toolbar_title.text = "修改头像"
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
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
                            this@AvatarActivity,
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
                    photoClip(it)
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
                        photoClip(imageUri)
                    } else {
                        toastError("获取图片失败！！")
                    }
                }
                3 -> {
                    val bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
                    uploadFace(bitmap)
                }
            }
        }
    }

    private fun uploadFace(bitmap: Bitmap) {
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

    private fun photoClip(uri: Uri) {
        val outputImage = File(externalCacheDir, "output_image_clip.jpg")
        try {
            if (outputImage.exists()) outputImage.delete()
            outputImage.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        imageUri = if (Build.VERSION.SDK_INT >= 24) {
            val uri2 =
                FileProvider.getUriForFile(this, "com.autowheel.bangbang.fileProvider", outputImage)
            grantUriPermission(packageName, uri2, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            uri
        } else {
            Uri.fromFile(outputImage)
        }
        // 调用系统中自带的图片剪裁
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300)
        intent.putExtra("outputY", 300)
        intent.putExtra("return-data", false)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, 3)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish() // back button
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}