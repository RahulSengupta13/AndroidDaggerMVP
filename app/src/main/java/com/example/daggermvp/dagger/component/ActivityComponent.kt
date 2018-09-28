package com.example.daggermvp.dagger.component

import com.example.daggermvp.ui.main.MainActivity
import com.example.daggermvp.dagger.module.ActivityModule
import dagger.Component

@Component(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)
}