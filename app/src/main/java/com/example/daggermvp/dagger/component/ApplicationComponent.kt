package com.example.daggermvp.dagger.component

import com.example.daggermvp.BaseApp
import com.example.daggermvp.dagger.module.ApplicationModule
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: BaseApp)

}