package com.example.daggermvp.dagger.component

import com.example.daggermvp.dagger.module.AutoVisionModule
import com.example.daggermvp.ui.autovision.AutoVisionActivity
import dagger.Component

@Component(modules = [AutoVisionModule::class])
interface AutoVisionComponent {
    fun inject(autoVisionActivity: AutoVisionActivity)
}