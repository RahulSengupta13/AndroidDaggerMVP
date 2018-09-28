package com.example.daggermvp.dagger.module

import android.app.Activity
import com.example.daggermvp.ui.autovision.AutoVisionContract
import com.example.daggermvp.ui.autovision.AutoVisionPresenter
import dagger.Module
import dagger.Provides

@Module
class AutoVisionModule(private var activity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun providePresenter(): AutoVisionContract.Presenter {
        return AutoVisionPresenter()
    }
}