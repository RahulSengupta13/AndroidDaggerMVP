package com.example.daggermvp.dagger.component

import com.example.daggermvp.dagger.module.FullScreenImageModule
import com.example.daggermvp.ui.activities.fullscreenimage.FullScreenImageActivity
import dagger.Component

@Component(modules = [FullScreenImageModule::class])
interface FullScreenImageComponent {
    fun inject(fullScreenImageActivity: FullScreenImageActivity)
}