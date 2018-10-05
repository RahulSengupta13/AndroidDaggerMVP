package com.example.daggermvp.dagger.module

import android.app.Activity
import com.example.daggermvp.ui.activities.welcome.WelcomeContract
import com.example.daggermvp.ui.activities.welcome.WelcomePresenter
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