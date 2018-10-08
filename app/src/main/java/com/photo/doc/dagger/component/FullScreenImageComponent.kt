package com.photo.doc.dagger.component

import com.photo.doc.dagger.module.FullScreenImageModule
import com.photo.doc.ui.activities.fullscreenimage.FullScreenImageActivity
import dagger.Component

@Component(modules = [FullScreenImageModule::class])
interface FullScreenImageComponent {
    fun inject(fullScreenImageActivity: FullScreenImageActivity)
}