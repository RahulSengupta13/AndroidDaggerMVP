package com.example.daggermvp.ui.activities.autovision

import android.graphics.Bitmap
import android.net.Uri
import com.example.daggermvp.ui.BaseContract
import com.google.api.services.vision.v1.Vision

class AutoVisionContract {

    interface View: BaseContract.View {
        fun setResultImage(bitmap: Bitmap)
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun uploadImage(uri: Uri?)
        fun callCloudVision(bitmap: Bitmap)
        fun prepareAnnotationRequest(bitmap: Bitmap): Vision.Images.Annotate
    }
}