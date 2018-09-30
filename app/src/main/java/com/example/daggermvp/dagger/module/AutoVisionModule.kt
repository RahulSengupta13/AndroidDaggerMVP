package com.example.daggermvp.dagger.module

import android.app.Activity
import com.example.daggermvp.ui.autovision.*
import dagger.Module
import dagger.Provides

@Module
class AutoVisionModule(private var activity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun provideContextWrapperRepository(): AutoVisionContract.Presenter {
        return AutoVisionRepositoryImpl(activity)
    }
}