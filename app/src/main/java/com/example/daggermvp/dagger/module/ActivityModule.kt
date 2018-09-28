package com.example.daggermvp.dagger.module

import android.app.Activity
import com.example.daggermvp.ui.main.MainContract
import com.example.daggermvp.ui.main.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private var activity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun providePresenter(): MainContract.Presenter {
        return MainPresenter()
    }
}