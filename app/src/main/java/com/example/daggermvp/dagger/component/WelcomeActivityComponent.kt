package com.example.daggermvp.dagger.component

import com.example.daggermvp.ui.activities.welcome.WelcomeActivity
import com.example.daggermvp.dagger.module.WelcomeActivityModule
import dagger.Component

@Component(modules = [WelcomeActivityModule::class])
interface WelcomeActivityComponent {
    fun inject(welcomeActivity: WelcomeActivity)
}