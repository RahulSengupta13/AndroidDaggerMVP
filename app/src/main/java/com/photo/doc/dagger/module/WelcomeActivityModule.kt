package com.photo.doc.dagger.module

import android.app.Activity
import com.photo.doc.ui.activities.welcome.WelcomeContract
import com.photo.doc.ui.activities.welcome.WelcomePresenter
import dagger.Module
import dagger.Provides

@Module
class WelcomeActivityModule(private var activity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun providePresenter(): WelcomeContract.Presenter {
        return WelcomePresenter()
    }
}