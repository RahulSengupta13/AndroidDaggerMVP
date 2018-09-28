package com.example.daggermvp

import android.app.Application
import com.example.daggermvp.dagger.component.ApplicationComponent
import com.example.daggermvp.dagger.component.DaggerApplicationComponent
import com.example.daggermvp.dagger.module.ApplicationModule

class BaseApp : Application() {
    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        instance = this
        setup()

        if (BuildConfig.DEBUG) {
//            Do whatever
        }
    }

    private fun setup() {
        component = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        component.inject(this)
    }

    fun getApplicationComponent(): ApplicationComponent {
        return component
    }

    companion object {
        lateinit var instance: BaseApp private set
    }
}