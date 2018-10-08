package com.photo.doc.dagger.component

import com.photo.doc.dagger.module.AboutModule
import com.photo.doc.ui.activities.about.AboutActivity
import dagger.Component

@Component(modules = [AboutModule::class])
interface AboutComponent {
    fun inject(aboutActivity: AboutActivity)
}