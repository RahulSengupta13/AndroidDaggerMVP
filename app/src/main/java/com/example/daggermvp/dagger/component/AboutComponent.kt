package com.example.daggermvp.dagger.component

import com.example.daggermvp.dagger.module.AboutModule
import com.example.daggermvp.ui.activities.about.AboutActivity
import dagger.Component

@Component(modules = [AboutModule::class])
interface AboutComponent {
    fun inject(aboutActivity: AboutActivity)
}