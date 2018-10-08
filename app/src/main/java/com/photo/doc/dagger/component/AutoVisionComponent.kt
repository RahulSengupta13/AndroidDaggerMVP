package com.photo.doc.dagger.component

import com.photo.doc.dagger.module.AutoVisionModule
import com.photo.doc.ui.activities.autovision.AutoVisionActivity
import dagger.Component

@Component(modules = [AutoVisionModule::class])
interface AutoVisionComponent {
    fun inject(autoVisionActivity: AutoVisionActivity)
}