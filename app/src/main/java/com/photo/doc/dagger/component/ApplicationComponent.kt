package com.photo.doc.dagger.component

import com.photo.doc.BaseApp
import com.photo.doc.dagger.module.ApplicationModule
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(application: BaseApp)
}