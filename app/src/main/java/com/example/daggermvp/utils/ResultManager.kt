package com.example.daggermvp.utils

import android.net.Uri
import com.google.api.services.vision.v1.model.EntityAnnotation

object ResultManager {

    var photoUri = Uri.EMPTY!!
    var labelAnnotations = mutableListOf<EntityAnnotation>()
    var logoAnnotations = mutableListOf<EntityAnnotation>()

}