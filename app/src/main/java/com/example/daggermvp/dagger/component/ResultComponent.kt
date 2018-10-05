package com.example.daggermvp.dagger.component

import com.example.daggermvp.dagger.module.ResultModule
import com.example.daggermvp.ui.activities.result.ResultActivity
import dagger.Component

@Component(modules = [ResultModule::class])
interface ResultComponent {
    fun inject(resultActivity: ResultActivity)
}