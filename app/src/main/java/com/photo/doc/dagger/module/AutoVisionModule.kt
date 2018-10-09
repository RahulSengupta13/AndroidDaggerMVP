package com.photo.doc.dagger.module

import android.app.Activity
import com.photo.doc.ui.activities.autovision.*
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
        return AutoVisionPresenter(activity)
    }
}