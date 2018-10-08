package com.photo.doc.dagger.component

import com.photo.doc.ui.activities.welcome.WelcomeActivity
import com.photo.doc.dagger.module.WelcomeActivityModule
import dagger.Component

@Component(modules = [WelcomeActivityModule::class])
interface WelcomeActivityComponent {
    fun inject(welcomeActivity: WelcomeActivity)
}