package com.photo.doc.models

import com.google.api.services.vision.v1.model.WebPage

data class WebPageWithImage(val webPage: WebPage,
                            val imageUrl: String)