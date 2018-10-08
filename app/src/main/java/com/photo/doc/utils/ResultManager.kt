package com.photo.doc.utils

import android.net.Uri
import com.google.api.services.vision.v1.model.EntityAnnotation
import com.google.api.services.vision.v1.model.WebDetection
import com.photo.doc.models.WebPageWithImage

object ResultManager {

    var photoUri = Uri.EMPTY!!
    var labelAnnotations = mutableListOf<EntityAnnotation>()
    var logoAnnotations = mutableListOf<EntityAnnotation>()
    var webAnnotation = WebDetection()
    var matchingPagesWithImages = mutableListOf<WebPageWithImage>()

    fun clearResults() {
        labelAnnotations = mutableListOf()
        logoAnnotations = mutableListOf()
        webAnnotation = WebDetection()
        matchingPagesWithImages = mutableListOf()
    }

}