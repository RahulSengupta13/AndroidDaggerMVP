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
import android.net.Uri
import android.widget.Toast
import android.util.Log
import com.example.daggermvp.BuildConfig
import java.io.IOException
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.model.AnnotateImageRequest
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest
import com.example.daggermvp.utils.PackageManagerUtils
import com.google.api.services.vision.v1.VisionRequest
import com.google.api.services.vision.v1.VisionRequestInitializer
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.extensions.android.http.AndroidHttp
import com.example.daggermvp.R
import com.google.api.services.vision.v1.model.Feature
import com.google.api.services.vision.v1.model.Image
import java.io.ByteArrayOutputStream

class AutoVisionActivity : AppCompatActivity(), AutoVisionContract.View {

    @Inject
    lateinit var presenter: AutoVisionContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_vision)
        title = "Know the logo"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        injectDependency()
        presenter.attach(this)

        btnGallery.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                    .setMessage(getString(R.string.image_source))
                    .setPositiveButton(getString(R.string.camera)) { _, _ -> startCamera() }
                    .setNegativeButton(getString(R.string.gallery)) { _, _ -> startGalleryChooser() }
                    .create()
            builder.show()
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_IMAGE_REQUEST -> {
                if (data != null && resultCode == Activity.RESULT_OK) {
                    uploadImage(data.data)
                }
            }
            CAMERA_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val photoUri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", getCameraFile())
                    uploadImage(photoUri)
                }
            }
        }
    }

    private fun uploadImage(uri: Uri?) {
        if (uri != null) {
            try {
                val bitmap = scaleBitmapDown(
                        MediaStore.Images.Media.getBitmap(contentResolver, uri),
                        MAX_DIMENSION)
                callCloudVision(bitmap)
                galleryImageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                Log.d(TAG, "Image picking failed because ${e.message}")
                Toast.makeText(this, "Image picking failed because ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.")
            Toast.makeText(this, "Image picker gave us a null image.", Toast.LENGTH_LONG).show()
        }
    }

    private fun callCloudVision(bitmap: Bitmap?) {
        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
        try {
            val labelDetectionTask = LabelDetectionTask(this, prepareAnnotationRequest(bitmap
                    ?: return))
            labelDetectionTask.execute()
        } catch (e: IOException) {
            Log.d(TAG, "failed to make API request because of other IOException " + e.message)
        }
    }

    private fun prepareAnnotationRequest(bitmap: Bitmap): Vision.Images.Annotate {
        val httpTransport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()

        val requestInitializer = object : VisionRequestInitializer(CLOUD_VISION_API_KEY) {
            override fun initializeVisionRequest(visionRequest: VisionRequest<*>?) {
                super.initializeVisionRequest(visionRequest)

                val packageName = packageName
                visionRequest!!.requestHeaders.set(ANDROID_PACKAGE_HEADER, packageName)
                val sig = PackageManagerUtils.getSignature(packageManager, packageName)
                visionRequest.requestHeaders.set(ANDROID_CERT_HEADER, sig)
            }
        }

        val builder = Vision.Builder(httpTransport, jsonFactory, null)
        builder.setVisionRequestInitializer(requestInitializer)
        val vision = builder.build()
        val batchAnnotateImagesRequest = BatchAnnotateImagesRequest()

        batchAnnotateImagesRequest.requests = object : ArrayList<AnnotateImageRequest>() {
            init {
                val annotateImageRequest = AnnotateImageRequest()
                val base64EncodedImage = Image()
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()
                base64EncodedImage.encodeContent(imageBytes)
                annotateImageRequest.image = base64EncodedImage
                annotateImageRequest.features = object : ArrayList<Feature>() {
                    init {
                        val labelDetection = Feature()
                        labelDetection.type = "LABEL_DETECTION"
                        labelDetection.maxResults = MAX_LABEL_RESULTS
                        add(labelDetection)
                    }
                }
                add(annotateImageRequest)
            }
        }

        val annotateRequest = vision.images().annotate(batchAnnotateImagesRequest)
        annotateRequest.disableGZipContent = true
        Log.d(TAG, "created Cloud Vision request object, sending request")

        return annotateRequest
    }

    private fun scaleBitmapDown(bitmap: Bitmap?, maxDimension: Int): Bitmap? {
        val originalWidth = bitmap?.width
        val originalHeight = bitmap?.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension
        if (originalHeight != null) {
            when {
                originalHeight > originalWidth!! -> {
                    resizedHeight = maxDimension
                    resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
                }
                originalWidth > originalHeight -> {
                    resizedWidth = maxDimension
                    resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
                }
                originalHeight == originalWidth -> {
                    resizedHeight = maxDimension
                    resizedWidth = maxDimension
                }
            }
        }
        return Bitmap.createScaledBitmap(bitmap!!, resizedWidth, resizedHeight, false)
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
        private const val MAX_LABEL_RESULTS = 10
        private const val MAX_DIMENSION = 1200

        private const val CLOUD_VISION_API_KEY = BuildConfig.API_KEY
        private const val ANDROID_CERT_HEADER = "X-Android-Cert"
        private const val ANDROID_PACKAGE_HEADER = "X-Android-Package"
        private const val FILE_NAME = "temp.jpg"

        const val TAG = "AutoVisionActivity"

        fun getIntent(context: Context): Intent = Intent(context, AutoVisionActivity::class.java)
    }
}
