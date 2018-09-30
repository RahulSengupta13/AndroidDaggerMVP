package com.example.daggermvp.ui.autovision

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.example.daggermvp.dagger.component.DaggerAutoVisionComponent
import com.example.daggermvp.dagger.module.AutoVisionModule
import kotlinx.android.synthetic.main.activity_auto_vision.*
import javax.inject.Inject
import com.example.daggermvp.utils.PermissionUtils
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File
import android.os.Environment
import com.example.daggermvp.R

class AutoVisionActivity : AppCompatActivity(), AutoVisionContract.View, AutoVisionRepositoryContract.View {

    @Inject
    lateinit var presenter: AutoVisionContract.Presenter

    @Inject
    lateinit var autoVisionRepository: AutoVisionRepositoryContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_vision)
        title = "Know your image"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        injectDependency()

        presenter.attach(this)
        autoVisionRepository.attach(this)

        btnGallery.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                    .setMessage(getString(R.string.image_source))
                    .setPositiveButton(getString(R.string.camera)) { _, _ -> startCamera() }
                    .setNegativeButton(getString(R.string.gallery)) { _, _ -> startGalleryChooser() }
                    .create()
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_IMAGE_REQUEST -> {
                if (data != null && resultCode == Activity.RESULT_OK) {
                    autoVisionRepository.uploadImage(data.data)
                }
            }
            CAMERA_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val photoUri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", getCameraFile())
                    autoVisionRepository.uploadImage(photoUri)
                }
            }
        }
    }

    override fun setResultImage(bitmap: Bitmap) {
        galleryImageView.setImageBitmap(bitmap)
    }

    private fun startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_photo)),
                    GALLERY_IMAGE_REQUEST)
        }
    }

    private fun startCamera() {
        if (PermissionUtils.requestPermission(this,
                        CAMERA_PERMISSIONS_REQUEST,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA))) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoUri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", getCameraFile())
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST)
        }
    }

    private fun getCameraFile(): File {
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(dir, FILE_NAME)
    }

    private fun injectDependency() {
        val autoVisionComponent = DaggerAutoVisionComponent.builder()
                .autoVisionModule(AutoVisionModule(this))
                .build()
        autoVisionComponent.inject(this)
    }

    companion object {
        private const val GALLERY_PERMISSIONS_REQUEST = 0
        private const val GALLERY_IMAGE_REQUEST = 1
        private const val CAMERA_PERMISSIONS_REQUEST = 2
        private const val CAMERA_IMAGE_REQUEST = 3
        private const val FILE_NAME = "temp.jpg"

        const val TAG = "AutoVisionActivity"

        fun getIntent(context: Context): Intent = Intent(context, AutoVisionActivity::class.java)
    }
}
