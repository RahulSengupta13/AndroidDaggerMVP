package com.photo.doc

import android.app.Application
import com.photo.doc.dagger.component.ApplicationComponent
import com.photo.doc.dagger.component.DaggerApplicationComponent
import com.photo.doc.dagger.module.ApplicationModule

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