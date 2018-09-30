package com.example.daggermvp.ui.autovision

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.daggermvp.BuildConfig
import com.example.daggermvp.utils.BitmapUtils
import java.io.IOException
import com.example.daggermvp.utils.PackageManagerUtils
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.VisionRequest
import com.google.api.services.vision.v1.VisionRequestInitializer
import com.google.api.services.vision.v1.model.AnnotateImageRequest
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest
import com.google.api.services.vision.v1.model.Feature
import com.google.api.services.vision.v1.model.Image
import java.io.ByteArrayOutputStream

class AutoVisionRepositoryImpl constructor(private val activity: Activity) : AutoVisionContract.Presenter {

    private lateinit var view: AutoVisionContract.View

    override fun prepareAnnotationRequest(bitmap: Bitmap): Vision.Images.Annotate {
        val httpTransport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()

        val requestInitializer = object : VisionRequestInitializer(CLOUD_VISION_API_KEY) {
            override fun initializeVisionRequest(visionRequest: VisionRequest<*>?) {
                super.initializeVisionRequest(visionRequest)

                val packageName = activity.packageName
                visionRequest!!.requestHeaders.set(ANDROID_PACKAGE_HEADER, packageName)
                val sig = PackageManagerUtils.getSignature(activity.packageManager, packageName)
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
                        val labelDetection = Feature().apply{
                            type = "LABEL_DETECTION"
                            maxResults = MAX_LABEL_RESULTS
                        }
                        add(labelDetection)

                        val logoDetection = Feature().apply {
                            type = "LOGO_DETECTION"
                            maxResults = MAX_LOGO_RESULTS
                        }
                        add(logoDetection)

//                        val webDetection = Feature().apply {
//                            type = "WEB_DETECTION"
//                            maxResults = MAX_LOGO_RESULTS
//                        }
//                        add(webDetection)
                    }
                }
                add(annotateImageRequest)
            }
        }

        val annotateRequest = vision.images().annotate(batchAnnotateImagesRequest)
        annotateRequest.disableGZipContent = true
        Log.d(AutoVisionActivity.TAG, "created Cloud Vision request object, sending request")

        return annotateRequest
    }

    override fun callCloudVision(bitmap: Bitmap) {
        try {
            val labelDetectionTask = LabelDetectionTask(
                    activity as AutoVisionActivity,
                    prepareAnnotationRequest(bitmap))
            labelDetectionTask.execute()
        } catch (e: IOException) {
            Log.d(AutoVisionActivity.TAG, "failed to make API request because of other IOException " + e.message)
        }
    }

    override fun uploadImage(uri: Uri?) {
        if (uri != null) {
            try {
                val bitmap = BitmapUtils.scaleBitmapDown(
                        MediaStore.Images.Media.getBitmap(activity.contentResolver, uri),
                        MAX_DIMENSION) ?: return
                callCloudVision(bitmap)
                view.setResultImage(bitmap)
            } catch (e: IOException) {
                Log.d(AutoVisionActivity.TAG, "Image picking failed because ${e.message}")
            }
        } else {
            Log.d(AutoVisionActivity.TAG, "Image picker gave us a null image.")
        }
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }

    override fun attach(view: AutoVisionContract.View) {
        this.view = view
    }

    companion object {
        private const val MAX_LABEL_RESULTS = 10
        private const val MAX_LOGO_RESULTS = 10
        private const val MAX_DIMENSION = 1200

        private const val CLOUD_VISION_API_KEY = BuildConfig.API_KEY
        private const val ANDROID_CERT_HEADER = "X-Android-Cert"
        private const val ANDROID_PACKAGE_HEADER = "X-Android-Package"
    }
}